package com.senither.library.scoreboard;

import java.util.HashMap;
import java.util.List;
import org.bukkit.ChatColor;

public class ScoreboardPage
{

    /**
     * Represents our scoreboard title.
     *
     * @var String
     */
    private String title = null;

    /**
     * A list of entries for the scoreboard to render, the key is the
     * scoreboard score(index) and the value is the string to render.
     *
     * @var HashMap
     */
    private HashMap<Integer, String> entries = new HashMap<>();

    /**
     * Represents the extra delay to add to the scoreboard update timer.
     *
     * @var Integer
     */
    private int extraDelay = 0;

    /**
     * Creates a new scoreboard page instance.
     *
     * @param title The title of the scoreboard page.
     */
    public ScoreboardPage(String title)
    {
        this.title = colorize(title);
    }

    /**
     * Creates a new scoreboard page instance.
     *
     * @param entries The map of entries to add to the scoreboard page.
     */
    public ScoreboardPage(HashMap<Integer, String> entries)
    {
        this.entries.putAll(colorize(entries));
    }

    /**
     * Creates a new scoreboard page instance.
     *
     * @param title   The title of the scoreboard page.
     * @param entries The map of entries to add to the scoreboard page.
     */
    public ScoreboardPage(String title, HashMap<Integer, String> entries)
    {
        this.title = colorize(title);
        this.entries.putAll(colorize(entries));
    }

    /**
     * Sets the given message to the given index.
     *
     * @param index   The index to write to.
     * @param message The message to write.
     * @return ScoreboardPage
     */
    public ScoreboardPage setEntry(int index, String message)
    {
        entries.put(index, colorize(message));

        return this;
    }

    /**
     * Sets the list of messages to the scoreboard page entries, the
     * indexs will be automatically generated from the list indexs.
     *
     * @param entries The list of entries to add.
     * @return ScoreboardPage
     */
    public ScoreboardPage setEntries(List<String> entries)
    {
        int size = entries.size();
        for (String entry : entries) {
            this.entries.put(size--, colorize(entry));
        }

        return this;
    }

    /**
     * Sets the map of messages to the scoreboard page entries.
     *
     * @param entries The map of entries to add.
     * @return ScoreboardPage
     */
    public ScoreboardPage setEntries(HashMap<Integer, String> entries)
    {
        this.entries = colorize(entries);

        return this;
    }

    /**
     * Returns the list of page entries.
     *
     * @return HashMap
     */
    public HashMap<Integer, String> getEntries()
    {
        return entries;
    }

    /**
     * Sets the scoreboard page title.
     *
     * @param title The title to use.
     * @return ScoreboardPage
     */
    public ScoreboardPage setTitle(String title)
    {
        this.title = colorize(title);

        return this;
    }

    /**
     * Returns the scoreboard page title.
     *
     * @return String
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Sets the extra update delay for the scoreboard page.
     *
     * @param ticks The
     * @return
     */
    public ScoreboardPage setExtraDelay(int ticks)
    {
        this.extraDelay = ticks;

        return this;
    }

    /**
     * Returns the additional update delay for the scoreboard page.
     *
     * @return Integer
     */
    public int getExtraDelay()
    {
        return extraDelay;
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

    /**
     * Colorize a map of messages, using Bukkit/Spigots and(&) standard symbol syntax.
     *
     * @param arr The map of messages to colorize.
     * @return HashMap
     */
    private HashMap<Integer, String> colorize(HashMap<Integer, String> arr)
    {
        arr.keySet().stream().forEach((index) -> {
            arr.put(index, colorize(arr.get(index)));
        });

        return arr;
    }
}
