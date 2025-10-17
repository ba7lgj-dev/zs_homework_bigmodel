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
                "Password length must be between " + PasswordConfig.MIN_LENGTH + " and " + PasswordConfig.MAX_LENGTH + ".");
        }

        List<String> activeSets = config.getActiveCharacterSets();
        if (activeSets.size() < 3) {
            throw new IllegalArgumentException("At least three character types must be selected.");
        }

        if (activeSets.stream().anyMatch(String::isBlank)) {
            throw new IllegalArgumentException("Character sets cannot be blank.");
        }

        int uniqueChars = combinedUniqueCharacters(activeSets);
        if (uniqueChars < 10) {
            throw new IllegalArgumentException("Character pool is too small for secure generation.");
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
