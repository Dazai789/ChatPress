# chatpress-v1

chatpress-v1 是一个面向 AI 内容沉淀场景的轻量级知识页面发布系统。它的目标不是做一个传统博客，而是把 AI 聊天记录、学习笔记和 Markdown 内容整理成可保存、可维护、可公开访问的知识页面。

## 当前定位

这个项目目前处于后端 MVP 阶段，已经完成一条基础内容发布链路：

```text
输入标题、slug、Markdown 内容
-> 保存为 Artifact
-> 渲染为 HTML
-> 控制草稿或发布状态
-> 通过 /p/{slug} 公开访问
```

项目后续会继续向 ChatPress 的核心方向推进：

```text
AI 聊天记录
-> 保存原始记录
-> 解析 User / Assistant 消息
-> 转换成 Markdown 草稿
-> 复用现有 Artifact 发布链路
```

## 已完成能力

- Spring Boot 项目骨架。
- Maven 依赖和测试流程。
- H2 内存数据库。
- Spring Data JPA 数据访问。
- Artifact 创建、列表、详情、更新、删除。
- slug 唯一性校验。
- Markdown 转 HTML。
- 公开页面访问：`GET /p/{slug}`。
- 公开页面基础样式。
- 公开页面标题 HTML 转义。
- 草稿 / 发布状态控制。
- 公开页面只展示 `published` 内容。
- `sourceType` 来源类型：`markdown` / `ai_chat`。
- 统一错误响应。
- 基础接口测试。

## 当前接口

```text
POST   /api/artifacts
GET    /api/artifacts
GET    /api/artifacts/{id}
PUT    /api/artifacts/{id}
PUT    /api/artifacts/{id}/status
DELETE /api/artifacts/{id}
GET    /p/{slug}
```

## 核心数据对象

当前只有一个核心实体：`Artifact`。

主要字段：

| 字段 | 说明 |
|---|---|
| `id` | 内部主键 |
| `title` | 页面标题 |
| `slug` | 公开 URL 标识 |
| `sourceFormat` | 内容格式，目前固定为 `markdown` |
| `sourceType` | 内容来源：`markdown` 或 `ai_chat` |
| `sourceContent` | 用户输入的原始内容 |
| `renderedHtml` | Markdown 渲染后的 HTML |
| `status` | `draft` 或 `published` |
| `createdAt` | 创建时间 |
| `updatedAt` | 更新时间 |

## 技术栈

- Java 21
- Spring Boot 3.5.14
- Maven
- H2
- Spring Data JPA
- Bean Validation
- CommonMark
- JUnit / MockMvc

当前数据库是 H2 内存数据库。运行或测试时会临时创建，项目停止后数据会消失。等功能稳定后再切换 MySQL。

## 项目结构

```text
src/main/java/com/chatpress/v1/
  ChatpressV1Application.java
  artifact/
    Artifact.java
    ArtifactController.java
    ArtifactRepository.java
    ArtifactService.java
    MarkdownRenderer.java
    PublicPageController.java
    PublicPageRenderer.java
    dto/
      ArtifactRequest.java
      ArtifactResponse.java
      ArtifactStatusRequest.java
      ArtifactSummaryResponse.java
    exception/
      ArtifactNotFoundException.java
      DuplicateSlugException.java
  common/
    ApiErrorResponse.java
    ApiExceptionHandler.java
  system/
    HealthController.java
src/test/java/com/chatpress/v1/
  ChatpressV1ApplicationTests.java
  artifact/ArtifactControllerTest.java
  system/HealthControllerTest.java
docs/
  API.md
  DATA_MODEL.md
  PRODUCT.md
```

## 文档入口

- [产品定义](docs/PRODUCT.md)
- [数据模型](docs/DATA_MODEL.md)
- [API 设计](docs/API.md)

## 当前进度

基础内容系统已经完成。按完整产品 MVP 估算，当前约完成 35%。后端基础能力已经比较完整，但 ChatPress 的差异化能力才刚开始。

下一步建议做：

```text
AI 聊天记录解析
```

先支持一种简单格式：

```text
User: ...
Assistant: ...
```

然后把它转换成 Markdown 草稿，再复用现有 Markdown 渲染和公开发布链路。
