package com.senither.library.scoreboard;

import java.util.HashMap;
import java.util.List;
import org.bukkit.ChatColor;

public class ScoreboardPage
{

    private String title = null;
    private HashMap<Integer, String> entries = new HashMap<>();
    private int extraDelay = 0;

    public ScoreboardPage()
    {
        //
    }

    public ScoreboardPage(String title)
    {
        this.title = colorize(title);
    }

    public ScoreboardPage(HashMap<Integer, String> entrys)
    {
        this.entries.putAll(colorize(entrys));
    }

    public ScoreboardPage(String title, HashMap<Integer, String> entrys)
    {
        this.title = colorize(title);
        this.entries.putAll(colorize(entrys));
    }

    public HashMap<Integer, String> getEntries()
    {
        return entries;
    }

    public String getTitle()
    {
        return title;
    }

    public int getExtraDelay()
    {
        return extraDelay;
    }

    public ScoreboardPage setExtraDelay(int ticks)
    {
        this.extraDelay = ticks;

        return this;
    }

    public ScoreboardPage setEntries(HashMap<Integer, String> entries)
    {
        this.entries = colorize(entries);

        return this;
    }

    public ScoreboardPage setEntries(List<String> entries)
    {
        int size = entries.size();
        for (String entry : entries) {
            this.entries.put(size--, colorize(entry));
        }

        return this;
    }

    public ScoreboardPage setEntry(int index, String message)
    {
        this.entries.put(index, colorize(message));

        return this;
    }

    public ScoreboardPage setTitle(String title)
    {
        this.title = colorize(title);

        return this;
    }

    private String colorize(String str)
    {
        if (str == null) {
            return null;
        }

        return ChatColor.translateAlternateColorCodes('&', str);
    }

    private HashMap<Integer, String> colorize(HashMap<Integer, String> arr)
    {
        arr.keySet().stream().forEach((index) -> {
            arr.put(index, colorize(arr.get(index)));
        });

        return arr;
    }
}
