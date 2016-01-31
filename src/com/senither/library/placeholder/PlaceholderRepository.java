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
     * @var Pattern
     */
    private final Pattern pattern;

    /**
     * Creates a new placeholder repository instance.
     *
     * @param library The sen-library instance.
     */
    public PlaceholderRepository(SenLibrary library)
    {
        this.library = library;

        placeholders = new HashMap<>();
        pattern = Pattern.compile("(\\{)+[A-Za-z-]+(\\})");
    }

    /**
     * Pushes a new placeholder into the placeholder repository, if
     * the placeholder doesn't follow the placeholder format an
     * InvalidPlaceholderException will be thrown.
     *
     * The regex placeholder format is: (A-Za-z-)\w+
     *
     * @param plugin      The instance of the plugin registering the placeholder.
     * @param placeholder The placeholder string value.
     * @param callback    The placeholder callback method.
     * @return Boolean
     * @throws InvalidPlaceholderException
     */
    public boolean push(Plugin plugin, String placeholder, Placeholder callback) throws InvalidPlaceholderException
    {
        if (!placeholder.matches("([A-Za-z-])\\w+")) {
            throw new InvalidPlaceholderException("Invalid placeholder given, placeholders can only contain letters and dashes(-)");
        }

        if (placeholders.containsKey(placeholder)) {
            return false;
        }

        library.info("PlaceholderRepository - Added a new placeholder: " + String.format("[name=%s]", placeholder));

        placeholders.put(placeholder, new PlaceholderContainer(plugin, placeholder, callback));

        return true;
    }

    /**
     * Formats the given string from any placeholders, the given
     * player will be used to format the player placeholders.
     *
     * @param str    The string to format.
     * @param player The player to use to format the player placeholders.
     * @return String
     */
    public String format(String str, Player player)
    {
        if (player == null) {
            return format(str);
        }

        if (!hasPlaceholder(str)) {
            return str;
        }

        library.info("PlaceholderRepository - Player formating called on: " + String.format("[string=%s, player=%s]", str, player.getName()));

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

    /**
     * Formats the given string from any placeholders.
     *
     * @param str The string to format.
     * @return String
     */
    public String format(String str)
    {
        if (!hasPlaceholder(str)) {
            return str;
        }

        library.info("PlaceholderRepository - Normal formating called on: " + String.format("[string=%s]", str));

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

    /**
     * Checks the string to see if it contains any placeholders.
     *
     * @param str The string to check
     * @return Boolean
     */
    private boolean hasPlaceholder(String str)
    {
        return pattern.matcher(str).find();
    }
}
