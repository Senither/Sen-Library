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

    private final SenLibrary library;
    private final String name;

    private final Scoreboard board;
    private final DisplaySlot slot;
    private final Team team;
    private final List<ScoreboardPage> pages;
    private final HashMap<String, Player> listeners;
    private final HashMap<Integer, String> occupiedEntrys;

    private int index = 0;
    private int task = -1;
    private int delay = 20;
    private int extra = 0;
    private int ticks = 0;

    public ScoreboardHandler(SenLibrary library, String name, DisplaySlot slot, String objective, int delay)
    {
        this.library = library;
        this.name = name;

        this.slot = slot;
        this.delay = delay;
        this.pages = new ArrayList<>();
        this.listeners = new HashMap<>();
        this.occupiedEntrys = new HashMap<>();

        board = this.library.getPlugin().getServer().getScoreboardManager().getNewScoreboard();

        Objective obj = board.registerNewObjective(objective, "dummy");
        obj.setDisplaySlot(slot);

        team = board.registerNewTeam("senlibrary");

        this.start();
    }

    public void setDelay(int delay)
    {
        this.delay = delay;
    }

    public int getDelay()
    {
        return delay;
    }

    public ScoreboardHandler addPage(ScoreboardPage page)
    {
        this.pages.add(page);
        return this;
    }

    public ScoreboardHandler addPages(ScoreboardPage... pages)
    {
        this.pages.addAll(Arrays.asList(pages));
        return this;
    }

    public void removePage(int index)
    {
        this.pages.remove(index);
    }

    public boolean addPlayer(final Player player)
    {
        if (player == null || !player.isOnline()) {
            return false;
        }

        if (!listeners.containsKey(player.getName().toLowerCase())) {
            listeners.put(player.getName().toLowerCase(), player);
            player.setScoreboard(board);
            return true;
        }

        return false;
    }

    public void removePlayer(Player player)
    {
        if (player == null) {
            return;
        }
        removePlayer(player.getName());
    }

    public void removePlayer(String player)
    {
        if (listeners.containsKey(player.toLowerCase())) {
            listeners.remove(player.toLowerCase());
        }
    }

    public void clear()
    {
        pages.clear();
        listeners.clear();
        occupiedEntrys.clear();
    }

    public void start()
    {
        if (task != -1) {
            return;
        }
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(library.getPlugin(), this, 5, 1);
    }

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
            if (occupiedEntrys.containsKey(i) && !occupiedEntrys.get(i).equals(page.getEntries().get(i))) {
                board.resetScores(occupiedEntrys.get(i));
                occupiedEntrys.remove(i);
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

            occupiedEntrys.put(i, line);
        }
    }

    @Override
    public String toString()
    {
        return String.format("[id=%s, slot=%s, delay=%d]", name, slot.name(), delay);
    }
}
