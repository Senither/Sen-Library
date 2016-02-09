package com.senither.test;

import com.senither.library.SenLibrary;
import com.senither.library.config.Configuration;
import com.senither.library.database.eloquent.DataRow;
import com.senither.library.database.utils.QueryBuilder;
import com.senither.library.inventory.WallSide;
import com.senither.library.message.MessageType;
import com.senither.library.scoreboard.ScoreboardHandler;
import com.senither.library.scoreboard.ScoreboardPage;
import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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

        library.makeInventory(3, WallSide.ALL, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15), "&8Filler item").open(player);

        return true;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        Player player = e.getPlayer();

        // Sends a title and tab message to the player
        library.makeMessage("&6&lSenLibrary Dev Server", "&9I <3 127.0.0.1", MessageType.TAB).send(player);
        library.makeMessage("&6Welcome back!", "Hi there &a{playerName}", MessageType.TITLE).send(player);

        // Creates a dynamic scoreboard withscrolling text and assigns it to the player
        ScoreboardHandler scoreboard = library.makeScoreboard(player, 50);
        scoreboard.addPage(new ScoreboardPage("&e&lSCOREBOARD  TESTS").setEntries(Arrays.asList(
        /* >_> */"&r", "&6Your Name:", "{playerName}",
        /* >_> */ "&r&r", "&6Level", "{playerLevel}",
        /* >_> */ "&r&r&r", "&6Scrolling text", "Your name is {playerName} and you're level {playerLevel}."
        )).setExtraDelay(150));

        scoreboard.addPage(new ScoreboardPage("   &e&lSIMPLE STATS   ").setEntries(Arrays.asList(
        /* >_> */"&r", "&6Food:", "{playerFood}",
        /* >_> */ "&r&r", "&6Tealth", "{playerHealth}"
        )).resetLastPage(true));

        // Quyeries the database twice using the same query, once from the eloquent user model, and once with the query builder.
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
