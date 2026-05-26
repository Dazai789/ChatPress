# chatpress-v1 数据模型

## 1. 概述

当前 MVP 使用一个核心数据对象：`Artifact`。

一个 `Artifact` 表示一篇可以保存、编辑、发布和公开访问的知识页面。V1 内容主要来自 Markdown。

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
| `sourceType` | `Artifact.SourceType` | VARCHAR(50) | 是 | 内容来源。V1 主要使用 `MARKDOWN`，`AI_CHAT` 保留为后续扩展。 |
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
| `markdown` | 普通 Markdown 笔记，V1 的主要内容来源。 |
| `ai_chat` | 后续 AI 聊天记录导入预留值，当前不做解析。 |

当前阶段只围绕 `markdown` 完善导入和 HTML 渲染。`ai_chat` 保留在模型中，但不是 V1 的开发重点。

## 5. 字段说明

### `slug`

`slug` 用于公开 URL。

示例：

```text
/p/spring-boot-notes
```

规则：

- 后端根据 `title` 自动生成。
- API 请求中不需要传入。
- 唯一。
- 只允许小写字母、数字和单横线。
- 不能以横线开头或结尾。
- 不能出现连续横线。
- 创建时自动检查重复，重复时追加数字后缀。

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

V1 中通常是：

```text
sourceFormat: markdown
sourceType: markdown
```

### `sourceContent`

保存用户输入的原始内容。

普通 Markdown 示例：

```markdown
# Spring Boot Notes

Controller receives HTTP requests.
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
- API 请求中不接收 `slug`。
- `slug` 必须唯一。
- `slug` 格式必须匹配 `[a-z0-9]+(-[a-z0-9]+)*`。
- `sourceContent` 不能为空。
- API 请求中不接收 `sourceType`。
- V1 创建和更新时 `sourceType` 固定为 `markdown`。
- `sourceFormat` 当前固定为 `markdown`。
- 修改 `sourceContent` 时必须重新生成 `renderedHtml`。
- 公开页面只展示 `published` 内容。

## 7. 示例记录

```text
id: 1
title: Spring Boot Notes
slug: spring-boot-notes
sourceFormat: markdown
sourceType: markdown
sourceContent: # Spring Boot Notes
renderedHtml: <h1>Spring Boot Notes</h1>
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

V1 暂时不新增表，继续围绕 `Artifact` 完成 Markdown 导入和 HTML 渲染。

后续版本如果支持 AI 聊天记录、标签、版本或附件，可能新增：

- `ChatMessage`
- `ArtifactVersion`
- `Tag`
- `ArtifactTag`
- `Asset`
- `User`

当前阶段继续保持单表，避免过早复杂化。
