package fr.loxydev.ttcplugin.database;

import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import fr.loxydev.ttcplugin.utils.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HeadDataHandler extends DataHandler {

    public HeadDataHandler(int id) {
        this.table = "heads";
        this.prim_key = "headid";
        this.prim_key_value = id;
    }

    public static HeadDataHandler getHeadByPos(Location loc) {
        try (Connection conn = TheTerrierCityPlugin.dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT headid FROM heads WHERE headX = ? AND headY = ? AND headZ = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            stmt.setInt(1, loc.getBlockX());
            stmt.setInt(2, loc.getBlockY());
            stmt.setInt(3, loc.getBlockZ());

            ResultSet rs = stmt.executeQuery();
            if (rs.first()) {
                return new HeadDataHandler(rs.getInt(1));
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info("Could not retrieve Head at location X: " + loc.getBlockX() + " Y: " + loc.getBlockY() + " Z: " + loc.getBlockZ() + ".");
            Bukkit.getLogger().info(e.toString());
        }

        return null;
    }

    public void clicked(PlayerUtility player) {
        // First check if the player already found this headid
        try (Connection conn = TheTerrierCityPlugin.dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT foundid FROM headsfound WHERE headid = ? AND player = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            stmt.setInt(1, (Integer) this.prim_key_value);
            stmt.setString(2, player.getPlayer().getUniqueId().toString());

            ResultSet rs = stmt.executeQuery();

            if (rs.first()) {
                return;
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info("Failed to find if a head is found.");
            return;
        }

        // Create a new entry
        try (Connection conn = TheTerrierCityPlugin.dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO `headsfound` (headid, player) VALUES (?, ?)")) {
            stmt.setInt(1, (Integer) this.prim_key_value);
            stmt.setString(2, player.getPlayer().getUniqueId().toString());

            stmt.execute();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Could not register the head found");
            return;
        }

        // Update player's data
        player.getPlayerData().increaseHeads();

        // Send message to confirm found head
        int total_heads = 0;

        try (Connection conn = TheTerrierCityPlugin.dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT headid FROM heads", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                total_heads++;
                rs.getInt(1);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info("Could not retrieve number of heads registered.");
        }

        player.getPlayer().sendMessage(ChatColor.GREEN + "[TTC HeadHunt] Well seen, you found head #" + this.prim_key_value + ". You found " + player.getPlayerData().getHeads() + "/" + total_heads + " heads.");
    }
}