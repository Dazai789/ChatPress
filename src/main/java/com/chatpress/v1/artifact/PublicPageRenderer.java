package com.chatpress.v1.artifact;

import org.springframework.stereotype.Component;

@Component
public class PublicPageRenderer {

    public String render(Artifact artifact) {
        return """
                <!doctype html>
                <html lang="en">
                <head>
                    <meta charset="utf-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <title>%s</title>
                </head>
                <body>
                    <main>
                        %s
                    </main>
                </body>
                </html>
                """.formatted(artifact.getTitle(), artifact.getRenderedHtml());
    }
}
