package com.example.passwordgenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Holds the configuration parameters for password generation.
 */
public final class PasswordConfig {
    public static final int MIN_LENGTH = 8;
    public static final int MAX_LENGTH = 128;
    public static final int DEFAULT_LENGTH = 16;

    private final int length;
    private final boolean includeUppercase;
    private final boolean includeLowercase;
    private final boolean includeDigits;
    private final boolean includeSpecial;
    private final String customCharacters;

    private PasswordConfig(Builder builder) {
        this.length = builder.length;
        this.includeUppercase = builder.includeUppercase;
        this.includeLowercase = builder.includeLowercase;
        this.includeDigits = builder.includeDigits;
        this.includeSpecial = builder.includeSpecial;
        this.customCharacters = builder.customCharacters == null ? "" : builder.customCharacters;
    }

    public int getLength() {
        return length;
    }

    public boolean isIncludeUppercase() {
        return includeUppercase;
    }

    public boolean isIncludeLowercase() {
        return includeLowercase;
    }

    public boolean isIncludeDigits() {
        return includeDigits;
    }

    public boolean isIncludeSpecial() {
        return includeSpecial;
    }

    public String getCustomCharacters() {
        return customCharacters;
    }

    /**
     * Returns the active character sets to be used by the generator.
     */
    public List<String> getActiveCharacterSets() {
        List<String> sets = new ArrayList<>();
        if (includeUppercase) {
            sets.add(CharacterSets.UPPERCASE);
        }
        if (includeLowercase) {
            sets.add(CharacterSets.LOWERCASE);
        }
        if (includeDigits) {
            sets.add(CharacterSets.DIGITS);
        }
        if (includeSpecial) {
            sets.add(CharacterSets.SPECIAL);
        }
        if (!customCharacters.isEmpty()) {
            sets.add(customCharacters);
        }
        return sets;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private int length = DEFAULT_LENGTH;
        private boolean includeUppercase = true;
        private boolean includeLowercase = true;
        private boolean includeDigits = true;
        private boolean includeSpecial = false;
        private String customCharacters = "";

        private Builder() {
        }

        public Builder length(int length) {
            this.length = length;
            return this;
        }

        public Builder includeUppercase(boolean includeUppercase) {
            this.includeUppercase = includeUppercase;
            return this;
        }

        public Builder includeLowercase(boolean includeLowercase) {
            this.includeLowercase = includeLowercase;
            return this;
        }

        public Builder includeDigits(boolean includeDigits) {
            this.includeDigits = includeDigits;
            return this;
        }

        public Builder includeSpecial(boolean includeSpecial) {
            this.includeSpecial = includeSpecial;
            return this;
        }

        public Builder customCharacters(String customCharacters) {
            this.customCharacters = Objects.requireNonNullElse(customCharacters, "");
            return this;
        }

        public PasswordConfig build() {
            return new PasswordConfig(this);
        }
    }
}
