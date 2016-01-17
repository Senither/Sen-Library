package com.senither.library.chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.commons.lang.Validate;

public class ChatFilter
{

    private final Random random;
    private final char[] characters;
    private final char[] placehodlers;
    private final Map<String, WordPattern> patterns;

    private int capsPercentage = 75;

    public ChatFilter()
    {
        random = new Random();
        patterns = new HashMap<>();

        characters = "!\"#%&/()=?´`¨^'*-_.:,;@£${[]}\\|".toCharArray();
        placehodlers = "!#%&?=+@$".toCharArray();
    }

    public void setCapsPercentage(int capsPercentage)
    {
        this.capsPercentage = capsPercentage;
    }

    public boolean addWord(String word)
    {
        Validate.notNull(word, "You can't add null to the words filter");

        word = word.toLowerCase();

        if (patterns.containsKey(word)) {
            return false;
        }

        patterns.put(word, new WordPattern(word));

        return true;
    }

    public void addWords(String[] words)
    {
        for (String word : words) {
            addWord(word);
        }
    }

    public void addWords(List<String> words)
    {
        words.stream().forEach((word) -> {
            addWord(word);
        });
    }

    public String runFilter(ChatFilterType type, String str)
    {
        Validate.notNull(type, "The chat filter type can not be null");
        Validate.notNull(str, "The filter message can not be null");

        switch (type) {
            case CAPS_FILTER:
                str = runCapsFilter(str);
                break;

            case WORD_FILTER:
                str = runWordFilter(str);
                break;

            case ALL:
                str = runCapsFilter(str);
                str = runWordFilter(str);
                break;
        }

        return str;
    }

    public String runWordFilter(String str)
    {
        Validate.notNull(str, "The filter message can not be null");

        for (WordPattern wp : patterns.values()) {
            try {
                // This doesn't always work, gotta find a replacement 
                str = wp.getPattern().matcher(str).replaceAll(generateGibberish(wp.getLength()));
            } catch (IllegalArgumentException ex) {
            }
        }

        return str;
    }

    public String runCapsFilter(String str)
    {
        if (capsPercentage < 0) {
            return str;
        }

        Validate.notNull(str, "The filter message can not be null");

        char[] array = str.toCharArray();
        int count = 0, size = array.length;

        for (char character : array) {
            if (Character.isUpperCase(character)) {
                count++;
            } else if (isSpecialCharacter(character)) {
                size--;
            }
        }
        if (count == 0 && size == 0) {
            return str;
        }

        float precent = (count * 100) / size;

        if (precent > capsPercentage) {
            str = str.substring(0, 1).toUpperCase() + str.substring(1, str.length()).toLowerCase();
        }

        return str;
    }

    private boolean isSpecialCharacter(char character)
    {
        for (char c : characters) {
            if (c == character) {
                return true;
            }
        }

        return false;
    }

    private String generateGibberish(int length)
    {
        String word = "";
        for (int i = 0; i < length; i++) {
            word += placehodlers[random.nextInt(placehodlers.length)];
        }

        return word;
    }
}
