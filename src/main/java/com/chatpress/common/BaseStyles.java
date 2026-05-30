package com.chatpress.common;

/**
 * Shared CSS fragments used across HTML renderers. Reduces duplication
 * while keeping each renderer self-contained for its page-specific layout.
 */
public final class BaseStyles {

    private BaseStyles() {}

    /** Body, font, and base typography — used by all admin pages. */
    public static final String ADMIN_BASE = """
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
            a { color: #0f766e; text-decoration-thickness: 1px; text-underline-offset: 3px; }
            .button-link {
                display: inline-flex; align-items: center; height: 36px;
                padding: 0 12px; border-radius: 6px;
                background: #0f766e; color: #ffffff; text-decoration: none;
            }
            .secondary-link {
                display: inline-flex; align-items: center; height: 34px;
                padding: 0 12px; border: 1px solid #c9c6bd; border-radius: 6px;
                background: #ffffff; text-decoration: none; color: #242424;
            }
            .topbar {
                display: flex; align-items: center; justify-content: space-between; gap: 16px;
            }
            .muted, .empty { color: #737373; }
            @media (max-width: 760px) {
                .shell { padding: 18px; }
                .topbar { align-items: flex-start; flex-direction: column; }
            }
            """;

    /** Form inputs — used by AdminFormRenderer, AdminMarkdownImportRenderer. */
    public static final String FORM_INPUTS = """
            input, select, textarea, button {
                border: 1px solid #c9c6bd; border-radius: 6px;
                background: #ffffff; color: #242424; font: inherit;
            }
            input, select, textarea {
                width: 100%%; box-sizing: border-box; padding: 10px 12px;
            }
            input, select { height: 42px; }
            label { display: block; margin: 0 0 8px; color: #404040; font-weight: 650; }
            .field { margin-bottom: 18px; }
            .actions { display: flex; align-items: center; gap: 12px; }
            button {
                height: 40px; padding: 0 16px; border-color: #0f766e;
                background: #0f766e; color: #ffffff; cursor: pointer;
            }
            .error {
                margin: 0 0 18px; padding: 10px 12px;
                border: 1px solid #f1b4b4; border-radius: 6px;
                background: #fff5f5; color: #991b1b;
            }
            """;

    /** Status badge — used by AdminPageRenderer, AdminDetailRenderer, AdminDeleteRenderer. */
    public static final String STATUS_PILL = """
            .status {
                display: inline-block; min-width: 76px; padding: 3px 8px;
                border-radius: 999px; background: #e7eee9; color: #166534;
                font-size: 0.82rem; text-align: center;
            }
            .status.draft { background: #eee7db; color: #92400e; }
            """;

    /** Tag badge — used by AdminPageRenderer, PublicListRenderer. */
    public static final String TAG_PILL = """
            .tag {
                display: inline-block; padding: 2px 8px; border-radius: 999px;
                background: #e0f2f1; color: #0f766e; font-size: 0.78rem;
            }
            """;

    /** Pager links — used by AdminPageRenderer, AdminLogRenderer. */
    public static final String PAGER = """
            .pager {
                display: flex; align-items: center; justify-content: space-between;
                gap: 12px; margin-top: 16px; color: #525252;
            }
            .pager nav { display: flex; gap: 8px; }
            .pager a, .pager span.link-disabled {
                display: inline-flex; align-items: center; height: 36px;
                padding: 0 12px; border: 1px solid #c9c6bd; border-radius: 6px;
                background: #ffffff; text-decoration: none;
            }
            .pager span.link-disabled { color: #a3a3a3; }
            """;
}
