# chatpress-v1

chatpress-v1 是一个轻量级 Markdown 页面发布系统。它的核心目标是把 Markdown 内容保存为结构化的 `Artifact`，渲染成安全的 HTML，并通过公开 URL 访问。

## 当前定位

当前项目专注一条基础发布链路：

```text
输入标题和 Markdown 内容
-> 保存为 Artifact
-> 自动生成 slug
-> 渲染为 HTML
-> 设置 draft / published 状态
-> 通过 /p/{slug} 公开访问
```

AI 聊天记录导入、网页 HTML 导入和浏览器扩展暂不作为当前核心功能，后续可以复用现有 Artifact 发布链路扩展。

## 已完成能力

- Artifact 创建、列表、详情、编辑、删除。
- Markdown 文件导入。
- Markdown 渲染和 HTML 安全过滤。
- slug 自动生成和唯一性处理。
- draft / published 状态控制。
- `/p/{slug}` 公开页面。
- 后台列表、新建、详情、编辑、删除确认和导入页面。
- H2 file 数据库、H2 test 数据库和 Flyway 迁移。
- 基础 Spring Security 表单登录接入。
- 统一错误响应和基础接口测试。

## 当前欠缺能力

- 真实 User 表。
- 注册 / 登录 API。
- BCrypt 密码加密。
- JWT。
- 用户数据隔离。
- MySQL 实跑验证。
- Redis、AOP、限流、异步、Docker、CI。

## 技术栈

- Java 21
- Spring Boot 3.5.14
- Maven
- Spring MVC
- Spring Data JPA
- Spring Security
- H2 / MySQL profile
- Flyway
- Bean Validation
- CommonMark 及扩展
- JUnit / MockMvc

默认本地启动使用 H2 file 数据库，数据保存在 `./data/chatpress`。测试使用 `test` profile 和 H2 内存数据库，避免污染本地数据。

MySQL profile 使用环境变量配置：

```text
MYSQL_URL=jdbc:mysql://localhost:3306/chatpress
MYSQL_USERNAME=root
MYSQL_PASSWORD=...
```

## 核心入口

```text
GET  /p/{slug}
GET  /admin/artifacts
POST /api/artifacts
POST /api/artifacts/import/markdown
```

完整接口见 [API 设计](docs/API.md)。

## 文档入口

- [产品定义](docs/PRODUCT.md)
- [数据模型](docs/DATA_MODEL.md)
- [API 设计](docs/API.md)

## 项目结构

```text
src/main/java/com/chatpress/v1/
  artifact/
  common/
  config/
  system/
src/main/resources/
  db/migration/
src/test/java/com/chatpress/v1/
docs/
```
