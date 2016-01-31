package com.senither.library.item;

import com.senither.library.SenLibrary;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemParser
{

    /**
     * Represents our Sen Library instance, this is
     * used to call other parts of the library.
     *
     * @var SenLibrary
     */
    private final SenLibrary library;

    /**
     * Creates a new Item Parser instance.
     *
     * @param library The sen-library instance.
     */
    public ItemParser(SenLibrary library)
    {
        this.library = library;
    }

    /**
     * Gives the ItemStack object the given display name.
     *
     * @param item The item that should be edited.
     * @param name The name the item should have.
     * @return ItemStack
     */
    public ItemStack parse(ItemStack item, String name)
    {
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(library.getChatFormatter().colorize(name));

        item.setItemMeta(meta);

        return item;
    }

    /**
     * Gives the ItemStack object the given display name.
     *
     * @param item         The item that should be edited.
     * @param name         The name the item should have.
     * @param enchantments The list of enchants that should be applied to the ItemStack.
     * @return ItemStack
     */
    public ItemStack parse(ItemStack item, String name, HashMap<Enchantment, Integer> enchantments)
    {
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(library.getChatFormatter().colorize(name));

        item.setItemMeta(meta);

        item.addUnsafeEnchantments(enchantments);

        return item;
    }

    /**
     * Gives the ItemStack object the given display name and lore.
     *
     * @param item The item that should be edited.
     * @param name The name the item should have.
     * @param lore The list of messages that should be used for the lore.
     * @return ItemStack
     */
    public ItemStack parse(ItemStack item, String name, List<String> lore)
    {
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(library.getChatFormatter().colorize(name));
        meta.setLore(library.getChatFormatter().colorize(lore));

        item.setItemMeta(meta);

        return item;
    }

    /**
     * Gives the ItemStack object the given display name and lore.
     *
     * @param item         The item that should be edited.
     * @param name         The name the item should have.
     * @param lore         The list of messages that should be used for the lore.
     * @param enchantments The list of enchants that should be applied to the ItemStack.
     * @return ItemStack
     */
    public ItemStack parse(ItemStack item, String name, List<String> lore, HashMap<Enchantment, Integer> enchantments)
    {
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(library.getChatFormatter().colorize(name));
        meta.setLore(library.getChatFormatter().colorize(lore));

        item.setItemMeta(meta);

        item.addUnsafeEnchantments(enchantments);

        return item;
    }

    /**
     * Creates a new ItemStack object with the given name.
     *
     * @param material The item material that should be used.
     * @param name     The name of the item that should be created.
     * @return ItemStack
     */
    public ItemStack make(Material material, String name)
    {
        return parse(new ItemStack(material, 1), name);
    }

    /**
     * Creates a new ItemStack object with the given name.
     *
     * @param material     The item material that should be used.
     * @param name         The name of the item that should be created.
     * @param enchantments The list of enchants that should be applied to the ItemStack.
     * @return ItemStack
     */
    public ItemStack make(Material material, String name, HashMap<Enchantment, Integer> enchantments)
    {
        return parse(new ItemStack(material, 1), name, enchantments);
    }

    /**
     * Creates a new ItemStack object with the given name.
     *
     * @param material The item material that should be used.
     * @param data     The item data(damage) that should be parsed.
     * @param name     The name of the item that should be created.
     * @return ItemStack
     */
    public ItemStack make(Material material, short data, String name)
    {
        return parse(new ItemStack(material, 1, data), name);
    }

    /**
     * Creates a new ItemStack object with the given name.
     *
     * @param material     The item material that should be used.
     * @param data         The item data(damage) that should be parsed.
     * @param name         The name of the item that should be created.
     * @param enchantments The list of enchants that should be applied to the ItemStack.
     * @return ItemStack
     */
    public ItemStack make(Material material, short data, String name, HashMap<Enchantment, Integer> enchantments)
    {
        return parse(new ItemStack(material, 1, data), name);
    }

    /**
     *
     * Creates a new ItemStack object with the given name and lore.
     *
     * @param material The item material that should be used.
     * @param name     The name the item should have.
     * @param lore     The list of messages that should be used for the lore.
     * @return ItemStack
     */
    public ItemStack make(Material material, String name, List<String> lore)
    {
        return parse(new ItemStack(material, 1), name, lore);
    }

    /**
     *
     * Creates a new ItemStack object with the given name and lore.
     *
     * @param material     The item material that should be used.
     * @param name         The name the item should have.
     * @param lore         The list of messages that should be used for the lore.
     * @param enchantments The list of enchants that should be applied to the ItemStack.
     * @return ItemStack
     */
    public ItemStack make(Material material, String name, List<String> lore, HashMap<Enchantment, Integer> enchantments)
    {
        return parse(new ItemStack(material, 1), name, lore);
    }

    /**
     * Creates a new ItemStack object with the given name and lore.
     *
     * @param material The item material that should be used.
     * @param data     The item data(damage) that should be parsed.
     * @param name     The name the item should have.
     * @param lore     The list of messages that should be used for the lore.
     * @return ItemStack
     */
    public ItemStack make(Material material, short data, String name, List<String> lore)
    {
        return parse(new ItemStack(material, 1, data), name, lore);
    }

    /**
     * Creates a new ItemStack object with the given name and lore.
     *
     * @param material     The item material that should be used.
     * @param data         The item data(damage) that should be parsed.
     * @param name         The name the item should have.
     * @param lore         The list of messages that should be used for the lore.
     * @param enchantments The list of enchants that should be applied to the ItemStack.
     * @return ItemStack
     */
    public ItemStack make(Material material, short data, String name, List<String> lore, HashMap<Enchantment, Integer> enchantments)
    {
        return parse(new ItemStack(material, 1, data), name, lore);
    }
}
