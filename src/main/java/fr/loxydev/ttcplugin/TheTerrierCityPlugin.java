package fr.loxydev.ttcplugin;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import fr.loxydev.ttcplugin.commands.CommandManager;
import fr.loxydev.ttcplugin.listeners.PlayerJoinLeaveListeners;
import fr.loxydev.ttcplugin.listeners.MenuListener;
import fr.loxydev.ttcplugin.menu.Menu;
import fr.loxydev.ttcplugin.menu.PlayerMenuUtility;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class TheTerrierCityPlugin extends JavaPlugin {

    private static TheTerrierCityPlugin plugin;

    private static ArrayList<Menu> LIST_OF_MENUS;
    public static HashMap<Player, PlayerMenuUtility> shopManagerMenuList = new HashMap<>();

    // MongoDB collections
    private MongoCollection playersDataCol;
    private MongoCollection shopsCol;
    private MongoCollection itemsCol;
    private MongoCollection teamsCol;

    @Override
    public void onEnable() {
        plugin = this;

        // Setup config
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        // Connect to and initialize MongoDB
        connect();

        // Register the command manager
        getCommand("ttc").setExecutor(new CommandManager());

        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinLeaveListeners(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
    }

    @Override
    public void onDisable() {

    }

    private void connect() {
        MongoClient mongoClient = MongoClients.create(getConfig().getString("mongo_connect"));
        MongoDatabase database = mongoClient.getDatabase("ttc_dataset");
        playersDataCol = database.getCollection("playersdata");
        shopsCol = database.getCollection("shops");
        itemsCol = database.getCollection("items");
        teamsCol = database.getCollection("teams");
    }

    public static TheTerrierCityPlugin getPlugin() {
        return plugin;
    }

    public static ArrayList<Menu> getListOfMenus() {
        return LIST_OF_MENUS;
    }

    public static PlayerMenuUtility getPlayerMenuUtility(Player p) {
        PlayerMenuUtility playerMenuUtility = null;
        if (!(TheTerrierCityPlugin.shopManagerMenuList.containsKey(p))) {
            playerMenuUtility = new PlayerMenuUtility(p);
            TheTerrierCityPlugin.shopManagerMenuList.put(p, playerMenuUtility);

            return playerMenuUtility;
        } else {
            return TheTerrierCityPlugin.shopManagerMenuList.get(p);
        }
    }

    public MongoCollection getPlayersDataCol() {
        return playersDataCol;
    }

    public MongoCollection getShopsCol() {
        return shopsCol;
    }

    public MongoCollection getItemsCol() {
        return itemsCol;
    }

    public MongoCollection getTeamsCol() {
        return teamsCol;
    }
}
