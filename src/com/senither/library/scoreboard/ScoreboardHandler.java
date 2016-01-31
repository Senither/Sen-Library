package com.senither.library.scoreboard;

import com.senither.library.SenLibrary;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public final class ScoreboardHandler implements Runnable
{

    /**
     * Represents our Sen Library instance, this is
     * used to call other parts of the library.
     *
     * @var SenLibrary
     */
    private final SenLibrary library;

    /**
     * The scoreboard name(key) the handler is assigned to.
     *
     * @var String
     */
    private final String name;

    /**
     * The Bukkit scoreboard object that will be added to player listeners.
     *
     * @var Scoreboard
     */
    private final Scoreboard board;

    /**
     * The display slot to render the scoreboard at.
     *
     * @var DisplaySlot
     */
    private final DisplaySlot slot;

    /**
     * The scoreboard team to add players to.
     *
     * @var Team
     */
    private final Team team;

    /**
     * The list of scoreboard pages to render for the player listeners.
     *
     * @var List
     */
    private final List<ScoreboardPage> pages;

    /**
     * A map of players that should have the scoreboard rendered for them.
     *
     * @var HashMap
     */
    private final HashMap<String, Player> listeners;

    /**
     * A map of occupied entries form the scoreboard pages, this is used to
     * cache lines of the scoreboard so they won't have to be re-rendered.
     *
     * @var HashMap
     */
    private final HashMap<Integer, String> occupiedEntries;

    /**
     *
     *
     * @var Integer
     */
    private int index = 0;

    /**
     * The Bukkit scheduler task id, this is used to start and stop the
     * schedulers without having to creating new schedules every time.
     *
     * @var Integer
     */
    private int task = -1;

    /**
     * The update delay, this determines the update rate.
     *
     * @var Integer
     */
    private int delay = 20;

    /**
     * The extra delay to add from the scoreboard page.
     *
     * @var Integer
     */
    private int extra = 0;

    /**
     * The amount of scoreboard ticks, this is used to help determine
     * when to update the scoreboard for players.
     *
     * @var Integer
     */
    private int ticks = 0;

    /**
     * Creates a new scoreboard handler instance.
     *
     * @param library   The sen-library instance.
     * @param name      The scoreboard name(key).
     * @param slot      The display to render the scoreboard at.
     * @param objective The objective to assign the scoreboard to.
     * @param delay
     */
    public ScoreboardHandler(SenLibrary library, String name, DisplaySlot slot, String objective, int delay)
    {
        this.library = library;
        this.name = name;

        this.slot = slot;
        this.delay = delay;
        this.pages = new ArrayList<>();
        this.listeners = new HashMap<>();
        this.occupiedEntries = new HashMap<>();

        board = this.library.getPlugin().getServer().getScoreboardManager().getNewScoreboard();

        Objective obj = board.registerNewObjective(objective, "dummy");
        obj.setDisplaySlot(slot);

        team = board.registerNewTeam("senlibrary");

        this.start();
    }

    /**
     * Gets the scoreboard name(key).
     *
     * @return String
     */
    public String getScoreboardName()
    {
        return name;
    }

    /**
     * Sets the scoreboard update delay.
     *
     * @param delay The update delay in ticks.
     */
    public void setDelay(int delay)
    {
        this.delay = delay;
    }

    /**
     * Get the scoreboard update delay.
     *
     * @return Integer
     */
    public int getDelay()
    {
        return delay;
    }

    /**
     * Adds a scoreboard page to the list of pages to render.
     *
     * @param page The scoreboard page to add.
     * @return ScoreboardHandler
     */
    public ScoreboardHandler addPage(ScoreboardPage page)
    {
        pages.add(page);

        return this;
    }

    /**
     * Adds a list of scoreboard pages to the list of pages to render.
     *
     * @param pages The scoreboard pages to add.
     * @return ScoreboardHandler
     */
    public ScoreboardHandler addPages(ScoreboardPage... pages)
    {
        this.pages.addAll(Arrays.asList(pages));

        return this;
    }

    /**
     * Removes the scoreboard with the given index.
     *
     * @param index The index to remove.
     */
    public void removePage(int index)
    {
        pages.remove(index);
    }

    /**
     * Adds a player to the scoreboard listener.
     *
     * @param player The player to add.
     * @return Boolean
     */
    public boolean addPlayer(final Player player)
    {
        if (player == null || !player.isOnline()) {
            return false;
        }

        String playerName = player.getName().toLowerCase();

        if (!listeners.containsKey(playerName)) {
            listeners.put(playerName, player);

            player.setScoreboard(board);

            return true;
        }

        return false;
    }

    /**
     * Removes the given player from the player listeners.
     *
     * @param player The player to remove.
     */
    public void removePlayer(Player player)
    {
        if (player == null) {
            return;
        }

        removePlayer(player.getName());
    }

    /**
     * Removes the given player name from the player listeners.
     *
     * @param player The player name to remove.
     */
    public void removePlayer(String player)
    {
        if (listeners.containsKey(player.toLowerCase())) {
            listeners.remove(player.toLowerCase());
        }
    }

    /**
     * Clears the scoreboard handler of all its pages,
     * player listeners and cached lines.
     */
    public void clear()
    {
        pages.clear();
        listeners.clear();
        occupiedEntries.clear();
    }

    /**
     * Starts the scoreboard handler updater if it isn't started already.
     */
    public void start()
    {
        if (task != -1) {
            return;
        }

        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(library.getPlugin(), this, 5, 1);
    }

    /**
     * Stops the scoreboard handler updater if it is currently running.
     */
    public void stop()
    {
        if (task == -1) {
            return;
        }

        library.getPlugin().getServer().getScheduler().cancelTask(task);
        task = -1;
    }

    @Override
    public void run()
    {
        if (ticks-- > 0) {
            return;
        }

        if (pages.isEmpty()) {
            return;
        }

        if (pages.size() == ++index) {
            index = 0;
        }

        ScoreboardPage page = pages.get(index);
        extra = page.getExtraDelay();
        ticks = delay + extra;

        Objective obj = board.getObjective(slot);

        if (page.getTitle() != null) {
            obj.setDisplayName(page.getTitle());
        }

        for (int i : page.getEntries().keySet()) {
            if (occupiedEntries.containsKey(i) && !occupiedEntries.get(i).equals(page.getEntries().get(i))) {
                board.resetScores(occupiedEntries.get(i));
                occupiedEntries.remove(i);
            }

            if (page.getEntries().get(i) == null) {
                continue;
            }

            String line = page.getEntries().get(i);

            for (Player player : listeners.values()) {
                line = library.getPlaceholder().format(line, player);
            }

            team.addEntry(line);
            obj.getScore(line).setScore(i);

            occupiedEntries.put(i, line);
        }
    }

    @Override
    public String toString()
    {
        return String.format("[id=%s, slot=%s, delay=%d]", getScoreboardName(), slot.name(), getDelay());
    }
}
