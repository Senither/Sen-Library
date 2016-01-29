package com.senither.library.scoreboard;

import com.senither.library.SenLibrary;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;

public class ScoreboardFactory implements Listener
{

    private final SenLibrary library;
    private final HashMap<String, ScoreboardHandler> scoreboards = new HashMap<>();

    public ScoreboardFactory(SenLibrary library)
    {
        this.library = library;
    }

    public ScoreboardHandler make(Player player, DisplaySlot slot, String objective, int delay)
    {
        return make(player.getUniqueId().toString(), slot, objective, delay);
    }

    public ScoreboardHandler make(String name, DisplaySlot slot, String objective, int delay)
    {
        if (scoreboards.containsKey(name)) {
            return make(UUID.randomUUID().toString(), slot, objective, delay);
        }

        ScoreboardHandler scoreboard = new ScoreboardHandler(library, name, slot, objective, delay);
        scoreboards.put(name, scoreboard);

        library.info("ScoreboardFactory - Building scoreboard: " + scoreboard);

        return scoreboard;
    }

    public boolean removeScoreboard(String name)
    {
        if (scoreboards.containsKey(name)) {
            ScoreboardHandler scoreboard = scoreboards.remove(name);

            library.info("ScoreboardFactory - Removing scoreboard: " + scoreboard);

            scoreboard.stop();
            scoreboard.clear();

            return true;
        }

        return false;
    }

    @EventHandler
    public void onPlayerLogout(PlayerQuitEvent e)
    {
        removeScoreboard(e.getPlayer().getUniqueId().toString());
    }
}
