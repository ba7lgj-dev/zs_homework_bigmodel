package com.example.passwordgenerator;

/**
 * 应用入口：直接启动图形化界面，不再提供命令行模式。
 */
public final class Application {

    private Application() {
        // 工具类，禁止实例化。
    }

    public static void main(String[] args) {
        PasswordGeneratorFrame.launch();
    }
}
