package com.senither.library.config;

import com.senither.library.SenLibrary;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public final class Configuration implements ConfigurationSection
{

    /**
     * Represents our plugin instance, allowing
     * you to access data about the server.
     *
     * @var JavaPlugin
     */
    private final JavaPlugin plugin;

    /**
     * The file name of the configuration
     *
     * @var String
     */
    private final String fileName;

    /**
     * The folder to store the config in.
     *
     * @var File
     */
    private File folder;

    /**
     * The configuration file object.
     *
     * @var File
     */
    private File configFile;

    /**
     * The file configuration object, allowing you to
     * set and get data from the config.
     *
     * @var FileConfiguration
     */
    private FileConfiguration fileConfiguration;

    /**
     * Creates a new configuration instance from the given file name.
     *
     * @param library  The sen-library instance.
     * @param fileName The configuration file name.
     */
    public Configuration(SenLibrary library, String fileName)
    {
        Validate.isTrue(library.getPlugin().isEnabled(), "Plugin is not enabled");

        this.plugin = library.getPlugin();
        this.fileName = fileName;
        this.folder = plugin.getDataFolder();
        this.configFile = new File(folder, fileName);

        if (!configFile.exists()) {
            getFileConfiguration().options().copyDefaults(true);
            saveConfig();
        }

        this.fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Creates a new configuration instance from the given folder and file name.
     *
     * @param library  The sen-library instance.
     * @param folder   The folder to store the config in.
     * @param fileName The configuration file name.
     */
    public Configuration(SenLibrary library, File folder, String fileName)
    {
        Validate.isTrue(library.getPlugin().isEnabled(), "Plugin is not enabled");

        this.plugin = library.getPlugin();
        this.fileName = fileName;
        this.folder = folder;
        this.configFile = new File(folder, fileName);

        if (!configFile.exists()) {
            getFileConfiguration().options().copyDefaults(true);
            saveConfig();
        }

        this.fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Creates a new configuration instance from the given folder and file name.
     *
     * @param library  The sen-library instance.
     * @param folder   The folder to store the config in.
     * @param fileName The configuration file name.
     * @param yaml     The default YAML to write to the file if it doesn't exists.
     */
    public Configuration(SenLibrary library, File folder, String fileName, List<String> yaml)
    {
        Validate.isTrue(library.getPlugin().isEnabled(), "Plugin is not enabled");

        this.plugin = library.getPlugin();
        this.fileName = fileName;
        this.folder = folder;
        this.configFile = new File(folder, fileName);

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();

                FileWriter fw = new FileWriter(configFile.getAbsoluteFile());
                try (BufferedWriter bw = new BufferedWriter(fw)) {
                    for (String line : yaml) {
                        bw.write(line + "\r\n");
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        this.fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Reloads the configuration, if the configuration isn't setup,
     * it will attempt to create the FileConfiguration object.
     *
     * If the file and folder object are null, an IllegalStateException will be thrown.
     *
     * @throws IllegalStateException
     */
    public void reloadConfig()
    {
        if (configFile == null) {
            if (folder == null) {
                throw new IllegalStateException();
            }

            configFile = new File(folder, fileName);
        }

        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

        // Look for defaults in the jar
        InputStream defConfigStream = plugin.getResource(fileName);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            fileConfiguration.setDefaults(defConfig);
        }
    }

    /**
     * Returns the file configuration, if the file
     * is null, it will attempt to create it.
     *
     * @return FileConfiguration
     */
    public FileConfiguration getFileConfiguration()
    {
        if (fileConfiguration == null) {
            this.reloadConfig();
        }

        return fileConfiguration;
    }

    /**
     * Saves the current state of the configuration to file.
     */
    public void saveConfig()
    {
        if (fileConfiguration == null || configFile == null) {
            return;
        }

        try {
            getFileConfiguration().save(configFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
    }

    /**
     * Saves the default configuration state to file.
     */
    public void saveDefaultConfig()
    {
        if (!configFile.exists()) {
            plugin.saveResource(fileName, false);
        }
    }

    @Override
    public void addDefault(String path, Object value)
    {
        getFileConfiguration().addDefault(path, value);
    }

    @Override
    public void set(String path, Object value)
    {
        getFileConfiguration().set(path, value);
    }

    @Override
    public boolean contains(String path)
    {
        return getFileConfiguration().contains(path);
    }

    @Override
    public ConfigurationSection createSection(String path)
    {
        return getFileConfiguration().createSection(path);
    }

    @Override
    public ConfigurationSection createSection(String path, Map<?, ?> map)
    {
        return getFileConfiguration().createSection(path, map);
    }

    @Override
    public String getName()
    {
        return fileName;
    }

    public String getFullName()
    {
        return folder.getPath() + File.separator + getName();
    }

    @Override
    public ConfigurationSection getParent()
    {
        return getFileConfiguration().getParent();
    }

    @Override
    public org.bukkit.configuration.Configuration getRoot()
    {
        return getFileConfiguration().getRoot();
    }

    @Override
    public ConfigurationSection getConfigurationSection(String path)
    {
        return getFileConfiguration().getConfigurationSection(path);
    }

    @Override
    public String getCurrentPath()
    {
        return getFileConfiguration().getCurrentPath();
    }

    @Override
    public ConfigurationSection getDefaultSection()
    {
        return getFileConfiguration().getDefaultSection();
    }

    @Override
    public Object get(String path)
    {
        return getFileConfiguration().get(path);
    }

    @Override
    public Object get(String path, Object def)
    {
        return getFileConfiguration().get(path, def);
    }

    @Override
    public boolean getBoolean(String path)
    {
        return getFileConfiguration().getBoolean(path);
    }

    @Override
    public boolean getBoolean(String path, boolean def)
    {
        return getFileConfiguration().getBoolean(path, def);
    }

    @Override
    public List<Boolean> getBooleanList(String path)
    {
        return getFileConfiguration().getBooleanList(path);
    }

    @Override
    public Color getColor(String path)
    {
        return getFileConfiguration().getColor(path);
    }

    @Override
    public Color getColor(String path, Color def)
    {
        return getFileConfiguration().getColor(path, def);
    }

    @Override
    public double getDouble(String path)
    {
        return getFileConfiguration().getDouble(path);
    }

    @Override
    public double getDouble(String path, double def)
    {
        return getFileConfiguration().getDouble(path, def);
    }

    @Override
    public List<Double> getDoubleList(String path)
    {
        return getFileConfiguration().getDoubleList(path);
    }

    @Override
    public int getInt(String path)
    {
        return getFileConfiguration().getInt(path);
    }

    @Override
    public int getInt(String path, int def)
    {
        return getFileConfiguration().getInt(path, def);
    }

    @Override
    public List<Integer> getIntegerList(String path)
    {
        return getFileConfiguration().getIntegerList(path);
    }

    @Override
    public ItemStack getItemStack(String path)
    {
        return getFileConfiguration().getItemStack(path);
    }

    @Override
    public ItemStack getItemStack(String path, ItemStack def)
    {
        return getFileConfiguration().getItemStack(path, def);
    }

    @Override
    public long getLong(String path)
    {
        return getFileConfiguration().getLong(path);
    }

    @Override
    public long getLong(String path, long def)
    {
        return getFileConfiguration().getLong(path, def);
    }

    @Override
    public List<Long> getLongList(String path)
    {
        return getFileConfiguration().getLongList(path);
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String path)
    {
        return getFileConfiguration().getOfflinePlayer(path);
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String path, OfflinePlayer def)
    {
        return getFileConfiguration().getOfflinePlayer(path, def);
    }

    @Override
    public String getString(String path)
    {
        return getFileConfiguration().getString(path);
    }

    @Override
    public String getString(String path, String def)
    {
        return getFileConfiguration().getString(path, def);
    }

    @Override
    public List<String> getStringList(String path)
    {
        return getFileConfiguration().getStringList(path);
    }

    @Override
    public Vector getVector(String path)
    {
        return getFileConfiguration().getVector(path);
    }

    @Override
    public Vector getVector(String path, Vector def)
    {
        return getFileConfiguration().getVector(path, def);
    }

    @Override
    public List<Float> getFloatList(String path)
    {
        return getFileConfiguration().getFloatList(path);
    }

    @Override
    public List<Short> getShortList(String path)
    {
        return getFileConfiguration().getShortList(path);
    }

    @Override
    public List<Character> getCharacterList(String path)
    {
        return getFileConfiguration().getCharacterList(path);
    }

    @Override
    public List<Byte> getByteList(String path)
    {
        return getFileConfiguration().getByteList(path);
    }

    @Override
    public List<?> getList(String path)
    {
        return getFileConfiguration().getList(path);
    }

    @Override
    public List<?> getList(String path, List<?> def)
    {
        return getFileConfiguration().getList(path, def);
    }

    @Override
    public List<Map<?, ?>> getMapList(String path)
    {
        return getFileConfiguration().getMapList(path);
    }

    @Override
    public Set<String> getKeys(boolean deep)
    {
        return getFileConfiguration().getKeys(deep);
    }

    @Override
    public Map<String, Object> getValues(boolean deep)
    {
        return getFileConfiguration().getValues(deep);
    }

    @Override
    public boolean isBoolean(String path)
    {
        return getFileConfiguration().isBoolean(path);
    }

    @Override
    public boolean isColor(String path)
    {
        return getFileConfiguration().isColor(path);
    }

    @Override
    public boolean isConfigurationSection(String path)
    {
        return getFileConfiguration().isConfigurationSection(path);
    }

    @Override
    public boolean isDouble(String path)
    {
        return getFileConfiguration().isDouble(path);
    }

    @Override
    public boolean isInt(String path)
    {
        return getFileConfiguration().isInt(path);
    }

    @Override
    public boolean isItemStack(String path)
    {
        return getFileConfiguration().isItemStack(path);
    }

    @Override
    public boolean isList(String path)
    {
        return getFileConfiguration().isList(path);
    }

    @Override
    public boolean isLong(String path)
    {
        return getFileConfiguration().isLong(path);
    }

    @Override
    public boolean isOfflinePlayer(String path)
    {
        return getFileConfiguration().isOfflinePlayer(path);
    }

    @Override
    public boolean isSet(String path)
    {
        return getFileConfiguration().isSet(path);
    }

    @Override
    public boolean isString(String path)
    {
        return getFileConfiguration().isString(path);
    }

    @Override
    public boolean isVector(String path)
    {
        return getFileConfiguration().isVector(path);
    }
}
