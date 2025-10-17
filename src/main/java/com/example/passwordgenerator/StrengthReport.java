package com.example.passwordgenerator;

/**
 * 汇总密码强度分析结果，用于在 UI 中展示详细指标与建议。
 */
public record StrengthReport(
    StrengthLevel level,
    double entropyBits,
    double estimatedCrackTimeSeconds,
    String suggestion
) {

    public String formattedEntropy() {
        return String.format("%.1f 位", entropyBits);
    }

    public String formattedCrackTime() {
        if (estimatedCrackTimeSeconds <= 1) {
            return "< 1 秒";
        }
        double seconds = estimatedCrackTimeSeconds;
        double minutes = seconds / 60.0;
        double hours = minutes / 60.0;
        double days = hours / 24.0;
        double years = days / 365.25;

        if (years >= 1_000_000) {
            return "> 100 万年";
        } else if (years >= 1) {
            return String.format("约 %.1f 年", years);
        } else if (days >= 1) {
            return String.format("约 %.1f 天", days);
        } else if (hours >= 1) {
            return String.format("约 %.1f 小时", hours);
        } else if (minutes >= 1) {
            return String.format("约 %.1f 分钟", minutes);
        }
        return String.format("约 %.0f 秒", seconds);
    }

    public String normalizedSuggestion() {
        return (suggestion == null || suggestion.isBlank())
            ? "密码已经相当稳固，可按需保存。"
            : suggestion;
    }
}
