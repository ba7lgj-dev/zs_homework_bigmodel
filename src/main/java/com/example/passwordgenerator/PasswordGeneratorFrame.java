package com.example.passwordgenerator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

/**
 * 面向最终用户的 Swing 密码生成器界面。
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
    private final JLabel strengthLabel = new JLabel("当前强度: -", SwingConstants.LEFT);
    private final JProgressBar strengthBar = new JProgressBar(0, 100);
    private final JLabel entropyLabel = new JLabel("-");
    private final JLabel crackTimeLabel = new JLabel("-");
    private final JTextArea adviceArea = createAdviceArea();

    public PasswordGeneratorFrame(PasswordGenerator generator, PasswordStrengthEvaluator evaluator) {
        super("密码生成器");
        this.generator = generator;
        this.evaluator = evaluator;

        initializeLookAndFeel();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // 更宽的默认窗口，便于容纳中文提示文本。
        setMinimumSize(new Dimension(720, 420));
        buildUi();
        setLocationRelativeTo(null);
    }

    private void initializeLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // 回退到默认外观即可，不影响主要功能。
        }
    }

    private JTextArea createAdviceArea() {
        JTextArea area = new JTextArea("生成密码后将在此给出优化建议。");
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(false);
        area.setFocusable(false);
        area.setBorder(BorderFactory.createEmptyBorder());
        return area;
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
        // 使用滚动面板避免输入较长字符时界面被撑开。
        JScrollPane customScrollPane = new JScrollPane(customArea);
        customScrollPane.setPreferredSize(new Dimension(220, 60));
        panel.add(customScrollPane, gbc);

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
        panel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        panel.add(buildPasswordRow(), BorderLayout.NORTH);
        panel.add(buildAnalysisPanel(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildPasswordRow() {
        JPanel row = new JPanel(new BorderLayout(8, 8));
        passwordField.setEditable(false);
        row.add(passwordField, BorderLayout.CENTER);

        JButton copyButton = new JButton("复制");
        copyButton.addActionListener(e -> copyPassword());
        row.add(copyButton, BorderLayout.EAST);
        return row;
    }

    private JPanel buildAnalysisPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        panel.add(strengthLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        strengthBar.setStringPainted(true);
        strengthBar.setString("等待生成");
        strengthBar.setValue(0);
        panel.add(strengthBar, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("熵估计:"), gbc);
        gbc.gridx = 1;
        panel.add(entropyLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("估计破解时间:"), gbc);
        gbc.gridx = 1;
        panel.add(crackTimeLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(new JLabel("优化建议:"), gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        JScrollPane adviceScroll = new JScrollPane(adviceArea);
        adviceScroll.setBorder(BorderFactory.createEmptyBorder());
        adviceScroll.setPreferredSize(new Dimension(200, 80));
        panel.add(adviceScroll, gbc);

        return panel;
    }

    private void generatePassword() {
        // 根据表单选项构建密码生成配置。
        PasswordConfig config = PasswordConfig.builder()
            .length((Integer) lengthSpinner.getValue())
            .includeUppercase(uppercaseBox.isSelected())
            .includeLowercase(lowercaseBox.isSelected())
            .includeDigits(digitsBox.isSelected())
            .includeSpecial(specialBox.isSelected())
            .customCharacters(customArea.getText().trim())
            .build();

        try {
            // 同步生成密码并评估强度。
            String password = generator.generate(config);
            passwordField.setText(password);
            StrengthReport report = evaluator.analyze(password, config);
            updateStrengthIndicators(report);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "配置错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStrengthIndicators(StrengthReport report) {
        StrengthLevel level = report.level();
        strengthLabel.setText("当前强度: " + level.getDisplayName());
        strengthBar.setValue(level.getProgressValue());
        strengthBar.setString(level.getDisplayName());
        updateStrengthBarColor(level);
        entropyLabel.setText(report.formattedEntropy());
        crackTimeLabel.setText(report.formattedCrackTime());
        adviceArea.setText(report.normalizedSuggestion());
        adviceArea.setCaretPosition(0);
    }

    private void updateStrengthBarColor(StrengthLevel level) {
        Color color;
        switch (level) {
            case VERY_STRONG -> color = new Color(0x2E7D32); // 深绿色
            case STRONG -> color = new Color(0x388E3C);
            case MEDIUM -> color = new Color(0xF9A825); // 琥珀色
            default -> color = new Color(0xE53935); // 红色
        }
        strengthBar.setForeground(color);
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
