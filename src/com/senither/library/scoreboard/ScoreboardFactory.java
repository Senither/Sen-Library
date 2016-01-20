package com.senither.library.scoreboard;

import com.senither.library.SenLibrary;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

public class ScoreboardFactory
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

        ScoreboardHandler scoreboard = new ScoreboardHandler(library, slot, objective, delay);

        scoreboards.put(name, scoreboard);

        return scoreboard;
    }
}
