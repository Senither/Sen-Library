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

    public ItemStack make(ItemStack item, String name)
    {
        if (name != null) {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(library.getChat().colorize(name));
            item.setItemMeta(meta);
        }

        return item;
    }

    public ItemStack make(ItemStack item, String name, List<String> lore)
    {
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(library.getChat().colorize(name));

        List<String> placeholder = new ArrayList<>();
        for (String line : lore) {
            placeholder.add(library.getChat().colorize(line));
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
            meta.setDisplayName(library.getChat().colorize(name));
            item.setItemMeta(meta);
        }

        return item;
    }

    public ItemStack make(Material material, String name, List<String> lore)
    {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(library.getChat().colorize(name));

        List<String> placeholder = new ArrayList<>();
        for (String line : lore) {
            placeholder.add(library.getChat().colorize(line));
        }
        meta.setLore(placeholder);

        item.setItemMeta(meta);
        return item;
    }
}
