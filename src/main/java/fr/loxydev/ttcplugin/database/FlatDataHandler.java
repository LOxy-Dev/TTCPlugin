package fr.loxydev.ttcplugin.database;

import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FlatDataHandler extends DataHandler {

    public FlatDataHandler(int id) {
        this.table = "flats";
        this.prim_key = "flatid";
        this.prim_key_value = id;
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
}