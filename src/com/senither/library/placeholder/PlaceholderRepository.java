package com.senither.library.placeholder;

import com.senither.library.placeholder.contracts.Placeholder;
import com.senither.library.SenLibrary;
import com.senither.library.exceptions.InvalidPlaceholderException;
import com.senither.library.placeholder.contracts.GlobalPlaceholder;
import com.senither.library.placeholder.contracts.PlayerPlaceholder;
import java.util.HashMap;
import java.util.regex.Pattern;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlaceholderRepository
{

    /**
     * Represents our Sen Library instance, this is
     * used to call other parts of the library.
     *
     * @var SenLibrary
     */
    private final SenLibrary library;

    /**
     * Represents our list of placeholders, the placeholder
     * itself will act as the key and the callback, type
     * and any other relevant information will be
     * stored in the Placeholder Container.
     *
     * @var HashMap
     */
    private final HashMap<String, PlaceholderContainer> placeholders;

    /**
     * Represents our regex placeholder pattern, this is used to
     * check if a string has a placeholder in it before running
     * and calling all the callbacks and formatters.
     *
     */
    private final Pattern pattern;

    public PlaceholderRepository(SenLibrary library)
    {
        this.library = library;

        this.placeholders = new HashMap<>();
        this.pattern = Pattern.compile("(\\{)+[A-Za-z-]+(\\})");
    }

    public boolean push(Plugin plugin, String placeholder, Placeholder callback) throws InvalidPlaceholderException
    {
        if (!placeholder.matches("([A-Za-z-])\\w+")) {
            throw new InvalidPlaceholderException("Invalid placeholder given, placeholders can only contain letters and dashes(-)");
        }

        if (placeholders.containsKey(placeholder)) {
            return false;
        }

        placeholders.put(placeholder, new PlaceholderContainer(plugin, placeholder, callback));

        return true;
    }

    public String format(String str, Player player)
    {
        if (player == null) {
            return format(str);
        }

        if (!hasPlaceholder(str)) {
            return str;
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
        if (!hasPlaceholder(str)) {
            return str;
        }

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

        return str;
    }

    private boolean hasPlaceholder(String str)
    {
        return pattern.matcher(str).find();
    }
}
