package com.senither.library;

import com.senither.library.chat.ChatFormatter;
import com.senither.library.config.Configuration;
import com.senither.library.database.DatabaseFactory;
import com.senither.library.database.MySQL;
import com.senither.library.database.SQLite;
import com.senither.library.exceptions.InvalidPlaceholderException;
import com.senither.library.inventory.InventoryBuilder;
import com.senither.library.inventory.WallSide;
import com.senither.library.item.ItemParser;
import com.senither.library.message.Message;
import com.senither.library.message.MessageManager;
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
     * The sen library debugging value, if this is set to
     * true, all actions going through the library will
     * post debug messages to the console.
     *
     * @var Boolean
     */
    private boolean debug = false;

    /**
     * Represents our database logger instance, allowing
     * us to get more information during development
     * if debugging has been enabled.
     *
     * @var Logger
     */
    private static final Logger LOGGER = Logger.getGlobal();

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
     * Creates a new SenLibrary instance and sets
     * the debugging value at the same time.
     *
     * @param plugin The JavaPlugin class instance.
     * @param debug  The debug state that should be used.
     */
    public SenLibrary(JavaPlugin plugin, boolean debug)
    {
        this.plugin = plugin;
        this.debug = debug;
    }

    /**
     * Sets the new debugging state, if it is set to true, the
     * library will send messages to the console about what
     * is going on within the library itself
     *
     * @param value The new debugging value.
     */
    public void enableDebug(boolean value)
    {
        this.debug = value;
    }

    /**
     * Returns true if the library is in debugging
     * mode, it will return false otherwise.
     *
     * @return Boolean
     */
    public boolean isDebugging()
    {
        return debug;
    }

    /**
     * Sends a info log message to the console/terminal.
     *
     * @param message The message to send.
     */
    public void info(String message)
    {
        log(Level.INFO, message);
    }

    /**
     * Sends a info log message to the console/terminal.
     *
     * @param message The message to send.
     * @param objects The objects to parse to the logger log method.
     */
    public void info(String message, Object... objects)
    {
        log(Level.INFO, message, objects);
    }

    /**
     * Sends a warning log message to the console/terminal.
     *
     * @param message The message to send.
     */
    public void warning(String message)
    {
        log(Level.WARNING, message);
    }

    /**
     * Sends a warning log message to the console/terminal.
     *
     * @param message The message to send.
     * @param objects The objects to parse to the logger log method.
     */
    public void warning(String message, Object... objects)
    {
        log(Level.WARNING, message, objects);
    }

    /**
     * Sends a error log message to the console/terminal.
     *
     * @param message The message to send.
     */
    public void error(String message)
    {
        log(Level.SEVERE, message);
    }

    /**
     * Sends a error log message to the console/terminal.
     *
     * @param message The message to send.
     * @param objects The objects to parse to the logger log method.
     */
    public void error(String message, Object... objects)
    {
        log(Level.SEVERE, message, objects);
    }

    /**
     * Sends a log message to the console/terminal of the given
     * level if the debugging level is set to true.
     *
     * @param level   The level of the log message.
     * @param message The message to send.
     */
    public void log(Level level, String message)
    {
        if (debug) {
            LOGGER.log(level, "[{0}::SenLibrary]: {1}", new Object[]{plugin.getName(), message});
        }
    }

    /**
     * Sends a log message to the console/terminal of the given level
     * with the given objects if the debugging level is set to true.
     *
     * @param level   The level of the log message.
     * @param message The message to send.
     * @param objects The objects that should be parsed to the logger log method.
     */
    public void log(Level level, String message, Object... objects)
    {
        if (debug) {
            LOGGER.log(level, "[" + plugin.getName() + "::SenLibrary]: " + message, objects);
        }
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
     * Creates a dynamic scoreboard handler for the given player, when creating a
     * scoreboard for a player, the player will automatically be added to the
     * scoreboard listeners. The objective that will be used is "test"
     * and the delay update timer used is 5.
     *
     * This will used the sidebar display slot.
     *
     * @param player The player the scoreboard should be created for.
     * @return ScoreboardHandler
     */
    public ScoreboardHandler makeScoreboard(Player player)
    {
        return makeScoreboard(player, DisplaySlot.SIDEBAR, "test", 5);
    }

    /**
     * Creates a dynamic scoreboard handler for the given player and the given
     * objective, when creating a scoreboard for a player, the player will
     * automatically be added to the scoreboard listeners. The delay
     * update timer used is 5.
     *
     * This will used the sidebar display slot.
     *
     * @param player    The player the scoreboard should be created for.
     * @param objective The objective to assign the scoreboard to.
     * @return ScoreboardHandler
     */
    public ScoreboardHandler makeScoreboard(Player player, String objective)
    {
        return makeScoreboard(player, DisplaySlot.SIDEBAR, objective, 5);
    }

    /**
     * Creates a dynamic scoreboard handler for the given player with the given
     * delay, when creating a scoreboard for a player, the player will
     * automatically be added to the scoreboard listeners. The
     * objective that will be used is "test".
     *
     * This will used the sidebar display slot.
     *
     * @param player The player the scoreboard should be created for.
     * @param delay  The scoreboard update delay.
     * @return ScoreboardHandler
     */
    public ScoreboardHandler makeScoreboard(Player player, int delay)
    {
        return makeScoreboard(player, DisplaySlot.SIDEBAR, "test", delay);
    }

    /**
     * Creates a dynamic scoreboard handler for the given player, objective
     * and delay, when creating a scoreboard for a player, the player
     * will automatically be added to the scoreboard listeners.
     *
     * This will used the sidebar display slot.
     *
     * @param player    The player the scoreboard should be created for.
     * @param objective The objective to assign the scoreboard to.
     * @param delay     The scoreboard update delay.
     * @return ScoreboardHandler
     */
    public ScoreboardHandler makeScoreboard(Player player, String objective, int delay)
    {
        return makeScoreboard(player, DisplaySlot.SIDEBAR, objective, delay);
    }

    /**
     * Creates a dynamic scoreboard handler for the given player on the given
     * display slot, when creating a scoreboard for a player, the player
     * will automatically be added to the scoreboard listeners. The
     * object used is "test", and update delay is 5.
     *
     * @param player The player the scoreboard should be created for.
     * @param slot   The display slot to render the scoreboard at.
     * @return ScoreboardHandler
     */
    public ScoreboardHandler makeScoreboard(Player player, DisplaySlot slot)
    {
        return makeScoreboard(player, slot, "test", 5);
    }

    /**
     * Creates a dynamic scoreboard handler for the given player on the given
     * display slot with the given delay, when creating a scoreboard for a
     * player, the player will automatically be added to the scoreboard
     * listeners. The object used is "test".
     *
     * @param player The player the scoreboard should be created for.
     * @param slot   The display slot to render the scoreboard at.
     * @param delay  The scoreboard update delay.
     * @return ScoreboardHandler
     */
    public ScoreboardHandler makeScoreboard(Player player, DisplaySlot slot, int delay)
    {
        return makeScoreboard(player, slot, "test", delay);
    }

    /**
     * Creates a dynamic scoreboard handler for the given player on the given
     * display slot with the given objective, when creating a scoreboard for
     * a player, the player will automatically be added to the scoreboard
     * listeners. The update delay used is 5.
     *
     * @param player    The player the scoreboard should be created for.
     * @param slot      The display slot to render the scoreboard at.
     * @param objective The objective to assign the scoreboard to.
     * @return ScoreboardHandler
     */
    public ScoreboardHandler makeScoreboard(Player player, DisplaySlot slot, String objective)
    {
        return makeScoreboard(player, slot, objective, 5);
    }

    /**
     * Creates a dynamic scoreboard handler for the given player on the given display
     * slot with the given objective, when creating a scoreboard for a player, the
     * player will automatically be added to the scoreboard listeners.
     *
     * @param player    The player the scoreboard should be created for.
     * @param slot      The display slot to render the scoreboard at.
     * @param objective The objective to assign the scoreboard to.
     * @param delay     The scoreboard update delay.
     * @return ScoreboardHandler
     */
    public ScoreboardHandler makeScoreboard(Player player, DisplaySlot slot, String objective, int delay)
    {
        ScoreboardHandler scoreboard = getScoreboardFactory().make(player, slot, objective, delay);

        scoreboard.addPlayer(player);

        return scoreboard;
    }

    /**
     * Creates a dynamic scoreboard handler with the given name(key). The display slot
     * used is "sidebar", the objective is "test" and the update delay is 5.
     *
     * If a scoreboard name(key) is already taken, a randomly assigned UUID will
     * be used instead, you can get the name(key) from the scoreboard handler.
     *
     * @param name The name or key to give the scoreboard.
     * @return ScoreboardHandler
     */
    public ScoreboardHandler makeScoreboard(String name)
    {
        return makeScoreboard(name, DisplaySlot.SIDEBAR, "test", 5);
    }

    /**
     * Creates a dynamic scoreboard handler with the given name(key) and the given
     * objective. The display slot used is "sidebar" and the update delay is 5.
     *
     * If a scoreboard name(key) is already taken, a randomly assigned UUID will
     * be used instead, you can get the name(key) from the scoreboard handler.
     *
     * @param name      The name or key to give the scoreboard.
     * @param objective The objective to assign the scoreboard to.
     * @return ScoreboardHandler
     */
    public ScoreboardHandler makeScoreboard(String name, String objective)
    {
        return makeScoreboard(name, DisplaySlot.SIDEBAR, objective, 5);
    }

    /**
     * Creates a dynamic scoreboard handler with the given name(key) and the given update
     * delay. The display slot used is "sidebar" and the objective is "test".
     *
     * If a scoreboard name(key) is already taken, a randomly assigned UUID will
     * be used instead, you can get the name(key) from the scoreboard handler.
     *
     * @param name  The name or key to give the scoreboard.
     * @param delay The scoreboard update delay.
     * @return ScoreboardHandler
     */
    public ScoreboardHandler makeScoreboard(String name, int delay)
    {
        return makeScoreboard(name, DisplaySlot.SIDEBAR, "test", delay);
    }

    /**
     * Creates a dynamic scoreboard handler with the given name(key), the objective
     * and the given update delay. The display slot used is "sidebar".
     *
     * If a scoreboard name(key) is already taken, a randomly assigned UUID will
     * be used instead, you can get the name(key) from the scoreboard handler.
     *
     * @param name      The name or key to give the scoreboard.
     * @param objective The objective to assign the scoreboard to.
     * @param delay     The scoreboard update delay.
     * @return ScoreboardHandler
     */
    public ScoreboardHandler makeScoreboard(String name, String objective, int delay)
    {
        return makeScoreboard(name, DisplaySlot.SIDEBAR, objective, delay);
    }

    /**
     * Creates a dynamic scoreboard handler with the given name(key) on the given
     * display slot. The objective used is "test", and the update delay is 5.
     *
     * If a scoreboard name(key) is already taken, a randomly assigned UUID will
     * be used instead, you can get the name(key) from the scoreboard handler.
     *
     * @param name The name or key to give the scoreboard.
     * @param slot The display slot to render the scoreboard at.
     * @return ScoreboardHandler
     */
    public ScoreboardHandler makeScoreboard(String name, DisplaySlot slot)
    {
        return makeScoreboard(name, slot, "test", 5);
    }

    /**
     * Creates a dynamic scoreboard handler with the given name(key), display slot
     * and update delay. The objective used is "test".
     *
     * If a scoreboard name(key) is already taken, a randomly assigned UUID will
     * be used instead, you can get the name(key) from the scoreboard handler.
     *
     * @param name  The name or key to give the scoreboard.
     * @param slot  The display slot to render the scoreboard at.
     * @param delay The scoreboard update delay.
     * @return
     */
    public ScoreboardHandler makeScoreboard(String name, DisplaySlot slot, int delay)
    {
        return makeScoreboard(name, slot, "test", delay);
    }

    /**
     * Creates a dynamic scoreboard handler with the given name(key), display slot
     * and objective. The scoreboard update delay used is 5.
     *
     * If a scoreboard name(key) is already taken, a randomly assigned UUID will
     * be used instead, you can get the name(key) from the scoreboard handler.
     *
     * @param name      The name or key to give the scoreboard.
     * @param slot      The display slot to render the scoreboard at.
     * @param objective The objective to assign the scoreboard to.
     * @return
     */
    public ScoreboardHandler makeScoreboard(String name, DisplaySlot slot, String objective)
    {
        return makeScoreboard(name, slot, objective, 5);
    }

    /**
     * Creates a dynamic scoreboard handler with the given name(key),
     * display slot, objective and update delay.
     *
     * If a scoreboard name(key) is already taken, a randomly assigned UUID will
     * be used instead, you can get the name(key) from the scoreboard handler.
     *
     * @param name      The name or key to give the scoreboard.
     * @param slot      The display slot to render the scoreboard at.
     * @param objective The objective to assign the scoreboard to.
     * @param delay     The scoreboard update delay.
     * @return ScoreboardHandler
     */
    public ScoreboardHandler makeScoreboard(String name, DisplaySlot slot, String objective, int delay)
    {
        return getScoreboardFactory().make(name, slot, objective, delay);
    }

    /**
     * Makes a message packet, allowing you to send a title or tab packet
     * message, if the header or footer is null or an empty string,
     * they will be ignored when the packet is created and sent.
     *
     * @param header The header to send to the player
     * @return Message
     */
    public Message makeMessage(String header)
    {
        if (!MessageManager.hasLibrary()) {
            MessageManager.setLibrary(this);
        }

        Message message = new Message();

        return message.setHeader(header);
    }

    /**
     * Makes a message packet, allowing you to send a title or tab packet
     * message, if the header or footer is null or an empty string,
     * they will be ignored when the packet is created and sent.
     *
     * @param header The header to send.
     * @param footer The footer to send.
     * @return Message
     */
    public Message makeMessage(String header, String footer)
    {
        return makeMessage(header).setFooter(footer);
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
        return (databaseFactory == null) ? databaseFactory = new DatabaseFactory(this) : databaseFactory;
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
                placeholders.push(plugin, "playerName", (PlayerPlaceholder) (Player player) -> (player != null) ? player.getName() : "Player");
                placeholders.push(plugin, "playerDisplayName", (PlayerPlaceholder) (Player player) -> (player != null) ? player.getDisplayName() : "Player");
                placeholders.push(plugin, "playerWorld", (PlayerPlaceholder) (Player player) -> (player != null) ? player.getLocation().getWorld().getName() : "World");
                placeholders.push(plugin, "playerLocationX", (PlayerPlaceholder) (Player player) -> (player != null) ? "" + player.getLocation().getX() : "0");
                placeholders.push(plugin, "playerLocationY", (PlayerPlaceholder) (Player player) -> (player != null) ? "" + player.getLocation().getY() : "0");
                placeholders.push(plugin, "playerLocationZ", (PlayerPlaceholder) (Player player) -> (player != null) ? "" + player.getLocation().getZ() : "0");
                placeholders.push(plugin, "playerLocationPitch", (PlayerPlaceholder) (Player player) -> (player != null) ? "" + player.getLocation().getPitch() : "0");
                placeholders.push(plugin, "playerLocationYaw", (PlayerPlaceholder) (Player player) -> (player != null) ? "" + player.getLocation().getYaw() : "0");

                // Player propertie placeholders
                placeholders.push(plugin, "playerLevel", (PlayerPlaceholder) (Player player) -> (player != null) ? "" + player.getLevel() : "0");
                placeholders.push(plugin, "playerHealth", (PlayerPlaceholder) (Player player) -> (player != null) ? "" + player.getHealth() : "0");
                placeholders.push(plugin, "playerFood", (PlayerPlaceholder) (Player player) -> (player != null) ? "" + player.getFoodLevel() : "0");
            } catch (InvalidPlaceholderException ex) {
                Logger.getLogger(SenLibrary.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return placeholders;
    }

    /**
     * Returns the scoreboard factory, allowing you to create and remove
     * dynamic scoreboards for one or multiple players at a time. If
     * the scoreboard factory haven't been created yet, a new
     * instance will be created, and it will be registered
     * into the event management so player scoreboards
     * can be automatically be removed.
     *
     * @return ScoreboardFactory
     */
    public ScoreboardFactory getScoreboardFactory()
    {
        if (scoreboardFactory == null) {
            scoreboardFactory = new ScoreboardFactory(this);

            plugin.getServer().getPluginManager().registerEvents(scoreboardFactory, plugin);
        }

        return scoreboardFactory;
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
