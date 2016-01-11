package com.senither.library.placeholder;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlaceholderContainer
{

    /**
     * Represents our plugin instance that the
     * placeholder was registered to.
     *
     * @var Plugin plugin
     */
    private final Plugin plugin;

    /**
     * Represents our placeholder string value, this
     * will be used as a key in the network packet
     * that will be sent to the lobbies.
     *
     * @var String placeholder
     */
    private final String placeholder;

    /**
     * Represents our placeholder value, this will be
     * called on the update method, the value of
     * callback will be used as the value for
     * the network packet that will be sent
     * to the lobbies.
     *
     * @var PlaceholderDataset callback
     */
    private final Placeholder callback;

    public PlaceholderContainer(Plugin plugin, String placeholder, Placeholder callback)
    {
        this.plugin = plugin;
        this.placeholder = "{" + placeholder + "}";
        this.callback = callback;
    }

    /**
     * Gets the registered plugin.
     *
     * @return Plugin
     */
    public Plugin getPlugin()
    {
        return plugin;
    }

    /**
     * Gets the placeholder value.
     *
     * @return String
     */
    public String getPlaceholder()
    {
        return placeholder;
    }

    /**
     * Gets the callback value.
     *
     * @return Placeholder
     */
    public Placeholder getCallback()
    {
        return callback;
    }

    /**
     * Invokes the callback method if the
     * registered plugin is enabled.
     *
     * @param player
     * @return String
     */
    public String invokeCallback(Player player)
    {
        if (plugin != null && plugin.isEnabled()) {
            return callback.run(player);
        }
        return null;
    }

    @Override
    public String toString()
    {
        return getPlaceholder();
    }
}
