package fr.loxydev.ttcplugin;

import com.mongodb.client.MongoDatabase;
import fr.loxydev.ttcplugin.commands.CommandManager;
import fr.loxydev.ttcplugin.database.DataHandler;
import fr.loxydev.ttcplugin.listeners.PlayerJoinLeaveListeners;
import fr.loxydev.ttcplugin.listeners.MenuListener;
import fr.loxydev.ttcplugin.menu.Menu;
import fr.loxydev.ttcplugin.menu.PlayerMenuUtility;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public final class TheTerrierCityPlugin extends JavaPlugin {

    private static TheTerrierCityPlugin plugin;

    private static ArrayList<Menu> LIST_OF_MENUS;
    public static HashMap<Player, PlayerMenuUtility> shopManagerMenuList = new HashMap<>();
    public static MongoDatabase database;

    @Override
    public void onEnable() {
        plugin = this;

        // Setup config
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        // Connect to and initialize MongoDB
        database = DataHandler.connect();

        // Register the command manager
        getCommand("ttc").setExecutor(new CommandManager());

        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinLeaveListeners(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
    }

    @Override
    public void onDisable() {

    }

    public static TheTerrierCityPlugin getPlugin() {
        return plugin;
    }

    public static ArrayList<Menu> getListOfMenus() {
        return LIST_OF_MENUS;
    }

    public static PlayerMenuUtility getPlayerMenuUtility(Player p) {
        PlayerMenuUtility playerMenuUtility;
        if (!(TheTerrierCityPlugin.shopManagerMenuList.containsKey(p))) {
            playerMenuUtility = new PlayerMenuUtility(p);
            TheTerrierCityPlugin.shopManagerMenuList.put(p, playerMenuUtility);

            return playerMenuUtility;
        } else {
            return TheTerrierCityPlugin.shopManagerMenuList.get(p);
        }
    }
}
