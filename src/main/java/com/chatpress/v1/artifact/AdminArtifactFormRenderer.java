package com.chatpress.v1.artifact;

import org.springframework.stereotype.Component;

@Component
public class AdminArtifactFormRenderer {

    public String render(String title, String sourceContent, String errorMessage) {
        return """
                <!doctype html>
                <html lang="en">
                <head>
                    <meta charset="utf-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <link rel="icon" href="data:,">
                    <title>New Artifact - Admin</title>
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
                            max-width: 900px;
                            margin: 0 auto;
                            padding: 24px;
                        }

                        .topbar {
                            display: flex;
                            align-items: center;
                            justify-content: space-between;
                            gap: 16px;
                        }

                        h1 {
                            margin: 0;
                            font-size: 1.5rem;
                            font-weight: 650;
                            letter-spacing: 0;
                        }

                        a {
                            color: #0f766e;
                            text-decoration-thickness: 1px;
                            text-underline-offset: 3px;
                        }

                        form {
                            margin-top: 24px;
                        }

                        label {
                            display: block;
                            margin: 0 0 8px;
                            color: #404040;
                            font-weight: 650;
                        }

                        input,
                        textarea,
                        button {
                            border: 1px solid #c9c6bd;
                            border-radius: 6px;
                            background: #ffffff;
                            color: #242424;
                            font: inherit;
                        }

                        input,
                        textarea {
                            width: 100%%;
                            box-sizing: border-box;
                            padding: 10px 12px;
                        }

                        input {
                            height: 42px;
                        }

                        textarea {
                            min-height: 360px;
                            resize: vertical;
                            line-height: 1.6;
                            font-family: "SFMono-Regular", Consolas, "Liberation Mono", monospace;
                        }

                        .field {
                            margin-bottom: 18px;
                        }

                        .actions {
                            display: flex;
                            align-items: center;
                            gap: 12px;
                        }

                        button {
                            height: 40px;
                            padding: 0 16px;
                            border-color: #0f766e;
                            background: #0f766e;
                            color: #ffffff;
                            cursor: pointer;
                        }

                        .error {
                            margin: 0 0 18px;
                            padding: 10px 12px;
                            border: 1px solid #f1b4b4;
                            border-radius: 6px;
                            background: #fff5f5;
                            color: #991b1b;
                        }
                    </style>
                </head>
                <body>
                    <header>
                        <div class="shell topbar">
                            <h1>New Artifact</h1>
                            <a href="/admin/artifacts">Back to list</a>
                        </div>
                    </header>
                    <main class="shell">
                        %s
                        <form method="post" action="/admin/artifacts">
                            <div class="field">
                                <label for="title">Title</label>
                                <input id="title" name="title" value="%s" maxlength="200" required>
                            </div>
                            <div class="field">
                                <label for="sourceContent">Markdown</label>
                                <textarea id="sourceContent" name="sourceContent" required>%s</textarea>
                            </div>
                            <div class="actions">
                                <button type="submit">Save</button>
                                <a href="/admin/artifacts">Cancel</a>
                            </div>
                        </form>
                    </main>
                </body>
                </html>
                """.formatted(
                renderError(errorMessage),
                escapeHtml(title),
                escapeHtml(sourceContent)
        );
    }

    private String renderError(String errorMessage) {
        if (errorMessage == null || errorMessage.isBlank()) {
            return "";
        }
        return "<p class=\"error\">%s</p>".formatted(escapeHtml(errorMessage));
    }

    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
