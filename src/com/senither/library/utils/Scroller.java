package com.senither.library.utils;

import org.bukkit.ChatColor;

public class Scroller
{

    private final String line;

    private int index = 0;
    private int length;

    public Scroller(String line)
    {
        this.line = line;
        this.length = 22;
    }

    public Scroller(String line, int length)
    {
        this.line = line;
        this.length = length;
    }

    public void setLength(int length)
    {
        this.length = length;
    }

    public boolean hasNext()
    {
        return line.length() >= index;
    }

    public String next()
    {
        if (!hasNext()) {
            index = 0;
        }

        if (line.length() <= length) {
            return colorize(line);
        }

        String text;

        try {
            text = line.substring(index, length + index);
        } catch (StringIndexOutOfBoundsException e) {
            text = line.substring(index, line.length());

            int subIndex = length - line.length() + index - 1;
            if (subIndex > 0) {
                text += " " + line.substring(0, subIndex);
            }
        }

        index++;

        return colorize(text);
    }

    @Override
    public String toString()
    {
        return next();
    }

    /**
     * Colorize a message, using Bukkit/Spigots standard and(&) symbol syntax.
     *
     * @param str The message the colorize.
     * @return String
     */
    private String colorize(String str)
    {
        if (str == null) {
            return null;
        }

        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
