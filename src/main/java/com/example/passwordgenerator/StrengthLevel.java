package com.example.passwordgenerator;

/**
 * Describes qualitative password strength levels.
 */
public enum StrengthLevel {
    WEAK("弱"),
    MEDIUM("中"),
    STRONG("强"),
    VERY_STRONG("非常强");

    private final String displayName;

    StrengthLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
