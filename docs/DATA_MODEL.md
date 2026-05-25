# chatpress-v1 数据模型

## 1. 概述

当前 MVP 使用一个核心数据对象：`Artifact`。

一个 `Artifact` 表示一篇可以保存、编辑、发布和公开访问的知识页面。内容可以来自普通 Markdown，也可以来自 AI 聊天记录。

当前数据模型的重点是先保存原始内容，并生成可展示的 HTML：

```text
sourceContent
-> MarkdownRenderer
-> renderedHtml
-> PublicPageRenderer
-> /p/{slug}
```

## 2. 主表：`artifact`

当前只有一张核心表：

```text
artifact
```

它同时保存：

- 用户输入的原始内容。
- Markdown 渲染后的 HTML。
- 公开访问所需的 slug。
- 草稿 / 发布状态。
- 内容来源类型。

## 3. 字段

| 字段 | Java 类型 | 数据库含义 | 必填 | 说明 |
|---|---|---|---:|---|
| `id` | `Long` | BIGINT | 是 | 主键，内部标识。 |
| `title` | `String` | VARCHAR(200) | 是 | 页面标题。 |
| `slug` | `String` | VARCHAR(200) | 是 | 公开 URL 标识，唯一。 |
| `sourceFormat` | `String` | VARCHAR(50) | 是 | 内容格式，目前固定为 `markdown`。 |
| `sourceType` | `Artifact.SourceType` | VARCHAR(50) | 是 | 内容来源：`MARKDOWN` 或 `AI_CHAT`。 |
| `sourceContent` | `String` | CLOB / TEXT | 是 | 用户输入的原始内容。 |
| `renderedHtml` | `String` | CLOB / TEXT | 是 | 渲染后的 HTML。 |
| `status` | `Artifact.Status` | VARCHAR(50) | 是 | 状态：`DRAFT` 或 `PUBLISHED`。 |
| `createdAt` | `LocalDateTime` | Timestamp | 是 | 创建时间。 |
| `updatedAt` | `LocalDateTime` | Timestamp | 是 | 更新时间。 |

## 4. 枚举

### `Artifact.Status`

代码中：

```java
public enum Status {
    DRAFT,
    PUBLISHED
}
```

API 返回：

```text
draft
published
```

规则：

- 后台详情和列表可以看到所有状态。
- 公开页面 `/p/{slug}` 只允许访问 `PUBLISHED`。
- `DRAFT` 内容即使 slug 正确，也返回 404。

### `Artifact.SourceType`

代码中：

```java
public enum SourceType {
    MARKDOWN,
    AI_CHAT
}
```

API 返回：

```text
markdown
ai_chat
```

含义：

| 值 | 说明 |
|---|---|
| `markdown` | 普通 Markdown 笔记。 |
| `ai_chat` | AI 聊天记录或 AI 聊天整理内容。 |

当前阶段只保存 `sourceType`，还没有真正解析 AI 聊天记录。

## 5. 字段说明

### `slug`

`slug` 用于公开 URL。

示例：

```text
/p/spring-boot-notes
```

规则：

- 必填。
- 唯一。
- 创建和更新时都要检查重复。

### `sourceFormat`

`sourceFormat` 表示内容格式。

当前固定为：

```text
markdown
```

它和 `sourceType` 不一样：

```text
sourceFormat = 内容是什么格式
sourceType   = 内容从哪里来
```

例如：

```text
sourceFormat: markdown
sourceType: ai_chat
```

表示这篇内容来源于 AI 聊天记录，但当前保存和渲染时仍按 Markdown 文本处理。

### `sourceContent`

保存用户输入的原始内容。

普通 Markdown 示例：

```markdown
# Spring Boot Notes

Controller receives HTTP requests.
```

AI 聊天记录示例：

```text
User: Controller 是什么？
Assistant: Controller 是接收外部请求的入口。
```

### `renderedHtml`

由 `MarkdownRenderer` 从 `sourceContent` 生成。

示例：

```html
<h1>Spring Boot Notes</h1>
<p>Controller receives HTTP requests.</p>
```

当前创建和更新 Artifact 时会重新生成。

## 6. 约束

当前强制规则：

- `title` 不能为空。
- `slug` 不能为空。
- `slug` 必须唯一。
- `sourceContent` 不能为空。
- `sourceType` 如果传入，只能是 `markdown` 或 `ai_chat`。
- `sourceType` 不传时默认为 `markdown`。
- `sourceFormat` 当前固定为 `markdown`。
- 修改 `sourceContent` 时必须重新生成 `renderedHtml`。
- 公开页面只展示 `published` 内容。

## 7. 示例记录

```text
id: 1
title: Spring Boot Notes
slug: spring-boot-notes
sourceFormat: markdown
sourceType: ai_chat
sourceContent: User: Controller 是什么？
renderedHtml: <p>User: Controller 是什么？</p>
status: published
createdAt: 2026-05-25T20:00:00
updatedAt: 2026-05-25T20:00:00
```

## 8. 当前数据库状态

当前使用 H2 内存数据库。

含义：

```text
项目启动时创建数据库
项目停止后数据消失
```

这适合当前学习和开发阶段。等功能稳定后，再迁移到 MySQL。

## 9. 后续可能新增的数据结构

下一阶段做 AI 聊天记录解析时，可能先不建新表，只在 Service 内解析文本。

未来可能新增：

- `ChatMessage`
- `ArtifactVersion`
- `Tag`
- `ArtifactTag`
- `Asset`
- `User`

当前阶段继续保持单表，避免过早复杂化。
