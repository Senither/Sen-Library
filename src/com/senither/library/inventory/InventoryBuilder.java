package com.senither.library.inventory;

import com.senither.library.SenLibrary;
import com.senither.library.item.ItemParser;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryBuilder
{

    /**
     * Represents our Sen Library instance, this is
     * used to call other parts of the library.
     *
     * @var SenLibrary
     */
    private final SenLibrary library;

    /**
     * The inventory title, when the inventory is build the title
     * will be ran through the placeholder repository to
     * replace any placeholders in the title.
     *
     * @var String
     */
    private String title;

    /**
     * The size of the inventory, this determines how
     * many item slots the inventory will have.
     *
     * @var Integer
     */
    private final int size;

    /**
     * The list of items to use when building the inventory.
     *
     * @var HashMap
     */
    private final HashMap<Integer, ItemStack> items;

    /**
     * The item parser, used for creating or adding
     * items to the inventory a lot easier.
     *
     * @var ItemParser
     */
    private final ItemParser parser;

    /**
     * Creates a new inventory builder instance.
     *
     * @param library The sen library instance.
     * @param rows    The The amount of rows the inventory should have.
     */
    public InventoryBuilder(SenLibrary library, int rows)
    {
        this.library = library;

        rows = (rows < 1) ? 1 : rows;
        rows = (rows > 6) ? 6 : rows;

        size = rows * 9;

        title = library.getChatFormatter().colorize("&9{player}'s Inventory");

        items = new HashMap<>();
        parser = library.getItemParser();
    }

    /**
     * Creates a new inventory builder instance.
     *
     * @param library The sen library instance.
     * @param rows    The amount of rows the inventory should have.
     * @param title   The title the inventory should have.
     */
    public InventoryBuilder(SenLibrary library, int rows, String title)
    {
        this.library = library;

        rows = (rows < 1) ? 1 : rows;
        rows = (rows > 6) ? 6 : rows;

        size = rows * 9;

        this.title = library.getChatFormatter().colorize(title);

        items = new HashMap<>();
        parser = new ItemParser(library);
    }

    /**
     * Sets the title of the inventory.
     *
     * @param title The title that should be used.
     * @return InventoryBuilder
     */
    public InventoryBuilder setTitle(String title)
    {
        this.title = library.getChatFormatter().colorize(title);

        return this;
    }

    /**
     * Unsets the given item index from the inventory.
     *
     * @param index The index to unset.
     * @return InventoryBuilder
     */
    public InventoryBuilder unset(int index)
    {
        items.remove(index);

        return this;
    }

    /**
     * Unsets every index from and to the given indexs.
     *
     * @param from Where to start from.
     * @param to   Where to end at.
     * @return InventoryBuilder
     */
    public InventoryBuilder unset(int from, int to)
    {
        int min = Math.min(from, to), max = Math.max(from, to);

        for (int i = min; i <= max; i++) {
            unset(i);
        }

        return this;
    }

    /**
     * Creates a line of the given item stack.
     *
     * @param row  The row to write to.
     * @param item The item stack to write.
     * @return inventoryBuilder
     */
    public InventoryBuilder createLine(int row, ItemStack item)
    {
        for (int i = row * 9; i < (9 + (row * 9)); i++) {
            items.put(i, item);
        }

        return this;
    }

    /**
     * Creates a line of the given item stack, the item
     * stack will be modified to have the given name.
     *
     * @param row  The row to write to.
     * @param item The item stack to write.
     * @param name The name to give the item stack.
     * @return InventoryBuilder
     */
    public InventoryBuilder createLine(int row, ItemStack item, String name)
    {
        return createLine(row, parser.parse(item, name));
    }

    /**
     * Creates a line of the given item stack, the item stack
     * will be modified to have the given name and lore.
     *
     * @param row  The row to write to.
     * @param item The item stack to write.
     * @param name The name to given the item stack.
     * @param lore The lore to give the item stack.
     * @return InventoryBuilder
     */
    public InventoryBuilder createLine(int row, ItemStack item, String name, List<String> lore)
    {
        return createLine(row, parser.parse(item, name, lore));
    }

    /**
     * Creates an item stack from the given material and name.
     *
     * @param row      The row to write to.
     * @param material The material to use to create the item stack.
     * @param name     The name to give the item stack.
     * @return InventoryBuilder
     */
    public InventoryBuilder createLine(int row, Material material, String name)
    {
        return createLine(row, parser.make(material, name));
    }

    /**
     * Creates an item stack from the given material, name and lore.
     *
     * @param row      The row to write to.
     * @param material The material to use to create the item stack.
     * @param name     The name to give the item stack.
     * @param lore     The lore to give the item stack.
     * @return InventoryBuilder
     */
    public InventoryBuilder createLine(int row, Material material, String name, List<String> lore)
    {
        return createLine(row, parser.make(material, name, lore));
    }

    /**
     * Creates a wall around the outside of the inventory with the given item stack.
     *
     * @param side The side to write to.
     * @param item The item stack to write to the side.
     * @return InventoryBuilder
     */
    public InventoryBuilder createWall(WallSide side, ItemStack item)
    {
        int index = -1, multiplier = -1, timers = -1;

        switch (side) {
            case ALL:
                createWall(WallSide.TOP, item);
                createWall(WallSide.RIGHT, item);
                createWall(WallSide.BOTTOM, item);
                createWall(WallSide.LEFT, item);
                break;

            case TOP:
                index = 0;
                multiplier = 1;
                timers = 9;
                break;

            case RIGHT:
                index = 8;
                multiplier = 9;
                timers = 6;
                break;

            case BOTTOM:
                index = size - 8;
                multiplier = 1;
                timers = 9;
                break;

            case LEFT:
                index = 0;
                multiplier = 9;
                timers = 6;
                break;
        }

        if (index == -1 || multiplier == -1 || timers == -1) {
            return this;
        }

        for (int i = 0; i < timers; i++) {
            items.put(index, item);
            index += multiplier;
        }

        return this;
    }

    /**
     * Creates a wall around the outside of the inventory with the given item stack.
     *
     * @param side The side to write to.
     * @param item The item stack to write to the side.
     * @param name The name to give the item stack.
     * @return InventoryBuilder
     */
    public InventoryBuilder createWall(WallSide side, ItemStack item, String name)
    {
        return createWall(side, parser.parse(item, name));
    }

    /**
     * Creates a wall around the outside of the inventory with the given item stack.
     *
     * @param side The side to write to.
     * @param item The item stack to write to the side.
     * @param name The name to give the item stack.
     * @param lore The lore to give the item stack.
     * @return InventoryBuilder
     */
    public InventoryBuilder createWall(WallSide side, ItemStack item, String name, List<String> lore)
    {
        return createWall(side, parser.parse(item, name, lore));
    }

    /**
     * Creates a wall around the outside of the inventory with the given material.
     *
     * @param side     The side to write to.
     * @param material The material to use to create the item stack.
     * @param name     The name to give the item stack.
     * @return InventoryBuilder
     */
    public InventoryBuilder createWall(WallSide side, Material material, String name)
    {
        return createWall(side, parser.make(material, name));
    }

    /**
     * Creates a wall around the outside of the inventory with the given material.
     *
     * @param side     The side to write to.
     * @param material The material to use to create the item stack.
     * @param name     The name to give the item stack.
     * @param lore     The lore to give the item stack.
     * @return InventoryBuilder
     */
    public InventoryBuilder createWall(WallSide side, Material material, String name, List<String> lore)
    {
        return createWall(side, parser.make(material, name, lore));
    }

    /**
     * Fills the entire inventory with the given item stack.
     *
     * @param item The item stack to add.
     * @return InventoryBuilder
     */
    public InventoryBuilder fill(ItemStack item)
    {
        for (int i = 0; i < (size / 9); i++) {
            createLine(i, item);
        }

        return this;
    }

    /**
     * Fills the entire inventory with the given item stack.
     *
     * @param item The item stack to add.
     * @param name The name to give the item stack.
     * @return InventoryBuilder
     */
    public InventoryBuilder fill(ItemStack item, String name)
    {
        return fill(parser.parse(item, name));
    }

    /**
     * Fills the entire inventory with the given item stack.
     *
     * @param item The item stack to add.
     * @param name The name to give the item stack.
     * @param lore The lore to give the item stack.
     * @return InventoryBuilder
     */
    public InventoryBuilder fill(ItemStack item, String name, List<String> lore)
    {
        return fill(parser.parse(item, name, lore));
    }

    /**
     * Builds and opens the inventory for the given player.
     *
     * @param player The player to open the inventory for.
     */
    public void open(Player player)
    {
        player.openInventory(build(player));
    }

    /**
     * Builds the inventory with non-player placeholders replacements.
     *
     * @return Inventory
     */
    public Inventory build()
    {
        library.getLogger().info("InventoryBuilder - Building inventory: " + toString());

        String inventoryTitle = library.getPlaceholder().format(title);

        return buildInventory(inventoryTitle);
    }

    /**
     * Builds the inventory with all the placeholder replacements.
     *
     * @param player The player to build the inventory for.
     * @return Inventory
     */
    public Inventory build(Player player)
    {
        library.getLogger().info("InventoryBuilder - Building inventory for " + player.getName() + ": " + toString());

        String inventoryTitle = library.getPlaceholder().format(title, player);

        return buildInventory(inventoryTitle);
    }

    /**
     * Builds the inventory with the given title.
     *
     * @param name The inventory title
     * @return Inventory
     */
    private Inventory buildInventory(String name)
    {
        Inventory inventory = Bukkit.createInventory(null, size, name);

        for (int i = 0; i < size; i++) {
            if (items.containsKey(i) && items.get(i) != null) {
                inventory.setItem(i, items.get(i));
            }
        }

        return inventory;
    }

    @Override
    public String toString()
    {
        return String.format("[title=%s, slots=%d, items=%d]", title, size, items.size());
    }
}
