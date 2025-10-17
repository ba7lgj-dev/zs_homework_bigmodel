package com.example.passwordgenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Evaluates password strength using entropy estimates and character diversity.
 */
public class PasswordStrengthEvaluator {

    private static final double BASE_GUESSES_PER_SECOND = 1e10; // GPU 集群的常见暴力破解速度。

    public StrengthLevel evaluate(String password, PasswordConfig config) {
        return analyze(password, config).level();
    }

    public StrengthReport analyze(String password, PasswordConfig config) {
        String nonNullPassword = password == null ? "" : password;
        double entropy = estimateEntropy(nonNullPassword, config);
        int diversityScore = countCharacterTypes(nonNullPassword);
        StrengthLevel level = classify(entropy, diversityScore, nonNullPassword.length());
        double crackTimeSeconds = estimateCrackTime(entropy);
        String suggestion = buildSuggestion(nonNullPassword, config, diversityScore, level);
        return new StrengthReport(level, entropy, crackTimeSeconds, suggestion);
    }

    private StrengthLevel classify(double entropy, int diversityScore, int length) {
        if (entropy >= 110 && diversityScore >= 4 && length >= 20) {
            return StrengthLevel.VERY_STRONG;
        } else if (entropy >= 80 && diversityScore >= 3 && length >= 16) {
            return StrengthLevel.STRONG;
        } else if (entropy >= 50 && diversityScore >= 2 && length >= 12) {
            return StrengthLevel.MEDIUM;
        }
        return StrengthLevel.WEAK;
    }

    private double estimateEntropy(String password, PasswordConfig config) {
        if (password.isEmpty()) {
            return 0.0;
        }
        int poolSize = Math.max(estimateCharacterPool(config), 1);
        return password.length() * log2(poolSize);
    }

    private int estimateCharacterPool(PasswordConfig config) {
        return (int) config.getActiveCharacterSets().stream()
            .flatMapToInt(String::chars)
            .distinct()
            .count();
    }

    private int countCharacterTypes(String password) {
        Set<String> present = new HashSet<>();
        for (char c : password.toCharArray()) {
            if (CharacterSets.UPPERCASE.indexOf(c) >= 0) {
                present.add("U");
            } else if (CharacterSets.LOWERCASE.indexOf(c) >= 0) {
                present.add("L");
            } else if (CharacterSets.DIGITS.indexOf(c) >= 0) {
                present.add("D");
            } else if (CharacterSets.SPECIAL.indexOf(c) >= 0) {
                present.add("S");
            } else {
                present.add("C");
            }
        }
        return present.size();
    }

    private String buildSuggestion(String password, PasswordConfig config, int diversityScore, StrengthLevel level) {
        List<String> tips = new ArrayList<>();
        if (password.length() < 16) {
            tips.add("尝试将密码长度提高到 16 位或以上");
        }
        if (diversityScore < 4) {
            tips.add("包含更多不同类型的字符（大写、小写、数字、特殊符号）");
        }
        if (!config.isIncludeSpecial() && config.getCustomCharacters().isEmpty()) {
            tips.add("加入一些特殊符号或自定义字符以增加复杂度");
        }
        if (level == StrengthLevel.WEAK && password.length() < 12) {
            tips.add("不要复用旧密码，并避免使用常见词汇");
        }
        if (tips.isEmpty() && level == StrengthLevel.VERY_STRONG) {
            tips.add("密码已经非常强，可考虑使用密码管理器保存");
        }
        return String.join("；", tips);
    }

    private double estimateCrackTime(double entropyBits) {
        double totalGuesses = Math.pow(2, entropyBits - 1); // 平均情况下需要尝试一半的可能性。
        double seconds = totalGuesses / BASE_GUESSES_PER_SECOND;
        if (!Double.isFinite(seconds) || seconds <= 0) {
            return Double.POSITIVE_INFINITY;
        }
        return seconds;
    }

    private double log2(double value) {
        return Math.log(value) / Math.log(2);
    }
}
