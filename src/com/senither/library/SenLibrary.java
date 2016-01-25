package com.senither.library;

import com.senither.library.chat.ChatFilter;
import com.senither.library.chat.ChatFormatter;
import com.senither.library.config.Configuration;
import com.senither.library.database.DatabaseFactory;
import com.senither.library.database.MySQL;
import com.senither.library.database.SQLite;
import com.senither.library.exceptions.InvalidPlaceholderException;
import com.senither.library.inventory.InventoryBuilder;
import com.senither.library.inventory.WallSide;
import com.senither.library.item.ItemParser;
import com.senither.library.placeholder.PlaceholderRepository;
import com.senither.library.placeholder.contracts.PlayerPlaceholder;
import com.senither.library.scoreboard.ScoreboardFactory;
import com.senither.library.scoreboard.ScoreboardHandler;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;

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
     * The chat formatter instance.
     *
     * @var PlaceholderRepository
     */
    private ChatFormatter chatFormatter;

    /**
     * The chat filter repository instance.
     *
     * @var ChatFilter
     */
    private ChatFilter chatFilter;

    /**
     * The item parser instance, allowing you
     * to create new items a lot easier.
     *
     * @var ItemParser
     */
    private ItemParser itemParser;

    /**
     * The scoreboard factory instance allows you to
     * create new dynamic scoreboard a lot easier.
     *
     * @var ScoreboardFactory
     */
    private ScoreboardFactory scoreboardFactory;

    /**
     * The database factory instance allows you to create
     * connections to many different database types, and
     * run queries against them with the query builder.
     *
     * @var DatabaseFactory
     */
    private DatabaseFactory databaseFactory;

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

    public ScoreboardHandler makeScoreboard(Player player)
    {
        return makeScoreboard(player, DisplaySlot.SIDEBAR, "test", 5);
    }

    public ScoreboardHandler makeScoreboard(Player player, String objective)
    {
        return makeScoreboard(player, DisplaySlot.SIDEBAR, objective, 5);
    }

    public ScoreboardHandler makeScoreboard(Player player, int delay)
    {
        return makeScoreboard(player, DisplaySlot.SIDEBAR, "test", delay);
    }

    public ScoreboardHandler makeScoreboard(Player player, String objective, int delay)
    {
        return makeScoreboard(player, DisplaySlot.SIDEBAR, objective, delay);
    }

    public ScoreboardHandler makeScoreboard(String name)
    {
        return makeScoreboard(name, DisplaySlot.SIDEBAR, "test", 5);
    }

    public ScoreboardHandler makeScoreboard(String name, String objective)
    {
        return makeScoreboard(name, DisplaySlot.SIDEBAR, objective, 5);
    }

    public ScoreboardHandler makeScoreboard(String name, int delay)
    {
        return makeScoreboard(name, DisplaySlot.SIDEBAR, "test", delay);
    }

    public ScoreboardHandler makeScoreboard(String name, String objective, int delay)
    {
        return makeScoreboard(name, DisplaySlot.SIDEBAR, objective, delay);
    }

    //
    public ScoreboardHandler makeScoreboard(Player player, DisplaySlot slot)
    {
        return makeScoreboard(player, slot, "test", 5);
    }

    public ScoreboardHandler makeScoreboard(Player player, DisplaySlot slot, int delay)
    {
        return makeScoreboard(player, slot, "test", delay);
    }

    public ScoreboardHandler makeScoreboard(String name, DisplaySlot slot)
    {
        return makeScoreboard(name, slot, "test", 5);
    }

    public ScoreboardHandler makeScoreboard(String name, DisplaySlot slot, int delay)
    {
        return makeScoreboard(name, slot, "test", delay);
    }

    public ScoreboardHandler makeScoreboard(Player player, DisplaySlot slot, String objective)
    {
        return makeScoreboard(player, slot, objective, 5);
    }

    public ScoreboardHandler makeScoreboard(Player player, DisplaySlot slot, String objective, int delay)
    {
        if (scoreboardFactory == null) {
            scoreboardFactory = new ScoreboardFactory(this);
        }

        ScoreboardHandler scoreboard = scoreboardFactory.make(player, slot, objective, delay);

        scoreboard.addPlayer(player);

        return scoreboard;
    }

    public ScoreboardHandler makeScoreboard(String name, DisplaySlot slot, String objective)
    {
        return makeScoreboard(name, slot, objective, 5);
    }

    public ScoreboardHandler makeScoreboard(String name, DisplaySlot slot, String objective, int delay)
    {
        if (scoreboardFactory == null) {
            scoreboardFactory = new ScoreboardFactory(this);
        }

        return scoreboardFactory.make(name, slot, objective, delay);
    }

    /**
     * Creates a MySQL database connection using the given database, username and
     * password. The hostname used is "localhost", and the port is "3306", if
     * you wish to use a different hostname and port, please use the the
     * other connectMySQL method that supports hostnames and ports.
     *
     * @param database The database name to connect to.
     * @param username The username for the database.
     * @param password The password for the database.
     * @return MySQL
     */
    public MySQL connectMySQL(String database, String username, String password)
    {
        return connectMySQL("localhost", 3306, database, username, password);
    }

    /**
     * Creates a MySQL database connection using the given port, database,
     * username and password. The hostname used is "localhost", if you
     * whish to use a different hostname, please use the other
     * connectMySQL method that support hostnames.
     *
     * @param port     The port the database is listing on.
     * @param database The database name to connect to.
     * @param username The username for the database.
     * @param password The password for the database.
     * @return MySQL
     */
    public MySQL connectMySQL(int port, String database, String username, String password)
    {
        return connectMySQL("localhost", port, database, username, password);
    }

    /**
     * Creates a MySQL database connection using the given hostname,
     * database, username and password. The port used is "3306",
     * if you whish to use a different port, please use the
     * other connectMySQL method that support ports.
     *
     * @param hostname The hostname the database is listening on,
     * @param database The database name to connect to.
     * @param username The username for the database.
     * @param password The password for the database.
     * @return MySQL
     */
    public MySQL connectMySQL(String hostname, String database, String username, String password)
    {
        return connectMySQL(hostname, 3306, database, username, password);
    }

    /**
     * Creates a MySQL database connection using the given
     * hostname, port, database, username and password.
     *
     * @param hostname The hostname the database is listening on,
     * @param port     The port the database is listing on.
     * @param database The database name to connect to.
     * @param username The username for the database.
     * @param password The password for the database.
     * @return MySQL
     */
    public MySQL connectMySQL(String hostname, int port, String database, String username, String password)
    {
        MySQL connection = new MySQL(this, hostname, port, database, username, password);

        return (MySQL) getDatabaseFactory().setConnection(connection);
    }

    /**
     * Creates an in-memory SQLite database connection, the database
     * and it's tables and data will all be deleted upon a server
     * shutdown or reload, this should only be used for testing
     * environments or storing information temporarily.
     *
     * @return SQLite
     */
    public SQLite connectSQLite()
    {
        SQLite connection = new SQLite(this);

        return (SQLite) getDatabaseFactory().setConnection(connection);
    }

    /**
     * Creates a SQLite database connection using the directory and
     * filename, the extension will default to "sqlite", if you
     * whish to use a different extension, please use the
     * other connectSQLite method support extensions.
     *
     * @param directory The folder the database is located in.
     * @param filename  The name of the database file.
     * @return SQLite
     */
    public SQLite connectSQLite(String directory, String filename)
    {
        return connectSQLite(directory, filename, "sqlite");
    }

    /**
     * Creates a SQLite database connection using the directory,
     * filename and extension given to the method.
     *
     * @param directory The folder the database is located in.
     * @param filename  The name of the database file.
     * @param extension The extension of the database file.
     * @return SQLite
     */
    public SQLite connectSQLite(String directory, String filename, String extension)
    {
        SQLite connection = new SQLite(this, directory, filename, extension);

        return (SQLite) getDatabaseFactory().setConnection(connection);
    }

    /**
     * Returns the database factory instance, allowing
     * you to access schemas and query builders.
     *
     * @return DatabaseFactory
     */
    public DatabaseFactory getDatabaseFactory()
    {
        if (databaseFactory == null) {
            databaseFactory = new DatabaseFactory(this);
        }

        return databaseFactory;
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
    public ChatFormatter getChatFormatter()
    {
        return (chatFormatter == null) ? chatFormatter = new ChatFormatter(this) : chatFormatter;
    }

    /**
     * Returns the chat filter, allowing you to
     * add words and rules to the filter, and
     * as well as format strings.
     *
     * @return ChatFilter
     */
    public ChatFilter getChatFilter()
    {
        return (chatFilter == null) ? chatFilter = new ChatFilter() : chatFilter;
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
