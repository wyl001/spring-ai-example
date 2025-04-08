# Spring AI 教学课程项目

本项目是基于 [Spring AI](https://spring.io/projects/spring-ai) 的教学示例，旨在帮助开发者快速上手 Spring AI 的使用，掌握其与 LLM（大语言模型）、Prompt Engineering、Embedding 及向量数据库集成的核心能力。

## 📚 课程简介

本课程内容包括：

- Spring AI 简介与架构解析
- 与 OpenAI、Azure OpenAI、Qwen 等 LLM 的集成
- Prompt 设计与测试（Prompt Engineering）
- 向量化与向量数据库（如 Milvus、Qdrant、PGVector）集成
- 基于知识库的问答（RAG）
- 自定义 Prompt Template 与 Chain
- 实战案例讲解（例如：企业知识问答系统）

> 本教学项目适用于具备一定 Java/Spring Boot 基础的开发者。

---



## 🚀 快速开始

### 环境准备

- JDK 17+
- Spring Boot 3.3.10
- Maven 3.8+
- 向量数据库（可选：Redis、Milvus、Qdrant、PostgreSQL+PGVector）
- 可选 LLM 服务：OpenAI、ZhiPu AI、通义千问（Qwen）等

### 配置方式

编辑 `application.yml`，根据你所使用的 LLM 和向量数据库填写 API 密钥和连接信息：

```yaml
spring:
  ai:
    openai:
      base-url: https://api.deepseek.com
      api-key: ${api.key.openai}
      embedding:
        enabled: false
      chat:
        enabled: false
        options:
          model: deepseek-chat
    zhipuai:
      chat:
        enabled: true
        options:
          model: glm-4-flash
      api-key: ${api.key.zhipuai}
    vectorstore:
      redis:
        initialize-schema: true
        index-name: custom-index
        prefix: custom-prefix
        batching-strategy: TOKEN_COUNT
  data:
    redis:
      url: redis://用户名:密码@地址:端口号


