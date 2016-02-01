package com.senither.test;

import com.senither.library.SenLibrary;
import com.senither.library.chat.ChatFilterType;
import com.senither.library.config.Configuration;
import com.senither.library.database.eloquent.DataRow;
import com.senither.library.database.utils.QueryBuilder;
import com.senither.library.inventory.InventoryBuilder;
import com.senither.library.inventory.WallSide;
import com.senither.library.scoreboard.ScoreboardHandler;
import com.senither.library.scoreboard.ScoreboardPage;
import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin implements Listener
{

    private SenLibrary library;

    @Override
    public void onEnable()
    {
        library = new SenLibrary(this, true);

        library.getChatFilter().addWords(Arrays.asList("fuck", "cunt", "ass", "asshole", "nigger"));

        Configuration config = library.makeConfig("test.yml");
        config.set("this.is.a.test", true);
        config.saveConfig();

        library.connectMySQL("localhost", "minecraft", "username", "secret");

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

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        Player player = e.getPlayer();

        ScoreboardHandler scoreboard = library.makeScoreboard(player, 20);
        scoreboard.addPage(new ScoreboardPage("   &e&lSIMPLE STATS   ").setEntries(Arrays.asList("&r", "&6Your Name:", "{player}", "&r&r", "&6Level", "{level}")));
        scoreboard.addPage(new ScoreboardPage("   &e&lSIMPLE STATS   ").setEntries(Arrays.asList("&r", "&6Food:", "{food}", "&r&r", "&6Tealth", "{health}")));

        // - Database tests
        // Using the eloquent user model.
        for (DataRow row : new User().with("groups").select("users.name", "group.name as rank").get()) {
            player.sendMessage(row.getString("name") + "'s rank is: " + row.getString("rank"));
        }

        // Using the normal query builder.
        for (DataRow row : new QueryBuilder("users").select("users.name", "group.name as rank").innerJoin("group", "users.id", "group.id").get()) {
            player.sendMessage(row.getString("name") + "'s rank is: " + row.getString("rank"));
        }
    }
}
