package com.senither.library.scoreboard;

import com.senither.library.SenLibrary;
import com.senither.library.utils.Scroller;
import java.util.HashMap;
import java.util.List;
import org.bukkit.entity.Player;

public class ScoreboardPage
{

    /**
     * Represents our scoreboard title.
     *
     * @var String
     */
    private Scroller title = null;

    /**
     * A list of entries for the scoreboard to render, the key is the
     * scoreboard score(index) and the value is the string to render.
     *
     * @var HashMap
     */
    private final HashMap<Integer, String> entries = new HashMap<>();

    /**
     * A list of cache entries for the scoreboard to render, the
     * list will be generated from the entries map, but the
     * strings will be converted to scroller objects,
     * allowing scrolling text on the scoreboard.
     *
     * @var HashMap
     */
    private final HashMap<Integer, Scroller> cache = new HashMap<>();

    /**
     * Determines if we should reset the page before this page, making
     * indexs that this page doesn't occupy, gets removed.
     *
     * @var Boolean
     */
    private boolean resetLastPage = false;

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
        this.title = new Scroller(title);
    }

    /**
     * Creates a new scoreboard page instance.
     *
     * @param entries The map of entries to add to the scoreboard page.
     */
    public ScoreboardPage(HashMap<Integer, String> entries)
    {
        entries.keySet().stream().forEach((index) -> {
            this.entries.put(index, entries.get(index));
        });
    }

    /**
     * Creates a new scoreboard page instance.
     *
     * @param title   The title of the scoreboard page.
     * @param entries The map of entries to add to the scoreboard page.
     */
    public ScoreboardPage(String title, HashMap<Integer, String> entries)
    {
        this.title = new Scroller(title);
        entries.keySet().stream().forEach((index) -> {
            this.entries.put(index, entries.get(index));
        });
    }

    /**
     * Sets the resetLastPage boolean, resetting the
     * last page before writing to the scoreboard.
     *
     * @param resetLastPage Determines if we should reset the last page or not.
     * @return ScoreboardPage
     */
    public ScoreboardPage resetLastPage(boolean resetLastPage)
    {
        this.resetLastPage = resetLastPage;

        return this;
    }

    /**
     * Determines if we should reset the page before this page, making
     * indexs that this page doesn't occupy, gets removed.
     *
     * @return Boolean
     */
    public boolean shouldResetLastPage()
    {
        return resetLastPage;
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
        entries.put(index, message);

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
            this.entries.put(size--, entry);
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
        entries.keySet().stream().forEach((index) -> {
            this.entries.put(index, entries.get(index));
        });

        return this;
    }

    /**
     * Returns the list of page entries.
     *
     * @return HashMap
     */
    public HashMap<Integer, Scroller> getEntries()
    {
        return cache;
    }

    /**
     * Clears the entries cache.
     */
    public void clearCache()
    {
        cache.clear();
    }

    /**
     * Checks to see if the cache is empty.
     *
     * @return Boolean
     */
    public boolean hasCache()
    {
        return !cache.isEmpty();
    }

    /**
     * Creates the entries cache, allowing the
     * page to be rendered on the scoreboard.
     *
     * @param library The sen-library instance.
     * @param player  The player to use to format player placeholders.
     */
    public void createCache(SenLibrary library, Player player)
    {
        this.clearCache();

        entries.keySet().stream().forEach((index) -> {
            cache.put(index, new Scroller(library.getPlaceholder().format(entries.get(index), player)));
        });
    }

    /**
     * Sets the scoreboard page title.
     *
     * @param title The title to use.
     * @return ScoreboardPage
     */
    public ScoreboardPage setTitle(String title)
    {
        this.title = new Scroller(title);

        return this;
    }

    /**
     * Returns the scoreboard page title.
     *
     * @return Scroller
     */
    public Scroller getTitle()
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
}
