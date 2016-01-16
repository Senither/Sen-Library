package com.senither.library.placeholder.contracts;

import org.bukkit.entity.Player;

public interface PlayerPlaceholder extends Placeholder
{

    /**
     * Player placeholder interface, this should be used for
     * placeholders that require the specific player object
     * to return a valid value, this is good for creating
     * unique placeholder values for each player.
     *
     * @param player The player object.
     * @return String
     */
    public String run(Player player);
}
