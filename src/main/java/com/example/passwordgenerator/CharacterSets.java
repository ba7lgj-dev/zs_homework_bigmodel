package com.example.passwordgenerator;

/**
 * Defines reusable character sets for password generation.
 */
public final class CharacterSets {
    private CharacterSets() {
    }

    public static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    public static final String DIGITS = "0123456789";
    public static final String SPECIAL = "!@#$%^&*()-_=+[]{}|;:,.<>?/";
}
