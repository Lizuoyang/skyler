<p align="center">
 <img src="https://img.shields.io/badge/Spring%20Cloud-2021-blue.svg" alt="Coverage Status">
 <img src="https://img.shields.io/badge/Spring%20Boot-2.7-blue.svg" alt="Downloads">
 <img src="https://img.shields.io/badge/Vue-3.2-blue.svg" alt="Downloads">
 <img src="https://img.shields.io/github/license/skyler-mesh/skyler"/>
</p>

## 系统说明

- 基于 Spring Cloud 2021 、Spring Boot 2.7、 OAuth2 的 RBAC **权限管理系统**
- 基于数据驱动视图的理念封装 element-plus，即使没有 vue 的使用经验也能快速上手
- 提供对常见容器化支持 Docker、Kubernetes、Rancher2 支持
- 提供 lambda 、stream api 、webflux 的生产实践

## 快速开始
### 核心依赖

| 依赖                          | 版本         |
|-----------------------------|------------|
| Spring Boot                 | 2.7.15     |
| Spring Cloud                | 2021.0.8   |
| Spring Cloud Alibaba        | 2021.0.5.0 |
| Spring Authorization Server | 0.4.3      |
| Mybatis Plus                | 3.5.3.1    |
| hutool                      | 5.8.1      |

### 模块说明

```lua
skyler-ui  -- https://gitee.com/lizuoyang/skyler-ui

skyler
├── skyler-auth -- 授权服务提供[3000]
└── skyler-common -- 系统公共模块
     ├── skyler-common-bom -- 全局依赖管理控制
     ├── skyler-common-core -- 公共工具类核心包
     ├── skyler-common-datapermission -- 数据权限
     ├── skyler-common-datasource -- 动态数据源包
     ├── skyler-common-job -- xxl-job 封装
     ├── skyler-common-log -- 日志服务
     ├── skyler-common-oss -- 文件上传工具类
     ├── skyler-common-mybatis -- mybatis 扩展封装
     ├── skyler-common-seata -- 分布式事务
     ├── skyler-common-security -- 安全工具类
     ├── skyler-common-swagger -- 接口文档
     ├── skyler-common-tenant -- 多租户
     ├── skyler-common-feign -- feign 扩展封装
     └── skyler-common-xss -- xss 安全封装
├── skyler-register -- Nacos Server[8848]
├── skyler-gateway -- Spring Cloud Gateway网关[9999]
└── skyler-upms -- 通用用户权限管理模块
     └── skyler-upms-api -- 通用用户权限管理系统公共api模块
     └── skyler-upms-biz -- 通用用户权限管理系统业务处理模块[4000]
└── skyler-visual
     └── skyler-monitor -- 服务监控 [5001]
     ├── skyler-codegen -- 图形化代码生成 [5002]
     └── skyler-quartz -- 定时任务管理台 [5007]
```


### Docker 运行

```
# 下载并运行服务端代码
git clone https://gitee.com/lizuoyang/skyler.git

cd skyler && mvn clean install && docker-compose up -d

# 下载并运行前端UI
git clone https://gitee.com/lizuoyang/skyler-ui

cd skyler-ui && npm install -g cnpm --registry=https://registry.npm.taobao.org


cnpm install && cnpm run build:docker && cd docker && docker-compose up -d
