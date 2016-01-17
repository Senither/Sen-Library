package com.senither.library.chat;

import java.util.regex.Pattern;

public class WordPattern
{

    private final int length;

    private final Pattern pattern;

    public WordPattern(String word)
    {
        length = word.length();

        StringBuilder sb = new StringBuilder();
        for (Character character : word.toCharArray()) {
            sb.append(character).append("+[\\.\\|\\-_!]*");
        }

        pattern = Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
    }

    public int getLength()
    {
        return length;
    }

    public Pattern getPattern()
    {
        return pattern;
    }
}
