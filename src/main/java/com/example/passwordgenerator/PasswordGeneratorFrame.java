package com.example.passwordgenerator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

/**
 * Swing based UI for the password generator.
 */
public class PasswordGeneratorFrame extends JFrame {

    private final PasswordGenerator generator;
    private final PasswordStrengthEvaluator evaluator;

    private final JSpinner lengthSpinner = new JSpinner(new SpinnerNumberModel(PasswordConfig.DEFAULT_LENGTH,
        PasswordConfig.MIN_LENGTH,
        PasswordConfig.MAX_LENGTH,
        1));
    private final JCheckBox uppercaseBox = new JCheckBox("包含大写字母", true);
    private final JCheckBox lowercaseBox = new JCheckBox("包含小写字母", true);
    private final JCheckBox digitsBox = new JCheckBox("包含数字", true);
    private final JCheckBox specialBox = new JCheckBox("包含特殊字符", false);
    private final JTextArea customArea = new JTextArea(2, 20);
    private final JTextField passwordField = new JTextField();
    private final JLabel strengthLabel = new JLabel("强度: -", SwingConstants.LEFT);

    public PasswordGeneratorFrame(PasswordGenerator generator, PasswordStrengthEvaluator evaluator) {
        super("密码生成器");
        this.generator = generator;
        this.evaluator = evaluator;

        initializeLookAndFeel();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(480, 320));
        buildUi();
        setLocationRelativeTo(null);
    }

    private void initializeLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Fallback to default look and feel.
        }
    }

    private void buildUi() {
        JPanel content = new JPanel(new BorderLayout(8, 8));
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        content.add(buildConfigurationPanel(), BorderLayout.CENTER);
        content.add(buildOutputPanel(), BorderLayout.SOUTH);
        setContentPane(content);
    }

    private JPanel buildConfigurationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        panel.add(new JLabel("密码长度:"), gbc);
        gbc.gridx = 1;
        panel.add(lengthSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("字符集选项:"), gbc);

        JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        checkboxPanel.add(uppercaseBox);
        checkboxPanel.add(lowercaseBox);
        checkboxPanel.add(digitsBox);
        checkboxPanel.add(specialBox);

        gbc.gridx = 1;
        panel.add(checkboxPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        panel.add(new JLabel("自定义字符:"), gbc);

        gbc.gridx = 1;
        customArea.setLineWrap(true);
        customArea.setWrapStyleWord(true);
        panel.add(customArea, gbc);

        JButton generateButton = new JButton("生成密码");
        generateButton.addActionListener(e -> generatePassword());

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(generateButton, gbc);

        return panel;
    }

    private JPanel buildOutputPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        passwordField.setEditable(false);
        panel.add(passwordField, BorderLayout.CENTER);
        panel.add(strengthLabel, BorderLayout.WEST);

        JButton copyButton = new JButton("复制");
        copyButton.addActionListener(e -> copyPassword());

        panel.add(copyButton, BorderLayout.EAST);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        return panel;
    }

    private void generatePassword() {
        PasswordConfig config = PasswordConfig.builder()
            .length((Integer) lengthSpinner.getValue())
            .includeUppercase(uppercaseBox.isSelected())
            .includeLowercase(lowercaseBox.isSelected())
            .includeDigits(digitsBox.isSelected())
            .includeSpecial(specialBox.isSelected())
            .customCharacters(customArea.getText().trim())
            .build();

        try {
            String password = generator.generate(config);
            passwordField.setText(password);
            StrengthLevel level = evaluator.evaluate(password, config);
            strengthLabel.setText("强度: " + level.getDisplayName());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "配置错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void copyPassword() {
        String password = passwordField.getText();
        if (password == null || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请先生成密码。", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(password), null);
        JOptionPane.showMessageDialog(this, "密码已复制到剪贴板。", "成功", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void launch() {
        SwingUtilities.invokeLater(() -> {
            PasswordGenerator generator = new PasswordGenerator();
            PasswordStrengthEvaluator evaluator = new PasswordStrengthEvaluator();
            PasswordGeneratorFrame frame = new PasswordGeneratorFrame(generator, evaluator);
            frame.setVisible(true);
        });
    }
}
