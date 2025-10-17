# Password Generator

该项目基于 Java 17 和 Maven 实现，包含命令行工具与 Swing 图形界面两种交互方式，可按需求文档生成安全的随机密码并评估密码强度。

## 快速开始

```bash
mvn clean package
```

### 命令行模式

```bash
mvn -q exec:java -Dexec.args="--length 24 --include-special --custom '€£¥'"
```

常用参数：

- `--length`：密码长度，范围 8-128，默认 16。
- `--no-uppercase`、`--no-lowercase`、`--no-digits`：排除对应字符集。
- `--include-special`：包含常用特殊字符。
- `--custom`：额外追加自定义字符集。
- `--ui`：启动 Swing 图形界面。

### 启动图形界面

```bash
mvn -q exec:java -Dexec.args="--ui"
```

## 测试

```bash
mvn test
```
