package com.senither.library.item;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder
{

    public ItemStack make(ItemStack item, String name)
    {
        if (name != null) {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(colorize(name));
            item.setItemMeta(meta);
        }

        return item;
    }

    public ItemStack make(ItemStack item, String name, List<String> lore)
    {
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(colorize(name));

        List<String> placeholder = new ArrayList<>();
        for (String line : lore) {
            placeholder.add(colorize(line));
        }
        meta.setLore(placeholder);

        item.setItemMeta(meta);
        return item;
    }

    public ItemStack make(Material material, String name)
    {
        ItemStack item = new ItemStack(material, 1);

        if (name != null) {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(colorize(name));
            item.setItemMeta(meta);
        }

        return item;
    }

    public ItemStack make(Material material, String name, List<String> lore)
    {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(colorize(name));

        List<String> placeholder = new ArrayList<>();
        for (String line : lore) {
            placeholder.add(colorize(line));
        }
        meta.setLore(placeholder);

        item.setItemMeta(meta);
        return item;
    }

    /**
     * Colorizes the string so it's colorful.
     *
     * @param str The string to color
     * @return The colored string
     */
    private String colorize(String str)
    {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
