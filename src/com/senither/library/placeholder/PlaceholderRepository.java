package com.senither.library.placeholder;

import com.senither.library.SenLibrary;
import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlaceholderRepository
{

    private final SenLibrary library;
    private final HashMap<String, PlaceholderContainer> placeholders;

    public PlaceholderRepository(SenLibrary library)
    {
        this.library = library;

        this.placeholders = new HashMap<>();
    }

    public boolean push(Plugin plugin, String placeholder, Placeholder callback)
    {
        placeholder = placeholder.trim();

        if (placeholders.containsKey(placeholder)) {
            return false;
        }

        placeholders.put(placeholder, new PlaceholderContainer(plugin, placeholder, callback));

        return true;
    }

    public String format(String str, Player player)
    {
        for (PlaceholderContainer placeholder : placeholders.values()) {
            if (str.contains(placeholder.toString())) {
                str = str.replace(placeholder.toString(), placeholder.invokeCallback(player));
            }
        }

        return str;
    }
}
