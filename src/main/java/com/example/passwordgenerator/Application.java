package com.example.passwordgenerator;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import javax.swing.SwingUtilities;
import java.util.concurrent.Callable;

@Command(name = "password-generator",
    mixinStandardHelpOptions = true,
    version = "1.0.0",
    description = "根据配置选项生成安全密码")
public class Application implements Callable<Integer> {

    @Option(names = {"-l", "--length"}, description = "密码长度（默认: ${DEFAULT-VALUE}）")
    private int length = PasswordConfig.DEFAULT_LENGTH;

    @Option(names = {"-U", "--no-uppercase"}, description = "排除大写字母")
    private boolean excludeUppercase;

    @Option(names = {"-L", "--no-lowercase"}, description = "排除小写字母")
    private boolean excludeLowercase;

    @Option(names = {"-D", "--no-digits"}, description = "排除数字")
    private boolean excludeDigits;

    @Option(names = {"-S", "--include-special"}, description = "包含特殊字符")
    private boolean includeSpecial;

    @Option(names = {"-C", "--custom"}, description = "额外自定义字符")
    private String customCharacters = "";

    @Option(names = {"--ui"}, description = "启动图形界面")
    private boolean launchUi;

    private final PasswordGenerator generator;
    private final PasswordStrengthEvaluator evaluator;

    public Application() {
        this.generator = new PasswordGenerator();
        this.evaluator = new PasswordStrengthEvaluator();
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Application()).execute(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    @Override
    public Integer call() {
        if (launchUi) {
            SwingUtilities.invokeLater(() -> {
                PasswordGeneratorFrame frame = new PasswordGeneratorFrame(generator, evaluator);
                frame.setVisible(true);
            });
            return 0;
        }

        PasswordConfig config = PasswordConfig.builder()
            .length(length)
            .includeUppercase(!excludeUppercase)
            .includeLowercase(!excludeLowercase)
            .includeDigits(!excludeDigits)
            .includeSpecial(includeSpecial)
            .customCharacters(customCharacters)
            .build();

        try {
            String password = generator.generate(config);
            StrengthLevel level = evaluator.evaluate(password, config);
            System.out.println("生成的密码: " + password);
            System.out.println("密码强度: " + level.getDisplayName());
            return 0;
        } catch (IllegalArgumentException ex) {
            System.err.println("配置错误: " + ex.getMessage());
            return 1;
        }
    }
}
