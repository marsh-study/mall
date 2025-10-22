# Mall Admin 前端项目

## 项目架构

```mermaid
flowchart TD
    %% 前端架构层（蓝色）
    subgraph 前端架构
        direction TB
        F1[表现层] --> F11[页面组件\n(商品详情页/订单列表页)]
        F1 --> F12[通用组件\n(按钮/表单/弹窗)]
        F2[路由层\n(Vue Router)]
        F3[状态管理层\n(Pinia)]
        F4[服务层\n(API交互-Axios)]
        F5[工具层\n(日期格式化/权限判断)]
        F1 --> F2
        F2 --> F3
        F3 --> F4
        F4 --> F5
    end

    %% 后端架构层（绿色）
    subgraph 后端架构
        direction TB
        B1[控制层\n(Controller)] --> B11[OmsOrderController\n(订单)]
        B1 --> B12[PmsProductController\n(商品)]
        B1 --> B13[UserController\n(用户)]
        B2[服务层\n(Service)] --> B21[OmsOrderServiceImpl]
        B2 --> B22[PmsProductServiceImpl]
        B3[数据访问层\n(DAO/MyBatis-Plus)]
        B4[实体层\n(Entity)] --> B41[OmsOrder/PmsSpu/UmsMember]
        B5[模型层\n(VO/DTO)] --> B51[OrderDetailVO/OrderRequestDTO]
        B6[配置层\n(SecurityConfig/数据库配置)]
        B7[工具层\n(加密/异常处理)]
        B1 --> B2
        B2 --> B3
        B3 --> B4
        B4 --> B5
        B6 --> B1
        B7 --> B2
    end

    %% 前后端交互
    F4 -->|API接口（HTTP/HTTPS）| B1
    style 前端架构 fill:#e6f7ff,stroke:#1890ff,stroke-width:2px
    style 后端架构 fill:#f0f9eb,stroke:#52c41a,stroke-width:2px
```

## 技术栈

- **前端框架**: Vue 3 + TypeScript
- **构建工具**: Vite
- **UI组件库**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router
- **HTTP客户端**: Axios

## 快速开始

```bash
# 安装依赖
pnpm install

# 启动开发服务器
pnpm run dev

# 构建生产版本
pnpm run build:prod
```

## 项目结构
