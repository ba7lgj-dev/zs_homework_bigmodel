package com.example.passwordgenerator;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generates passwords using a cryptographically secure random number generator.
 */
public class PasswordGenerator {

    private final SecureRandom random;

    public PasswordGenerator() {
        this(new SecureRandom());
    }

    PasswordGenerator(SecureRandom random) {
        this.random = random;
    }

    public String generate(PasswordConfig config) {
        validate(config);

        List<String> activeSets = config.getActiveCharacterSets();
        List<Character> characters = new ArrayList<>(config.getLength());

        // Ensure each selected character type is represented at least once.
        for (String set : activeSets) {
            characters.add(pickRandomCharacter(set));
        }

        String combined = combineSets(activeSets);
        int remaining = config.getLength() - characters.size();
        for (int i = 0; i < remaining; i++) {
            characters.add(pickRandomCharacter(combined));
        }

        // Shuffle to avoid predictable placement of required characters.
        Collections.shuffle(characters, random);

        char[] password = new char[characters.size()];
        for (int i = 0; i < characters.size(); i++) {
            password[i] = characters.get(i);
        }
        return new String(password);
    }

    private void validate(PasswordConfig config) {
        int length = config.getLength();
        if (length < PasswordConfig.MIN_LENGTH || length > PasswordConfig.MAX_LENGTH) {
            throw new IllegalArgumentException(
                "密码长度必须在 " + PasswordConfig.MIN_LENGTH + " 到 " + PasswordConfig.MAX_LENGTH + " 之间。");
        }

        List<String> activeSets = config.getActiveCharacterSets();
        if (activeSets.size() < 3) {
            throw new IllegalArgumentException("至少需要选择三种字符类型。");
        }

        if (activeSets.stream().anyMatch(String::isBlank)) {
            throw new IllegalArgumentException("字符集不能为空。");
        }

        int uniqueChars = combinedUniqueCharacters(activeSets);
        if (uniqueChars < 10) {
            throw new IllegalArgumentException("可用字符池过小，无法生成安全密码。");
        }
    }

    private int combinedUniqueCharacters(List<String> activeSets) {
        return (int) activeSets.stream()
            .flatMapToInt(String::chars)
            .distinct()
            .count();
    }

    private char pickRandomCharacter(String set) {
        int index = random.nextInt(set.length());
        return set.charAt(index);
    }

    private String combineSets(List<String> sets) {
        StringBuilder builder = new StringBuilder();
        for (String set : sets) {
            builder.append(set);
        }
        return builder.toString();
    }
}
