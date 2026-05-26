# chatpress-v1

chatpress-v1 是一个轻量级 Markdown 页面发布系统。它的第一版目标不是做传统博客，也不是马上处理复杂 AI 对话导入，而是先把 Markdown 内容稳定地保存、渲染并发布成可公开访问的 HTML 页面。

## 当前定位

这个项目目前处于后端 MVP 阶段。V1 当前专注一条基础 Markdown 发布链路：

```text
输入标题、Markdown 内容
-> 保存为 Artifact
-> 后端自动生成 slug
-> 渲染为 HTML
-> 控制草稿或发布状态
-> 通过 /p/{slug} 公开访问
```

后续版本再逐步扩展到 AI 对话导入：

```text
AI 聊天记录或网页 HTML
-> 提取可发布内容
-> 转换成 Markdown 或结构化内容
-> 复用现有 HTML 发布链路
```

## 已完成能力

- Spring Boot 项目骨架。
- Maven 依赖和测试流程。
- H2 内存数据库。
- Spring Data JPA 数据访问。
- Artifact 创建、列表、详情、更新、删除。
- slug 自动生成和唯一性处理。
- Markdown 转 HTML。
- 公开页面访问：`GET /p/{slug}`。
- 公开页面基础样式。
- 公开页面标题 HTML 转义。
- 草稿 / 发布状态控制。
- 公开页面只展示 `published` 内容。
- `sourceType` 来源类型，目前 V1 主要使用 `markdown`。
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
| `slug` | 后端自动生成的公开 URL 标识 |
| `sourceFormat` | 内容格式，目前固定为 `markdown` |
| `sourceType` | 内容来源，V1 主要使用 `markdown`，`ai_chat` 作为后续扩展 |
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

基础 Markdown 发布链路已经完成。按 V1 Markdown Publisher 估算，当前后端基础能力已经比较完整，下一步应优先把 Markdown 页面发布体验做扎实。

下一步建议做：

```text
Markdown 发布体验打磨
```

优先方向：

```text
优化公开页面 HTML 样式
完善 Markdown 渲染效果
再考虑 Markdown 预览
```
