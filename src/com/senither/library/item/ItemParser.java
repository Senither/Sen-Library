package com.senither.library.item;

import com.senither.library.SenLibrary;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemParser
{

    private final SenLibrary library;

    public ItemParser(SenLibrary library)
    {
        this.library = library;
    }

    /**
     * Gives the ItemStack object the given display name.
     *
     * @param item The item that should be edited.
     * @param name The name the item should have.
     * @return
     */
    public ItemStack make(ItemStack item, String name)
    {
        if (name != null) {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(library.getChat().colorize(name));
            item.setItemMeta(meta);
        }

        return item;
    }

    /**
     * Gives the ItemStack object the given display name and lore.
     *
     * @param item The item that should be edited.
     * @param name The name the item should have.
     * @param lore The list of messages that should be used for the lore.
     * @return
     */
    public ItemStack make(ItemStack item, String name, List<String> lore)
    {
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(library.getChat().colorize(name));

        List<String> placeholder = new ArrayList<>();
        lore.stream().forEach((line) -> {
            placeholder.add(library.getChat().colorize(line));
        });
        meta.setLore(placeholder);

        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates a new ItemStack object with the given name.
     *
     * @param material The item material that should be used.
     * @param name     The name of the item that should be created.
     * @return
     */
    public ItemStack make(Material material, String name)
    {
        ItemStack item = new ItemStack(material, 1);

        return make(item, name);
    }

    /**
     * Creates a new ItemStack object with the given name.
     *
     * @param material The item material that should be used.
     * @param data     The item data(damage) that should be parsed.
     * @param name     The name of the item that should be created.
     * @return
     */
    public ItemStack make(Material material, short data, String name)
    {
        ItemStack item = new ItemStack(material, 1, data);

        return make(item, name);
    }

    /**
     *
     * Creates a new ItemStack object with the given name and lore.
     *
     * @param material The item material that should be used.
     * @param name     The name the item should have.
     * @param lore     The list of messages that should be used for the lore.
     * @return
     */
    public ItemStack make(Material material, String name, List<String> lore)
    {
        ItemStack item = new ItemStack(material, 1);

        return make(item, name, lore);
    }

    /**
     * Creates a new ItemStack object with the given name and lore.
     *
     * @param material The item material that should be used.
     * @param data     The item data(damage) that should be parsed.
     * @param name     The name the item should have.
     * @param lore     The list of messages that should be used for the lore.
     * @return
     */
    public ItemStack make(Material material, short data, String name, List<String> lore)
    {
        ItemStack item = new ItemStack(material, 1, data);

        return make(item, name, lore);
    }
}
