package com.example.passwordgenerator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordGeneratorTest {

    private final PasswordGenerator generator = new PasswordGenerator();
    private final PasswordStrengthEvaluator evaluator = new PasswordStrengthEvaluator();

    @Test
    void generatesPasswordOfRequestedLength() {
        PasswordConfig config = PasswordConfig.builder()
            .length(20)
            .includeUppercase(true)
            .includeLowercase(true)
            .includeDigits(true)
            .includeSpecial(true)
            .build();

        String password = generator.generate(config);
        assertEquals(20, password.length());
    }

    @Test
    void passwordContainsSelectedCharacterTypes() {
        PasswordConfig config = PasswordConfig.builder()
            .length(16)
            .includeUppercase(true)
            .includeLowercase(true)
            .includeDigits(true)
            .includeSpecial(true)
            .build();

        String password = generator.generate(config);
        assertTrue(password.chars().anyMatch(c -> CharacterSets.UPPERCASE.indexOf(c) >= 0));
        assertTrue(password.chars().anyMatch(c -> CharacterSets.LOWERCASE.indexOf(c) >= 0));
        assertTrue(password.chars().anyMatch(c -> CharacterSets.DIGITS.indexOf(c) >= 0));
        assertTrue(password.chars().anyMatch(c -> CharacterSets.SPECIAL.indexOf(c) >= 0));
    }

    @Test
    void rejectsInvalidConfiguration() {
        PasswordConfig config = PasswordConfig.builder()
            .length(4)
            .includeUppercase(true)
            .includeLowercase(true)
            .includeDigits(false)
            .includeSpecial(false)
            .build();

        assertThrows(IllegalArgumentException.class, () -> generator.generate(config));
    }

    @Test
    void evaluatorRecognizesStrongPasswords() {
        PasswordConfig config = PasswordConfig.builder()
            .length(32)
            .includeUppercase(true)
            .includeLowercase(true)
            .includeDigits(true)
            .includeSpecial(true)
            .build();

        String password = generator.generate(config);
        StrengthLevel level = evaluator.evaluate(password, config);
        assertTrue(level == StrengthLevel.STRONG || level == StrengthLevel.VERY_STRONG);
    }

    @Test
    void analyzerProvidesDetailedFeedback() {
        PasswordConfig config = PasswordConfig.builder()
            .length(18)
            .includeUppercase(true)
            .includeLowercase(true)
            .includeDigits(true)
            .includeSpecial(true)
            .build();

        String password = generator.generate(config);
        StrengthReport report = evaluator.analyze(password, config);
        assertTrue(report.entropyBits() > 0);
        assertNotNull(report.formattedCrackTime());
        assertNotNull(report.normalizedSuggestion());
    }
}
