package com.senither.test;

import com.senither.library.SenLibrary;
import com.senither.library.chat.ChatFilterType;
import com.senither.library.config.Configuration;
import com.senither.library.inventory.InventoryBuilder;
import com.senither.library.inventory.WallSide;
import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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

        library.getChatFilter().addWords(Arrays.asList("fuck", "cunt", "ass", "asshole", "nigger"));

        Configuration config = library.makeConfig("test.yml");
        config.set("this.is.a.test", true);
        config.saveConfig();

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You can't use this command on the console!");
            return false;
        }

        Player player = (Player) sender;

        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);

        InventoryBuilder inventory = library.makeInventory(3, WallSide.ALL, item, "&8Filler item");

        inventory.open(player);

        return true;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e)
    {
        e.setMessage(library.getChatFilter().runFilter(ChatFilterType.ALL, e.getMessage()));
    }
}
