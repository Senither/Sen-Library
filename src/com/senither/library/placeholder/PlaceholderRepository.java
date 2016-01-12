package com.senither.library.placeholder;

import com.senither.library.placeholder.contracts.Placeholder;
import com.senither.library.SenLibrary;
import com.senither.library.placeholder.contracts.GlobalPlaceholder;
import com.senither.library.placeholder.contracts.PlayerPlaceholder;
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

    public String formatPlayer(String str, Player player)
    {
        if (player == null) {
            return format(str);
        }

        for (PlaceholderContainer placeholder : placeholders.values()) {
            if (str.contains(placeholder.toString())) {
                String callback = null;

                switch (placeholder.getType()) {
                    case GLOBAL:
                        callback = ((GlobalPlaceholder) placeholder.getCallback()).run();
                        break;

                    case PLAYER:
                        callback = ((PlayerPlaceholder) placeholder.getCallback()).run(player);
                }

                if (callback != null) {
                    str = str.replace(placeholder.getPlaceholder(), callback);
                }
            }
        }

        return str;
    }

    public String format(String str)
    {
        for (PlaceholderContainer placeholder : placeholders.values()) {
            if (placeholder.getType().equals(PlaceholderType.PLAYER)) {
                continue;
            }

            if (str.contains(placeholder.toString())) {
                String callback = null;

                switch (placeholder.getType()) {
                    case GLOBAL:
                        str = ((GlobalPlaceholder) placeholder.getCallback()).run();
                        break;
                }

                if (callback != null) {
                    str = str.replace(placeholder.getPlaceholder(), callback);
                }
            }
        }

        return null;
    }
}
