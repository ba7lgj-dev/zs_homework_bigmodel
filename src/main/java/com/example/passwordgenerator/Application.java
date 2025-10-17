package com.example.passwordgenerator;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import javax.swing.SwingUtilities;
import java.util.concurrent.Callable;

@Command(name = "password-generator",
    mixinStandardHelpOptions = true,
    version = "1.0.0",
    description = "Generates secure passwords based on configurable options.")
public class Application implements Callable<Integer> {

    @Option(names = {"-l", "--length"}, description = "Password length (default: ${DEFAULT-VALUE})")
    private int length = PasswordConfig.DEFAULT_LENGTH;

    @Option(names = {"-U", "--no-uppercase"}, description = "Exclude uppercase characters")
    private boolean excludeUppercase;

    @Option(names = {"-L", "--no-lowercase"}, description = "Exclude lowercase characters")
    private boolean excludeLowercase;

    @Option(names = {"-D", "--no-digits"}, description = "Exclude digits")
    private boolean excludeDigits;

    @Option(names = {"-S", "--include-special"}, description = "Include special characters")
    private boolean includeSpecial;

    @Option(names = {"-C", "--custom"}, description = "Additional custom characters to include")
    private String customCharacters = "";

    @Option(names = {"--ui"}, description = "Launch the Swing user interface")
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
            System.out.println("Generated password: " + password);
            System.out.println("Strength: " + level.getDisplayName());
            return 0;
        } catch (IllegalArgumentException ex) {
            System.err.println("Configuration error: " + ex.getMessage());
            return 1;
        }
    }
}
