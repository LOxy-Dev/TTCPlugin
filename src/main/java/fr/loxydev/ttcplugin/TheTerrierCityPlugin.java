package fr.loxydev.ttcplugin;

import com.mysql.cj.jdbc.MysqlDataSource;
import fr.loxydev.ttcplugin.commands.CommandManager;
import fr.loxydev.ttcplugin.database.DataHandler;
import fr.loxydev.ttcplugin.database.DbCredentials;
import fr.loxydev.ttcplugin.listeners.ElevatorListener;
import fr.loxydev.ttcplugin.listeners.PlayerJoinLeaveListeners;
import fr.loxydev.ttcplugin.listeners.MenuListener;
import fr.loxydev.ttcplugin.utils.PlayerUtility;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.HashMap;

public final class TheTerrierCityPlugin extends JavaPlugin {

    private static TheTerrierCityPlugin plugin;
    public static MysqlDataSource dataSource;
    public static HashMap<Player, PlayerUtility> playerList = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;

        // Setup config
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        // Connect to and initialize MySQL
        try {
            dataSource = DataHandler.connect(new DbCredentials(getConfig().getString("dbh"), getConfig().getInt("dbp"), getConfig().getString("dbn"), getConfig().getString("dbu"), getConfig().getString("dbpwd")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Register the command manager
        getCommand("ttc").setExecutor(new CommandManager());

        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinLeaveListeners(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new ElevatorListener(), this);
    }

    @Override
    public void onDisable() {

    }

    public static TheTerrierCityPlugin getPlugin() {
        return plugin;
    }

    public static PlayerUtility getPlayerUtility(Player p) {
        PlayerUtility playerUtility;
        if (!(TheTerrierCityPlugin.playerList.containsKey(p))) {
            playerUtility = new PlayerUtility(p);
            TheTerrierCityPlugin.playerList.put(p, playerUtility);

            return playerUtility;
        } else {
            return TheTerrierCityPlugin.playerList.get(p);
        }
    }
}
