package com.senither.library;

import com.senither.library.inventory.InventoryBuilder;
import com.senither.library.inventory.WallSide;
import com.senither.library.placeholder.PlaceholderRepository;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public final class SenLibrary
{

    private final Plugin plugin;

    private final PlaceholderRepository placeholders;

    public SenLibrary(Plugin plugin)
    {
        this.plugin = plugin;

        this.placeholders = new PlaceholderRepository(this);

        this.setupDefaultPlaceholders();
    }

    public InventoryBuilder makeInventory(int rows)
    {
        return new InventoryBuilder(this, rows);
    }

    public InventoryBuilder makeInventory(int rows, WallSide side, ItemStack item)
    {
        InventoryBuilder inventory = new InventoryBuilder(this, rows);

        return inventory.createWall(side, item);
    }

    public InventoryBuilder makeInventory(int rows, WallSide side, ItemStack item, String title)
    {
        InventoryBuilder inventory = new InventoryBuilder(this, rows);

        return inventory.createWall(side, item, title);
    }

    public InventoryBuilder makeInventory(int rows, String title)
    {
        return new InventoryBuilder(this, rows, title);
    }

    public InventoryBuilder makeInventory(int rows, String title, WallSide side, ItemStack item)
    {
        InventoryBuilder inventory = new InventoryBuilder(this, rows, title);

        return inventory.createWall(side, item);
    }

    public InventoryBuilder makeInventory(int rows, String title, WallSide side, ItemStack item, String itemTitle)
    {
        InventoryBuilder inventory = new InventoryBuilder(this, rows, title);

        return inventory.createWall(side, item, itemTitle);
    }

    public PlaceholderRepository getPlaceholderRepository()
    {
        return placeholders;
    }

    /**
     * Registers all the default placeholders, allowing
     * you to use the formating method to replace
     * the placeholders with actually values.
     */
    private void setupDefaultPlaceholders()
    {
        // Name placeholders
        getPlaceholderRepository().push(plugin, "player", (Player player) -> (player != null) ? player.getName() : "Player");
        getPlaceholderRepository().push(plugin, "playerDisplay", (Player player) -> (player != null) ? player.getDisplayName() : "Player");
        getPlaceholderRepository().push(plugin, "world", (Player player) -> (player != null) ? player.getLocation().getWorld().getName() : "World");

        // Player propertie placeholders
        getPlaceholderRepository().push(plugin, "level", (Player player) -> (player != null) ? "" + player.getLevel() : "0");
        getPlaceholderRepository().push(plugin, "health", (Player player) -> (player != null) ? "" + player.getHealth() : "0");
        getPlaceholderRepository().push(plugin, "food", (Player player) -> (player != null) ? "" + player.getFoodLevel() : "0");
    }
}
