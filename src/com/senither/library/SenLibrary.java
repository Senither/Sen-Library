package com.senither.library;

import com.senither.library.chat.ChatFormatter;
import com.senither.library.config.Configuration;
import com.senither.library.exceptions.InvalidPlaceholderException;
import com.senither.library.inventory.InventoryBuilder;
import com.senither.library.inventory.WallSide;
import com.senither.library.item.ItemParser;
import com.senither.library.placeholder.PlaceholderRepository;
import com.senither.library.placeholder.contracts.PlayerPlaceholder;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class SenLibrary
{

    /**
     * The Bukkit/Spigot plugin instance of the plugin
     * that are using SenLibrary, it is required
     * to run some server specific tasks.
     *
     * @var Plugin
     */
    private final JavaPlugin plugin;

    /**
     * The placeholder repository instance.
     *
     * @var PlaceholderRepository
     */
    private PlaceholderRepository placeholders;

    /**
     * The placeholder repository instance.
     *
     * @var PlaceholderRepository
     */
    private ChatFormatter chat;

    /**
     * The item parser instance, allowing you
     * to create new items a lot easier.
     *
     * @var ItemParser
     */
    private ItemParser itemParser;

    /**
     * Creates a new SenLibrary instance.
     *
     * @param plugin The JavaPlugin class instance.
     */
    public SenLibrary(JavaPlugin plugin)
    {
        this.plugin = plugin;
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

    /**
     * Creates a new custom configuration file, allowing you to store and load information
     * from and to the configuration file, if a file with the configuration name is
     * found in the main path of your plugin, it will be loaded into memory
     * and used as the default properties for the configuration.
     *
     * @param name The configuration name.
     * @return Configuration
     */
    public Configuration makeConfig(String name)
    {
        return new Configuration(this, name);
    }

    /**
     * Creates a new custom configuration file, allowing you to store and load information
     * from and to the configuration file, if a file with the configuration name is
     * found in the main path of your plugin, it will be loaded into memory
     * and used as the default properties for the configuration.
     *
     * @param folder The folder to store the configuration in.
     * @param name   The configuration name.
     * @return Configuration
     */
    public Configuration makeConfig(File folder, String name)
    {
        return new Configuration(this, folder, name);
    }

    /**
     * Creates a new custom configuration file, allowing you to store and load information
     * from and to the configuration file, if a file with the configuration name is
     * found in the main path of your plugin, it will be loaded into memory
     * and used as the default properties for the configuration.
     *
     * @param folder     The folder to store the configuration in.
     * @param name       The configuration name.
     * @param defaultYml The default yml that will be written to the configuration.
     * @return Configuration
     */
    public Configuration makeConfig(File folder, String name, List<String> defaultYml)
    {
        return new Configuration(this, folder, name, defaultYml);
    }

    /**
     * Returns the item parser instance, allowing you
     * to create and modify item stacks easier.
     *
     * @return ItemParser
     */
    public ItemParser getItemParser()
    {
        return (itemParser == null) ? itemParser = new ItemParser(this) : itemParser;
    }

    /**
     * Returns the placeholder repository, allowing you to format messages
     * using placeholders with actually values, as-well as pushing
     * custom placeholders into the repository.
     *
     * @return PlaceholderRepository
     */
    public PlaceholderRepository getPlaceholder()
    {
        if (placeholders == null) {
            placeholders = new PlaceholderRepository(this);

            try {
                // Name placeholders
                placeholders.push(plugin, "player", (PlayerPlaceholder) (Player player) -> (player != null) ? player.getName() : "Player");
                placeholders.push(plugin, "playerDisplay", (PlayerPlaceholder) (Player player) -> (player != null) ? player.getDisplayName() : "Player");
                placeholders.push(plugin, "world", (PlayerPlaceholder) (Player player) -> (player != null) ? player.getLocation().getWorld().getName() : "World");

                // Player propertie placeholders
                placeholders.push(plugin, "level", (PlayerPlaceholder) (Player player) -> (player != null) ? "" + player.getLevel() : "0");
                placeholders.push(plugin, "health", (PlayerPlaceholder) (Player player) -> (player != null) ? "" + player.getHealth() : "0");
                placeholders.push(plugin, "food", (PlayerPlaceholder) (Player player) -> (player != null) ? "" + player.getFoodLevel() : "0");
            } catch (InvalidPlaceholderException ex) {
                Logger.getLogger(SenLibrary.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return placeholders;
    }

    /**
     * Returns the chat formatter, allowing you to format
     * messages, send messages depending on conditions.
     *
     * @return ChatFormatter
     */
    public ChatFormatter getChat()
    {
        return (chat == null) ? chat = new ChatFormatter(this) : chat;
    }

    /**
     * Returns the plugin instance that was parsed
     * to the Sen Library to setup its instance.
     *
     * @return JavaPlugin
     */
    public JavaPlugin getPlugin()
    {
        return plugin;
    }
}
