package com.senither.library;

import com.senither.library.chat.ChatFormatter;
import com.senither.library.inventory.InventoryBuilder;
import com.senither.library.inventory.WallSide;
import com.senither.library.placeholder.PlaceholderRepository;
import com.senither.library.placeholder.contracts.PlayerPlaceholder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public final class SenLibrary
{

    /**
     * The Bukkit/Spigot plugin instance of the plugin
     * that are using SenLibrary, it is required
     * to run some server specific tasks.
     *
     * @var Plugin
     */
    private final Plugin plugin;

    /**
     * The placeholder repository instance.
     *
     * @var PlaceholderRepository
     */
    private final PlaceholderRepository placeholders;

    /**
     * The placeholder repository instance.
     *
     * @var PlaceholderRepository
     */
    private final ChatFormatter chat;

    public SenLibrary(Plugin plugin)
    {
        this.plugin = plugin;

        this.placeholders = new PlaceholderRepository(this);
        this.chat = new ChatFormatter(this);

        this.setupDefaultPlaceholders();
    }

    /**
     * Creates a new inventory builder instance.
     *
     * @param rows The amount of rows the inventory should have.
     * @return InventoryBuilder
     */
    public InventoryBuilder makeInventory(int rows)
    {
        return new InventoryBuilder(this, rows);
    }

    /**
     * Creates a new inventory builder instance, upon creating the
     * builder object, it will also generate walls around the
     * outside of the inventory with the given item.
     *
     * @param rows The amount of rows the inventory should have.
     * @param side The side that should be generated.
     * @param item The item used to generate the wall.
     * @return InventoryBuilder
     */
    public InventoryBuilder makeInventory(int rows, WallSide side, ItemStack item)
    {
        InventoryBuilder inventory = new InventoryBuilder(this, rows);

        return inventory.createWall(side, item);
    }

    /**
     * Creates a new inventory builder instance, upon creating the
     * builder object, it will also generate walls around the
     * outside of the inventory with the given item.
     *
     * @param rows      The amount of rows the inventory should have.
     * @param side      The side that should be generated.
     * @param item      The item used to generate the wall.
     * @param itemTitle The title/name of the item.
     * @return InventoryBuilder
     */
    public InventoryBuilder makeInventory(int rows, WallSide side, ItemStack item, String itemTitle)
    {
        InventoryBuilder inventory = new InventoryBuilder(this, rows);

        return inventory.createWall(side, item, itemTitle);
    }

    /**
     * Creates a new inventory builder instance with the given title.
     *
     * @param rows  The amount of rows the inventory should have.
     * @param title The inventory title, this is displayed at the top.
     * @return InventoryBuilder
     */
    public InventoryBuilder makeInventory(int rows, String title)
    {
        return new InventoryBuilder(this, rows, title);
    }

    /**
     * Creates a new inventory builder instance with the given title, upon
     * creating the builder object, it will also generate walls around
     * the outside of the inventory with the given item.
     *
     * @param rows  The amount of rows the inventory should have.
     * @param title The inventory title, this is displayed at the top.
     * @param side  The side that should be generated.
     * @param item  The item used to generate the wall.
     * @return InventoryBuilder
     */
    public InventoryBuilder makeInventory(int rows, String title, WallSide side, ItemStack item)
    {
        InventoryBuilder inventory = new InventoryBuilder(this, rows, title);

        return inventory.createWall(side, item);
    }

    /**
     * Creates a new inventory builder instance with the given title, upon
     * creating the builder object, it will also generate walls around
     * the outside of the inventory with the given item.
     *
     * @param rows      The amount of rows the inventory should have.
     * @param title     The inventory title, this is displayed at the top.
     * @param side      The side that should be generated.
     * @param item      The item used to generate the wall.
     * @param itemTitle The title/name of the item.
     *
     * @return InventoryBuilder
     */
    public InventoryBuilder makeInventory(int rows, String title, WallSide side, ItemStack item, String itemTitle)
    {
        InventoryBuilder inventory = new InventoryBuilder(this, rows, title);

        return inventory.createWall(side, item, itemTitle);
    }

    public PlaceholderRepository getPlaceholder()
    {
        return placeholders;
    }

    public ChatFormatter getChat()
    {
        return chat;
    }

    public Plugin getPlugin()
    {
        return plugin;
    }

    /**
     * Registers all the default placeholders, allowing
     * you to use the formating method to replace
     * the placeholders with actually values.
     */
    private void setupDefaultPlaceholders()
    {
        // Name placeholders
        getPlaceholder().push(plugin, "player", (PlayerPlaceholder) (Player player) -> (player != null) ? player.getName() : "Player");
        getPlaceholder().push(plugin, "playerDisplay", (PlayerPlaceholder) (Player player) -> (player != null) ? player.getDisplayName() : "Player");
        getPlaceholder().push(plugin, "world", (PlayerPlaceholder) (Player player) -> (player != null) ? player.getLocation().getWorld().getName() : "World");

        // Player propertie placeholders
        getPlaceholder().push(plugin, "level", (PlayerPlaceholder) (Player player) -> (player != null) ? "" + player.getLevel() : "0");
        getPlaceholder().push(plugin, "health", (PlayerPlaceholder) (Player player) -> (player != null) ? "" + player.getHealth() : "0");
        getPlaceholder().push(plugin, "food", (PlayerPlaceholder) (Player player) -> (player != null) ? "" + player.getFoodLevel() : "0");
    }
}
