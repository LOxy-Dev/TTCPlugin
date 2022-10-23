package fr.loxydev.ttcplugin.database;

import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import fr.loxydev.ttcplugin.utils.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FlatDataHandler extends DataHandler {

    public FlatDataHandler(int id) {
        this.table = "flats";
        this.prim_key = "flatid";
        this.prim_key_value = id;
    }

    public String getOwner() {
        return getString("owner");
    }

    public int getPrice() {
        return getInt("price");
    }

    public static boolean isTP(Location loc) {
        try (Connection conn = TheTerrierCityPlugin.dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT flatid FROM flats WHERE (tpInX = ? AND tpInY = ? AND tpInZ = ?) OR (tpOutX = ? AND tpOutY = ? AND tpOutZ = ?)", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            stmt.setInt(1, loc.getBlockX());
            stmt.setInt(2, loc.getBlockY());
            stmt.setInt(3, loc.getBlockZ());
            stmt.setInt(4, loc.getBlockX());
            stmt.setInt(5, loc.getBlockY());
            stmt.setInt(6, loc.getBlockZ());

            ResultSet rs = stmt.executeQuery();

            return rs.first();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Could not retrieve information about " + loc.toString());
        }

        return false;
    }

    public Location getTpCoords() {
        return new Location(TheTerrierCityPlugin.lobby, getInt("tpInX"), getInt("tpInY"), getInt("tpInZ"));
    }

    public static Location getLinkedLoc(Location loc) {
        try (Connection conn = TheTerrierCityPlugin.dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT tpOutX, tpOutY, tpOutZ FROM flats WHERE (tpInX = ? AND tpInY = ? AND tpInZ = ?)", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            stmt.setInt(1, loc.getBlockX());
            stmt.setInt(2, loc.getBlockY());
            stmt.setInt(3, loc.getBlockZ());

            ResultSet rs = stmt.executeQuery();

            if (rs.first()) {
                return new Location(loc.getWorld(), rs.getInt(1), rs.getInt(2), rs.getInt(3));
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info("Could not retrieve information about " + loc.toString());
        }

        try (Connection conn = TheTerrierCityPlugin.dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT tpInX, tpInY, tpInZ FROM flats WHERE (tpOutX = ? AND tpOutY = ? AND tpOutZ = ?)", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            stmt.setInt(1, loc.getBlockX());
            stmt.setInt(2, loc.getBlockY());
            stmt.setInt(3, loc.getBlockZ());

            ResultSet rs = stmt.executeQuery();

            if (rs.first()) {
                return new Location(loc.getWorld(), rs.getInt(1), rs.getInt(2), rs.getInt(3));
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info("Could not retrieve information about " + loc.toString());
        }

        return null;
    }

    public static int ownsFlat(PlayerUtility player) {
        try (Connection conn = TheTerrierCityPlugin.dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT flatid FROM flats WHERE owner = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            stmt.setString(1, player.getPlayer().getUniqueId().toString());

            ResultSet rs = stmt.executeQuery();

            if (rs.first())
                return rs.getInt(1);
        } catch (SQLException e) {
            Bukkit.getLogger().severe("ERROR while attempting to fetch" + player.getPlayer().getName() + "'s flat info.");
        }

        return -1;
    }

    public ArrayList<Location> getCorners() {
        int x1, y1, z1;
        int x2, y2, z2;

        x1 = getInt("coord1X");
        y1 = getInt("coord1Y");
        z1 = getInt("coord1Z");
        x2 = getInt("coord2X");
        y2 = getInt("coord2Y");
        z2 = getInt("coord2Z");

        ArrayList<Location> corners = new ArrayList<>();
        corners.add(new Location(TheTerrierCityPlugin.lobby, x1, y1, z1));
        corners.add(new Location(TheTerrierCityPlugin.lobby, x2, y2, z2));

        return corners;
    }

    public boolean buy(PlayerUtility player) {
        // Can't buy if player already owns a flat
        if (ownsFlat(player) != -1) {
            return false;
        }

        // Can't buy if already bought
        if (getOwner() != null) {
            return false;
        }

        // Can't buy if not enough money
        int price = getPrice();
        if (player.getPlayerData().getPointsAmount() < price) {
            return false;
        }

        // Write data
        if (pushUpdate("owner", player.getPlayer().getUniqueId().toString()))
            return player.getPlayerData().addPoints(-1 * getPrice());

        return false;
    }
}