package com.senither.library.placeholder;

import com.senither.library.placeholder.contracts.Placeholder;
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
     * Represents our placeholder string key.
     *
     * @var String placeholder
     */
    private final String placeholder;

    /**
     * Represents our placeholder callback, this will be
     * called on the update method, the value of the
     * callback will be used as the value for the
     * formating method to format messages.
     *
     * @var Placeholder callback
     */
    private final Placeholder callback;

    /**
     * Represents our placeholder type, this will determine
     * the run method that is called for the placeholder
     * when the placeholder is invoked.
     *
     * @var PlaceholderType
     */
    private final PlaceholderType type;

    public PlaceholderContainer(Plugin plugin, String placeholder, Placeholder callback)
    {
        this.plugin = plugin;
        this.placeholder = "{" + placeholder + "}";
        this.callback = callback;

        this.type = PlaceholderType.fromInstance(callback);
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
     * Returns the placeholders type.
     *
     * @return PlaceholderType
     */
    public PlaceholderType getType()
    {
        return type;
    }

    @Override
    public String toString()
    {
        return getPlaceholder();
    }
}
