# chatpress-v1 产品定义

## 1. 产品概述

chatpress-v1 是一个轻量级 Markdown 页面发布系统。

它的第一版核心目标是把 Markdown 笔记和学习整理内容转化为结构化的 `Artifact`，并发布为可访问、可分享的 HTML 页面。

当前第一阶段已经完成基础 Artifact 发布系统。下一阶段继续专注 Markdown 导入、HTML 渲染和公开页面体验，不急着进入复杂 AI 聊天记录导入。

## 2. 产品定位

chatpress-v1 第一版位于 Markdown 笔记工具和轻量页面发布工具之间。

传统笔记工具适合记录，但公开分享和结构化发布能力有限。博客系统可以发布内容，但对学习笔记和项目过程记录来说流程偏重。

chatpress-v1 的定位是：

```text
把 Markdown 笔记变成可维护、可分享的知识页面。
```

## 3. 目标用户

第一版主要面向：

- 使用 Markdown 整理学习笔记和项目记录的个人用户。
- 想把本地笔记快速发布成 HTML 页面的人。
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

下一阶段仍然围绕这条链路打磨：让 Markdown 输入、HTML 渲染和公开页面展示更稳定、更像一个可用产品。

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
- 内容来源字段 `sourceType`，V1 主要使用 `markdown`。
- 基础测试覆盖。

## 6. 当前非目标

当前暂不实现：

- 真实 AI 模型调用。
- 自动读取 ChatGPT / Claude / Gemini 网页聊天记录。
- AI 聊天记录解析。
- 复制网页 HTML 后自动清洗成 Markdown。
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
-> 默认使用 sourceType = markdown
-> 粘贴 Markdown 内容
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
- V1 先专注 Markdown 导入和 HTML 渲染。
- `sourceType` 保留扩展空间，但当前不围绕 AI chat 展开。
- 每一步都配测试，避免后面改乱。

## 10. 下一阶段

下一阶段建议实现：

```text
Markdown 发布体验打磨
```

优先级：

```text
1. 优化公开页面 HTML 样式。
2. 完善 Markdown 渲染效果，重点关注标题、列表、代码块、链接。
3. 增加最小可用创建页面。
4. 再考虑 Markdown 预览。
```

AI 聊天记录导入、网页 HTML 导入、浏览器扩展放到后续版本：

```text
V2/V3: AI chat 或 HTML 导入
-> 清洗内容
-> 转成 Markdown
-> 复用 Artifact 发布链路
```
