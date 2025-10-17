package com.example.passwordgenerator;

/**
 * Describes qualitative password strength levels.
 */
public enum StrengthLevel {
    WEAK("弱", 15),
    MEDIUM("中", 45),
    STRONG("强", 75),
    VERY_STRONG("非常强", 100);

    private final String displayName;
    private final int progressValue;

    StrengthLevel(String displayName, int progressValue) {
        this.displayName = displayName;
        this.progressValue = progressValue;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getProgressValue() {
        return progressValue;
    }
}
