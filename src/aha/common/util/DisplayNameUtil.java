package aha.common.util;

import java.util.ArrayList;
import java.util.List;

public final class DisplayNameUtil {
    private DisplayNameUtil() {}

    public static String toDisplayName(String id) {
        if (id == null || id.isBlank()) return "";

        String normalized = id.replace('.', ' ')
                              .replace('-', ' ')
                              .replace('_', ' ');

        String[] parts = normalized.split("\\s+");

        List<String> words = new ArrayList<>();
        for (String part : parts) {
            if (part.isEmpty()) continue;
            words.addAll(splitCaseSegments(part));
        }

        for (int i = 0; i < words.size(); i++) {
            words.set(i, capitalizeWord(words.get(i)));
        }

        return String.join(" ", words);
    }

    private static List<String> splitCaseSegments(String input) {
        List<String> segments = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            boolean isLast = i == chars.length - 1;
            char next = !isLast ? chars[i + 1] : 0;

            if (current.isEmpty()) {
                current.append(c);
            } else {
                char last = current.charAt(current.length() - 1);

                if (Character.isLowerCase(last) && Character.isUpperCase(c)) {
                    segments.add(current.toString());
                    current.setLength(0);
                    current.append(c);
                } else if (Character.isUpperCase(last) && Character.isUpperCase(c) && Character.isLowerCase(next)) {
                    segments.add(current.toString());
                    current.setLength(0);
                    current.append(c);
                } else if (Character.isLetter(last) && Character.isDigit(c)) {
                    segments.add(current.toString());
                    current.setLength(0);
                    current.append(c);
                } else if (Character.isDigit(last) && Character.isLetter(c)) {
                    segments.add(current.toString());
                    current.setLength(0);
                    current.append(c);
                } else {
                    current.append(c);
                }
            }
        }

        if (!current.isEmpty()) {
            segments.add(current.toString());
        }

        return segments;
    }

    private static String capitalizeWord(String word) {
        if (word.isEmpty()) return word;

        if (isAllCaps(word)) {
            return word.charAt(0) + word.substring(1).toLowerCase();
        }

        return word.charAt(0) + word.substring(1);
    }

    private static boolean isAllCaps(String s) {
        boolean hasLetter = false;
        for (char c : s.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
                if (!Character.isUpperCase(c)) {
                    return false;
                }
            }
        }
        return hasLetter;
    }
}
