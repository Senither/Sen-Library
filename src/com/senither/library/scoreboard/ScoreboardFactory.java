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

    /**
     * Represents our Sen Library instance, this is
     * used to call other parts of the library.
     *
     * @var SenLibrary
     */
    private final SenLibrary library;

    /**
     * A map of all of the active scoreboards handlers, the
     * key used for the map is the scoreboard key.
     *
     * @var HashMap
     */
    private final HashMap<String, ScoreboardHandler> scoreboards = new HashMap<>();

    /**
     * Creates a new scoreboard factory instance.
     *
     * @param library The sen-library instance.
     */
    public ScoreboardFactory(SenLibrary library)
    {
        this.library = library;
    }

    /**
     * Creates a new scoreboard handler with the given display
     * slot, objective and update delay. The player's UUID
     * will be used as the scoreboard name(key).
     *
     * @param player    The players UUID to use.
     * @param slot      The display slot to render the scoreboard at.
     * @param objective The objective to assign the scoreboard to.
     * @param delay     The scoreboard update delay.
     * @return ScoreboardHandler
     */
    public ScoreboardHandler make(Player player, DisplaySlot slot, String objective, int delay)
    {
        return make(player.getUniqueId().toString(), slot, objective, delay);
    }

    /**
     * Creates a new scoreboard handler with the given name(key),
     * display slot, objective and update delay.
     *
     * @param name      The name(key) to assign the scoreboard to.
     * @param slot      The display slot to render the scoreboard at.
     * @param objective The objective to assign the scoreboard to.
     * @param delay     The scoreboard update delay.
     * @return ScoreboardHandler
     */
    public ScoreboardHandler make(String name, DisplaySlot slot, String objective, int delay)
    {
        if (scoreboards.containsKey(name)) {
            return make(UUID.randomUUID().toString(), slot, objective, delay);
        }

        ScoreboardHandler scoreboard = new ScoreboardHandler(library, name, slot, objective, delay);
        scoreboards.put(name, scoreboard);

        library.getLogger().info("ScoreboardFactory - Building scoreboard: " + scoreboard);

        return scoreboard;
    }

    /**
     * Removes the given scoreboard name(key) from the scoreboard map,
     * if the scoreboard is still active the scoreboard will be
     * stopped and cleared from any pages and listeners.
     *
     * @param name The scoreboard name(key) to remove.
     * @return Boolean
     */
    public boolean removeScoreboard(String name)
    {
        if (scoreboards.containsKey(name)) {
            ScoreboardHandler scoreboard = scoreboards.remove(name);

            library.getLogger().info("ScoreboardFactory - Removing scoreboard: " + scoreboard);

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
