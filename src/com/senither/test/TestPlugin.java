package com.senither.test;

import com.senither.library.SenLibrary;
import com.senither.library.config.Configuration;
import com.senither.library.inventory.InventoryBuilder;
import com.senither.library.inventory.WallSide;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin implements Listener
{

    private SenLibrary library;

    @Override
    public void onEnable()
    {
        library = new SenLibrary(this);

        Configuration config = library.makeConfig("test.yml");
        config.set("this.is.a.test", true);
        config.saveConfig();

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e)
    {
        Player player = e.getPlayer();

        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);

        InventoryBuilder inventory = library.makeInventory(3, WallSide.ALL, item, "&8Filler item");

        inventory.open(player);
    }

}
