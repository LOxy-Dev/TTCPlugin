package fr.loxydev.ttcplugin;

import com.mysql.cj.jdbc.MysqlDataSource;
import fr.loxydev.ttcplugin.commands.CommandManager;
import fr.loxydev.ttcplugin.database.DataHandler;
import fr.loxydev.ttcplugin.database.DbCredentials;
import fr.loxydev.ttcplugin.database.FlatDataHandler;
import fr.loxydev.ttcplugin.database.PlayerDataHandler;
import fr.loxydev.ttcplugin.listeners.*;
import fr.loxydev.ttcplugin.utils.Hologram;
import fr.loxydev.ttcplugin.utils.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public final class TheTerrierCityPlugin extends JavaPlugin {

    private static TheTerrierCityPlugin plugin;
    public static MysqlDataSource dataSource;
    public static World lobby;
    public static World survival;
    public static HashMap<Player, PlayerUtility> playerList = new HashMap<>();
    public static ArrayList<Hologram> holograms = new ArrayList<>();

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
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new TeleportListener(), this);
        getServer().getPluginManager().registerEvents(new HeadHuntListener(), this);
        getServer().getPluginManager().registerEvents(new LobbyProtection(), this);

        // Register the worlds
        lobby = new WorldCreator("Lobby").createWorld();
        survival = new WorldCreator("Survival").createWorld();

        // Add holograms
        // Flats holograms
        try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT flatid FROM flats;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt(1);
                FlatDataHandler flatData = new FlatDataHandler(id);
                Location tpLoc = flatData.getTpCoords();
                Location loc = new Location(tpLoc.getWorld(), tpLoc.getBlockX(), tpLoc.getBlockY(), tpLoc.getBlockZ());

                ArrayList<String> text = new ArrayList<>();
                text.add("Appartement §o#" + id);
                if (flatData.getOwner() == null) {
                    text.add("À vendre");
                    text.add("Prix: " + flatData.getPrice());
                } else {
                    PlayerDataHandler playerData = new PlayerDataHandler(UUID.fromString(flatData.getOwner()));
                    text.add(playerData.getTeam().getColor() + playerData.getPlayerName());
                }

                holograms.add(new Hologram(loc, text, "flat_" + id));
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Couldn't create flats holograms");
        }
    }

    @Override
    public void onDisable() {
        // Remove holograms
        for (Hologram hologram : holograms) {
            hologram.remove();
        }
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
