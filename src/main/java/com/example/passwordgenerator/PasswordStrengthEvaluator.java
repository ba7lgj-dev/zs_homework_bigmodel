package com.example.passwordgenerator;

import java.util.HashSet;
import java.util.Set;

/**
 * Evaluates password strength using entropy estimates and character diversity.
 */
public class PasswordStrengthEvaluator {

    public StrengthLevel evaluate(String password, PasswordConfig config) {
        if (password == null || password.isEmpty()) {
            return StrengthLevel.WEAK;
        }

        double entropy = estimateEntropy(password, config);
        int diversityScore = countCharacterTypes(password);

        if (entropy >= 110 && diversityScore >= 4) {
            return StrengthLevel.VERY_STRONG;
        } else if (entropy >= 80 && diversityScore >= 3) {
            return StrengthLevel.STRONG;
        } else if (entropy >= 50 && diversityScore >= 2) {
            return StrengthLevel.MEDIUM;
        }
        return StrengthLevel.WEAK;
    }

    private double estimateEntropy(String password, PasswordConfig config) {
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

    private double log2(double value) {
        return Math.log(value) / Math.log(2);
    }
}
