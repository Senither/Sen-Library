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

    private final SenLibrary library;

    private String title;
    private final int size;
    private final HashMap<Integer, ItemStack> items;
    private final ItemParser builder;

    public InventoryBuilder(SenLibrary library, int rows)
    {
        this.library = library;

        rows = (rows < 1) ? 1 : rows;
        rows = (rows > 6) ? 6 : rows;

        size = rows * 9;

        this.title = library.getChat().colorize("&9{player}'s Inventory");

        items = new HashMap<>();
        builder = new ItemParser(library);
    }

    public InventoryBuilder(SenLibrary library, int rows, String title)
    {
        this.library = library;

        rows = (rows < 1) ? 1 : rows;
        rows = (rows > 6) ? 6 : rows;

        size = rows * 9;

        this.title = library.getChat().colorize(title);

        items = new HashMap<>();
        builder = new ItemParser(library);
    }

    public Inventory build()
    {
        Inventory inventory = Bukkit.createInventory(null, size, title);

        for (int i = 0; i < size; i++) {
            if (items.containsKey(i) && items.get(i) != null) {
                inventory.setItem(i, items.get(i));
            }
        }

        return inventory;
    }

    public Inventory build(Player player)
    {
        String inventoryTitle = library.getPlaceholder().formatPlayer(title, player);

        Inventory inventory = Bukkit.createInventory(null, size, inventoryTitle);

        for (int i = 0; i < size; i++) {
            if (items.containsKey(i) && items.get(i) != null) {
                inventory.setItem(i, items.get(i));
            }
        }

        return inventory;
    }

    public void open(Player player)
    {
        player.openInventory(build(player));
    }

    public InventoryBuilder setTitle(String title)
    {
        this.title = library.getChat().colorize(title);

        return this;
    }

    public InventoryBuilder unset(int index)
    {
        items.remove(index);

        return this;
    }

    public InventoryBuilder unset(int from, int to)
    {
        if (from < to) {
            for (int i = from; i <= to; i++) {
                unset(i);
            }
        } else {
            for (int i = to; i <= from; i++) {
                unset(i);
            }
        }
        return this;
    }

    public InventoryBuilder createLine(int row, ItemStack item)
    {
        for (int i = row * 9; i < (9 + (row * 9)); i++) {
            items.put(i, item);
        }
        return this;
    }

    public InventoryBuilder createLine(int row, ItemStack item, String name)
    {
        return createLine(row, builder.make(item, name));
    }

    public InventoryBuilder createLine(int row, ItemStack item, String name, List<String> lore)
    {
        return createLine(row, builder.make(item, name, lore));
    }

    public InventoryBuilder createLine(int row, Material material, String name)
    {
        return createLine(row, builder.make(material, name));
    }

    public InventoryBuilder createLine(int row, Material material, String name, List<String> lore)
    {
        return createLine(row, builder.make(material, name, lore));
    }

    public InventoryBuilder createWall(WallSide side, ItemStack item)
    {
        int index = -1;
        int multiplier = -1;
        int timers = -1;

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

    public InventoryBuilder createWall(WallSide side, ItemStack item, String name)
    {
        return createWall(side, builder.make(item, name));
    }

    public InventoryBuilder createWall(WallSide side, ItemStack item, String name, List<String> lore)
    {
        return createWall(side, builder.make(item, name, lore));
    }

    public InventoryBuilder createWall(WallSide side, Material material, String name)
    {
        return createWall(side, builder.make(material, name));
    }

    public InventoryBuilder createWall(WallSide side, Material material, String name, List<String> lore)
    {
        return createWall(side, builder.make(material, name, lore));
    }

    public InventoryBuilder fill(ItemStack item)
    {
        for (int i = 0; i < (size / 9); i++) {
            createLine(i, item);
        }
        return this;
    }

    public InventoryBuilder fill(ItemStack item, String name)
    {
        return fill(builder.make(item, name));
    }

    public InventoryBuilder fill(ItemStack item, String name, List<String> lore)
    {
        return fill(builder.make(item, name, lore));
    }
}
