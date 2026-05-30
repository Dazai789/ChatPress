package com.chatpress.artifact.renderer;

import com.chatpress.artifact.Artifact;
import com.chatpress.common.util.HtmlUtils;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class AdminPageRenderer {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public String render(Page<Artifact> artifacts, String q, String status, String tag) {
        String searchValue = normalize(q);
        String statusValue = normalize(status).toLowerCase(Locale.ROOT);
        String tagValue = normalize(tag).toLowerCase(Locale.ROOT);
        String rows = artifacts.getContent().stream()
                .map(this::renderRow)
                .collect(Collectors.joining());

        if (rows.isBlank()) {
            rows = """
                    <tr>
                        <td colspan="6" class="empty">No artifacts found</td>
                    </tr>
                    """;
        }

        return """
                <!doctype html>
                <html lang="en">
                <head>
                    <meta charset="utf-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <link rel="icon" href="data:,">
                    <title>Artifacts - Admin</title>
                    <style>
                        body {
                            margin: 0;
                            background: #f5f5f2;
                            color: #242424;
                            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", "Helvetica Neue", sans-serif;
                            font-size: 15px;
                        }

                        header {
                            border-bottom: 1px solid #dedbd2;
                            background: #ffffff;
                        }

                        .shell {
                            max-width: 1080px;
                            margin: 0 auto;
                            padding: 24px;
                        }

                        h1 {
                            margin: 0;
                            font-size: 1.5rem;
                            font-weight: 650;
                            letter-spacing: 0;
                        }

                        .topbar {
                            display: flex;
                            align-items: center;
                            justify-content: space-between;
                            gap: 16px;
                        }

                        .button-link {
                            display: inline-flex;
                            align-items: center;
                            height: 36px;
                            padding: 0 12px;
                            border-radius: 6px;
                            background: #0f766e;
                            color: #ffffff;
                            text-decoration: none;
                        }

                        .topbar-actions {
                            display: flex;
                            align-items: center;
                            gap: 10px;
                        }

                        .secondary-link {
                            display: inline-flex;
                            align-items: center;
                            height: 34px;
                            padding: 0 12px;
                            border: 1px solid #c9c6bd;
                            border-radius: 6px;
                            background: #ffffff;
                            text-decoration: none;
                        }

                        form {
                            display: grid;
                            grid-template-columns: minmax(140px, 1fr) 140px 140px auto;
                            gap: 10px;
                            margin: 24px 0 18px;
                        }

                        input,
                        select,
                        button {
                            height: 40px;
                            border: 1px solid #c9c6bd;
                            border-radius: 6px;
                            background: #ffffff;
                            color: #242424;
                            font: inherit;
                        }

                        input,
                        select {
                            padding: 0 12px;
                        }

                        button {
                            padding: 0 16px;
                            background: #0f766e;
                            color: #ffffff;
                            border-color: #0f766e;
                            cursor: pointer;
                        }

                        table {
                            width: 100%%;
                            border-collapse: collapse;
                            background: #ffffff;
                            border: 1px solid #dedbd2;
                        }

                        th,
                        td {
                            padding: 12px 14px;
                            border-bottom: 1px solid #ebe8df;
                            text-align: left;
                            vertical-align: middle;
                        }

                        th {
                            background: #eeece4;
                            color: #4a4a4a;
                            font-size: 0.82rem;
                            font-weight: 650;
                            text-transform: uppercase;
                        }

                        tr:last-child td {
                            border-bottom: 0;
                        }

                        a {
                            color: #0f766e;
                            text-decoration-thickness: 1px;
                            text-underline-offset: 3px;
                        }

                        a.button-link {
                            color: #ffffff;
                            text-decoration: none;
                        }

                        .status {
                            display: inline-block;
                            min-width: 76px;
                            padding: 3px 8px;
                            border-radius: 999px;
                            background: #e7eee9;
                            color: #166534;
                            font-size: 0.82rem;
                            text-align: center;
                        }

                        .status.draft {
                            background: #eee7db;
                            color: #92400e;
                        }

                        .tag {
                            display: inline-block;
                            padding: 2px 7px;
                            border-radius: 999px;
                            background: #e0f2f1;
                            color: #0f766e;
                            font-size: 0.78rem;
                        }

                        .tags-cell {
                            max-width: 180px;
                        }

                        .muted,
                        .empty {
                            color: #737373;
                        }

                        .pager {
                            display: flex;
                            align-items: center;
                            justify-content: space-between;
                            gap: 12px;
                            margin-top: 16px;
                            color: #525252;
                        }

                        .pager nav {
                            display: flex;
                            gap: 8px;
                        }

                        .pager a,
                        .pager span.link-disabled {
                            display: inline-flex;
                            align-items: center;
                            height: 36px;
                            padding: 0 12px;
                            border: 1px solid #c9c6bd;
                            border-radius: 6px;
                            background: #ffffff;
                            text-decoration: none;
                        }

                        .pager span.link-disabled {
                            color: #a3a3a3;
                        }

                        @media (max-width: 760px) {
                            .shell {
                                padding: 18px;
                            }

                            form {
                                grid-template-columns: 1fr;
                            }

                            .topbar {
                                align-items: flex-start;
                                flex-direction: column;
                            }

                            .topbar-actions {
                                flex-wrap: wrap;
                            }

                            table {
                                display: block;
                                overflow-x: auto;
                            }
                        }
                    </style>
                </head>
                <body>
                    <header>
                        <div class="shell topbar">
                            <h1>Artifacts</h1>
                            <div class="topbar-actions">
                                <a class="secondary-link" href="/admin/logs">Logs</a>
                                <a class="secondary-link" href="/admin/artifacts/import/markdown">Import</a>
                                <a class="button-link" href="/admin/artifacts/new">New</a>
                            </div>
                        </div>
                    </header>
                    <main class="shell">
                        <form method="get" action="/admin/artifacts">
                            <input type="search" name="q" value="%s" placeholder="Search title">
                            <input type="text" name="tag" value="%s" placeholder="Tag">
                            <select name="status">
                                <option value=""%s>All statuses</option>
                                <option value="published"%s>Published</option>
                                <option value="draft"%s>Draft</option>
                            </select>
                            <button type="submit">Search</button>
                        </form>
                        <table>
                            <thead>
                                <tr>
                                    <th>Title</th>
                                    <th>Tags</th>
                                    <th>Status</th>
                                    <th>Slug</th>
                                    <th>Created</th>
                                    <th>Public</th>
                                </tr>
                            </thead>
                            <tbody>
                                %s
                            </tbody>
                        </table>
                        <div class="pager">
                            <span>Page %d of %d · %d total</span>
                            <nav>
                                %s
                                %s
                            </nav>
                        </div>
                    </main>
                </body>
                </html>
                """.formatted(
                HtmlUtils.escapeHtml(searchValue),
                HtmlUtils.escapeHtml(tagValue),
                selected(statusValue.isBlank()),
                selected("published".equals(statusValue)),
                selected("draft".equals(statusValue)),
                rows,
                artifacts.getNumber() + 1,
                Math.max(artifacts.getTotalPages(), 1),
                artifacts.getTotalElements(),
                previousLink(artifacts, searchValue, tagValue, statusValue),
                nextLink(artifacts, searchValue, tagValue, statusValue)
        );
    }

    private String renderRow(Artifact artifact) {
        String status = artifact.getStatus().name().toLowerCase(Locale.ROOT);
        String titleLink = "<a href=\"/admin/artifacts/%d\">%s</a>".formatted(
                artifact.getId(),
                HtmlUtils.escapeHtml(artifact.getTitle())
        );
        String publicLink = artifact.getStatus() == Artifact.Status.PUBLISHED
                ? "<a href=\"/p/%s\">Open</a>".formatted(HtmlUtils.escapeHtml(artifact.getSlug()))
                : "<span class=\"muted\">Draft</span>";

        String tagsHtml = artifact.getTags().stream()
                .map(tag -> "<span class=\"tag\">" + HtmlUtils.escapeHtml(tag.getName()) + "</span>")
                .collect(Collectors.joining(" "));
        if (tagsHtml.isBlank()) {
            tagsHtml = "<span class=\"muted\">—</span>";
        }

        return """
                <tr>
                    <td>%s</td>
                    <td class="tags-cell">%s</td>
                    <td><span class="status %s">%s</span></td>
                    <td><code>%s</code></td>
                    <td>%s</td>
                    <td>%s</td>
                </tr>
                """.formatted(
                titleLink,
                tagsHtml,
                HtmlUtils.escapeHtml(status),
                HtmlUtils.escapeHtml(status),
                HtmlUtils.escapeHtml(artifact.getSlug()),
                artifact.getCreatedAt().format(DATE_TIME_FORMATTER),
                publicLink
        );
    }

    private String previousLink(Page<Artifact> artifacts, String q, String tag, String status) {
        if (!artifacts.hasPrevious()) {
            return "<span class=\"link-disabled\">Previous</span>";
        }
        return "<a href=\"%s\">Previous</a>".formatted(adminUrl(artifacts.getNumber() - 1, artifacts.getSize(), q, tag, status));
    }

    private String nextLink(Page<Artifact> artifacts, String q, String tag, String status) {
        if (!artifacts.hasNext()) {
            return "<span class=\"link-disabled\">Next</span>";
        }
        return "<a href=\"%s\">Next</a>".formatted(adminUrl(artifacts.getNumber() + 1, artifacts.getSize(), q, tag, status));
    }

    private String adminUrl(int page, int size, String q, String tag, String status) {
        StringBuilder url = new StringBuilder("/admin/artifacts?page=")
                .append(page)
                .append("&size=")
                .append(size);

        if (!q.isBlank()) {
            url.append("&q=").append(encode(q));
        }

        if (!tag.isBlank()) {
            url.append("&tag=").append(encode(tag));
        }

        if (!status.isBlank()) {
            url.append("&status=").append(encode(status));
        }

        return url.toString();
    }

    private String selected(boolean selected) {
        return selected ? " selected" : "";
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return value.trim();
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

}
