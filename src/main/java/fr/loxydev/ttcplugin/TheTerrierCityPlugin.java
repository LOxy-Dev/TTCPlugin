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
import org.bukkit.*;
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
        // Add scoreboards
        // Team ranks
        try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT name, color, points FROM teams ORDER BY points DESC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            ResultSet rs = stmt.executeQuery();

            ArrayList<String> content = new ArrayList<>();
            while (rs.next()) {
                if (rs.getString(1).equals("Admin Team") || rs.getString(1).equals("Default Team"))
                    continue;

                String bob = "§" +
                        rs.getString(2) +
                        "§l" +
                        rs.getString(1) +
                        "§r: " +
                        rs.getInt(3);

                content.add(bob);
            }
            holograms.add(new Hologram(new Location(lobby, -2, 214, 10), content, "teams"));
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Couldn't fetch team scoreboard.");
        }

        // Player Top 5
        try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT uuid FROM players ORDER BY points DESC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            ResultSet rs = stmt.executeQuery();

            ArrayList<String> content = new ArrayList<>();
            int count = 0;
            while (rs.next() && count < 5) {
                if (rs.getString(1).equals("4bb38f66-67f4-41f9-b12b-8398367eb7eb"))
                    continue;

                PlayerDataHandler playerData = new PlayerDataHandler(UUID.fromString(rs.getString(1)));

                String bob = "§" +
                        playerData.getTeam().getColor().getChar() +
                        playerData.getPlayerName() +
                        "§r: " +
                        playerData.getPointsAmount();

                content.add(bob);

                count++;
            }
            holograms.add(new Hologram(new Location(lobby, 4, 214, 10), content, "players"));
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Couldn't fetch top 5 players scoreboard.");
        }

        // Flats holograms
        try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT flatid FROM flats;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt(1);
                FlatDataHandler flatData = new FlatDataHandler(id);
                Location tpLoc = flatData.getTpCoords();
                Location loc = new Location(tpLoc.getWorld(), tpLoc.getBlockX() + 0.5, tpLoc.getBlockY()- 0.25, tpLoc.getBlockZ() + 0.5);

                ArrayList<String> text = new ArrayList<>();
                text.add("Appartement §o#" + id);
                if (flatData.getOwner() == null) {
                    text.add("À vendre");
                    text.add("Prix: " + flatData.getPrice());
                } else {
                    try {
                        PlayerDataHandler playerData = new PlayerDataHandler(UUID.fromString(flatData.getOwner()));
                        text.add(playerData.getTeam().getColor() + playerData.getPlayerName());
                    } catch (IllegalArgumentException e) {
                        text.add("§l" + flatData.getOwner());
                    }
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

    public static void updateScoreboards() {
        ArrayList<String> content;
        // Player Top 5
        content = new ArrayList<>();
        try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT uuid FROM players ORDER BY points DESC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            ResultSet rs = stmt.executeQuery();

            int count = 0;
            while (rs.next() && count < 5) {
                if (rs.getString(1).equals("4bb38f66-67f4-41f9-b12b-8398367eb7eb"))
                    continue;

                PlayerDataHandler playerData = new PlayerDataHandler(UUID.fromString(rs.getString(1)));

                String bob = "§" +
                        playerData.getTeam().getColor().getChar() +
                        playerData.getPlayerName() +
                        "§r: " +
                        playerData.getPointsAmount();

                content.add(bob);

                count++;
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Couldn't fetch top 5 players scoreboard.");
        }
        for (Hologram h : holograms) {
            if (!h.getId().equals("players"))
                continue;

            h.updateText(content);
        }

        // Team ranks
        content = new ArrayList<>();
        try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT name, color, points FROM teams ORDER BY points DESC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                if (rs.getString(1).equals("Admin Team") || rs.getString(1).equals("Default Team"))
                    continue;

                String bob = "§" +
                        rs.getString(2) +
                        "§l" +
                        rs.getString(1) +
                        "§r: " +
                        rs.getInt(3);

                content.add(bob);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Couldn't fetch team scoreboard.");
        }

        for (Hologram h : holograms) {
            if (!h.getId().equals("teams"))
                continue;

            h.updateText(content);
        }
    }
}
