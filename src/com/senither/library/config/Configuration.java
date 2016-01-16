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

public final class Configuration
{

    private final JavaPlugin plugin;
    private final String fileName;
    private File folder;
    private File configFile;
    private FileConfiguration fileConfiguration;

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

    public FileConfiguration getFileConfiguration()
    {
        if (fileConfiguration == null) {
            this.reloadConfig();
        }

        return fileConfiguration;
    }

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

    public void saveDefaultConfig()
    {
        if (!configFile.exists()) {
            plugin.saveResource(fileName, false);
        }
    }

    public void set(String path, Object value)
    {
        getFileConfiguration().set(path, value);
    }

    public boolean has(String path)
    {
        return getFileConfiguration().contains(path);
    }

    public Object get(String path)
    {
        return get(path, null);
    }

    public Object get(String path, Object def)
    {
        return getFileConfiguration().get(path, def);
    }

    public boolean getBoolean(String path)
    {
        return getBoolean(path, false);
    }

    public boolean getBoolean(String path, boolean def)
    {
        return getFileConfiguration().getBoolean(path, def);
    }

    public List<Boolean> getBooleanList(String path)
    {
        return getFileConfiguration().getBooleanList(path);
    }

    public List<Byte> getByteList(String path)
    {
        return getFileConfiguration().getByteList(path);
    }

    public List<Character> getCharacterList(String path)
    {
        return getFileConfiguration().getCharacterList(path);
    }

    public Color getColor(String path)
    {
        return getColor(path, Color.WHITE);
    }

    public Color getColor(String path, Color def)
    {
        return getFileConfiguration().getColor(path, def);
    }

    public ConfigurationSection getConfigurationSection(String path)
    {
        return getFileConfiguration().getConfigurationSection(path);
    }

    public ConfigurationSection getDefaultSection()
    {
        return getFileConfiguration().getDefaultSection();
    }

    public double getDouble(String path)
    {
        return getDouble(path, 0.0D);
    }

    public double getDouble(String path, double def)
    {
        return getFileConfiguration().getDouble(path, def);
    }

    public List<Double> getDoubleList(String path)
    {
        return getFileConfiguration().getDoubleList(path);
    }

    public List<Float> getFloatList(String path)
    {
        return getFileConfiguration().getFloatList(path);
    }

    public int getInt(String path)
    {
        return getInt(path, 0);
    }

    public int getInt(String path, int def)
    {
        return getFileConfiguration().getInt(path);
    }

    public List<Integer> getIntgerList(String path)
    {
        return getFileConfiguration().getIntegerList(path);
    }

    public ItemStack getItemStack(String path)
    {
        return getItemStack(path, null);
    }

    public ItemStack getItemStack(String path, ItemStack def)
    {
        return getFileConfiguration().getItemStack(path, def);
    }

    public Set<String> getKeys(boolean deep)
    {
        return getFileConfiguration().getKeys(deep);
    }

    public List<?> getList(String path)
    {
        return getFileConfiguration().getList(path);
    }

    public List<?> getList(String path, List<?> def)
    {
        return getFileConfiguration().getList(path, def);
    }

    public long getLong(String path)
    {
        return getLong(path, 0L);
    }

    public long getLong(String path, long def)
    {
        return getFileConfiguration().getLong(path, def);
    }

    public List<Long> getLongList(String path)
    {
        return getFileConfiguration().getLongList(path);
    }

    public List<Map<?, ?>> getMapList(String path)
    {
        return getFileConfiguration().getMapList(path);
    }

    public OfflinePlayer getOfflinePlayer(String path)
    {
        return getOfflinePlayer(path, null);
    }

    public OfflinePlayer getOfflinePlayer(String path, OfflinePlayer def)
    {
        return getFileConfiguration().getOfflinePlayer(path, def);
    }

    public List<Short> getShortList(String path)
    {
        return getFileConfiguration().getShortList(path);
    }

    public String getString(String path)
    {
        return getString(path, null);
    }

    public String getString(String path, String def)
    {
        return getFileConfiguration().getString(path, def);
    }

    public List<String> getStringList(String path)
    {
        return getFileConfiguration().getStringList(path);
    }

    public Map<String, Object> getValues(boolean deep)
    {
        return getFileConfiguration().getValues(deep);
    }

    public Vector getVector(String path)
    {
        return getVector(path, null);
    }

    public Vector getVector(String path, Vector def)
    {
        return getFileConfiguration().getVector(path, def);
    }

    public boolean isBoolean(String path)
    {
        return getFileConfiguration().isBoolean(path);
    }

    public boolean isColor(String path)
    {
        return getFileConfiguration().isColor(path);
    }

    public boolean isConfigurationSection(String path)
    {
        return getFileConfiguration().isConfigurationSection(path);
    }

    public boolean isDouble(String path)
    {
        return getFileConfiguration().isDouble(path);
    }

    public boolean isInt(String path)
    {
        return getFileConfiguration().isInt(path);
    }

    public boolean isItemStack(String path)
    {
        return getFileConfiguration().isItemStack(path);
    }

    public boolean isList(String path)
    {
        return getFileConfiguration().isList(path);
    }

    public boolean isLong(String path)
    {
        return getFileConfiguration().isLong(path);
    }

    public boolean isOfflinePlayer(String path)
    {
        return getFileConfiguration().isOfflinePlayer(path);
    }

    public boolean isSet(String path)
    {
        return getFileConfiguration().isSet(path);
    }

    public boolean isString(String path)
    {
        return getFileConfiguration().isString(path);
    }

    public boolean isVector(String path)
    {
        return getFileConfiguration().isVector(path);
    }
}
