# chatpress-v1 产品定义

## 1. 产品概述

chatpress-v1 是一个面向 AI 内容沉淀场景的轻量级知识页面发布系统。

它的核心目标是把 AI 聊天记录、Markdown 笔记和学习整理内容转化为结构化的 `Artifact`，并发布为可访问、可分享的网页。

当前第一阶段已经完成基础 Artifact 发布系统。下一阶段开始进入更贴近项目核心的能力：AI 聊天记录导入与整理。

## 2. 产品定位

chatpress-v1 位于 AI 聊天工具、笔记工具、博客系统之间。

AI 聊天工具适合生成内容，但内容分散在聊天窗口里。传统笔记工具适合记录，但公开分享和结构化发布能力有限。博客系统可以发布内容，但对零散 AI 对话和学习笔记来说流程偏重。

chatpress-v1 的定位是：

```text
把 AI 对话和 Markdown 笔记变成可维护、可分享的知识页面。
```

## 3. 目标用户

第一版主要面向：

- 高频使用 ChatGPT、Claude、Gemini、Cursor、Codex 等 AI 工具的学习者。
- 想把 AI 对话沉淀成学习笔记或项目文档的个人用户。
- 需要整理技术笔记、调研记录和项目过程的开发者。
- 面向求职展示，希望把学习和项目过程整理成可访问页面的用户。

第一版不面向多人协作团队、通用博客平台或内容社区。

## 4. 核心场景

当前已经完成的基础场景：

```text
用户输入 Markdown 内容
-> 创建 Artifact
-> 系统保存原始内容
-> 系统渲染 HTML
-> 用户设置 draft / published
-> published 内容可通过 /p/{slug} 公开访问
```

下一阶段核心场景：

```text
用户粘贴 AI 聊天记录
-> 系统识别为 sourceType = ai_chat
-> 解析 User / Assistant 消息
-> 生成 Markdown 草稿
-> 复用现有 Artifact 发布链路
```

## 5. MVP 已完成范围

已完成：

- 创建 artifact。
- 编辑 artifact。
- 删除 artifact。
- 查看 artifact 列表。
- 查看 artifact 详情。
- Markdown 渲染为 HTML。
- 基于 slug 的公开页面访问。
- 简单公开页面样式。
- 标题 HTML 转义。
- 统一异常返回。
- slug 重复校验。
- 草稿 / 发布状态控制。
- 公开页面只展示 `published` 内容。
- 内容来源字段 `sourceType`：`markdown` / `ai_chat`。
- 基础测试覆盖。

## 6. 当前非目标

当前暂不实现：

- 真实 AI 模型调用。
- 自动读取 ChatGPT / Claude / Gemini 网页聊天记录。
- 浏览器扩展。
- 用户账号和权限系统。
- 多用户协作。
- 文件上传。
- DOCX / PDF 解析。
- 语义搜索。
- 知识图谱。
- 评论系统。
- Redis。
- MCP server。
- 托管 SaaS。
- 复杂部署自动化。

这些能力可以作为后续版本方向，但不进入当前阶段。

## 7. 当前用户流程

```text
创建 artifact
-> 输入 title
-> 输入 slug
-> 选择或默认 sourceType
-> 粘贴 Markdown 或 AI 聊天记录文本
-> 保存
-> 修改状态为 published
-> 打开公开页面 URL
```

公开 URL 示例：

```text
/p/my-java-note
```

## 8. 核心对象

当前核心对象是 `Artifact`。

`Artifact` 表示一篇可发布的知识页面，包含：

- 标题。
- slug。
- 内容格式。
- 内容来源。
- 原始内容。
- 渲染 HTML。
- 状态。
- 创建时间。
- 更新时间。

## 9. 产品原则

- 先打通完整链路，再做复杂功能。
- 保留原始内容，不丢失用户输入。
- 后端逻辑优先清晰可理解。
- `sourceType` 用于区分内容来源，不急着做复杂解析。
- AI 聊天记录导入先做规则版，不直接接 LLM。
- 每一步都配测试，避免后面改乱。

## 10. 下一阶段

下一阶段建议实现：

```text
AI 聊天记录解析
```

第一版只支持一种简单格式：

```text
User: 我的问题
Assistant: AI 的回答

User: 下一个问题
Assistant: 下一个回答
```

目标不是一次做完整 AI 导入，而是先完成：

```text
ai_chat 原始文本
-> messages
-> Markdown 草稿
-> renderedHtml
```
