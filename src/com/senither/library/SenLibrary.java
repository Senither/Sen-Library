package com.senither.library;

import com.senither.library.inventory.InventoryBuilder;
import com.senither.library.placeholder.PlaceholderRepository;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public final class SenLibrary
{

    private final Plugin plugin;

    private final PlaceholderRepository placeholders;

    public SenLibrary(Plugin plugin)
    {
        this.plugin = plugin;

        this.placeholders = new PlaceholderRepository(this);

        getPlaceholderRepository().push(plugin, "player", (Player player) -> (player != null) ? player.getName() : "Player");
        
        
        getPlaceholderRepository().push(plugin, "level", (Player player) -> (player != null) ? "" + player.getLevel() : "0");
    }

    public InventoryBuilder makeInventory(int rows)
    {
        return new InventoryBuilder(this, rows);
    }

    public InventoryBuilder makeInventory(int rows, String title)
    {
        return new InventoryBuilder(this, rows, title);
    }

    public PlaceholderRepository getPlaceholderRepository()
    {
        return placeholders;
    }
}
