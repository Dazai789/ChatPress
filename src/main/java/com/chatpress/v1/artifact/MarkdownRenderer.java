package com.chatpress.v1.artifact;

import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.task.list.items.TaskListItemsExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MarkdownRenderer {

    private final List<Extension> extensions = List.of(
            AutolinkExtension.create(),
            StrikethroughExtension.create(),
            TablesExtension.create(),
            TaskListItemsExtension.create()
    );

    private final Parser parser = Parser.builder()
            .extensions(extensions)
            .build();

    private final HtmlRenderer renderer = HtmlRenderer.builder()
            .extensions(extensions)
            .build();

    public String render(String markdown) {
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }
}
