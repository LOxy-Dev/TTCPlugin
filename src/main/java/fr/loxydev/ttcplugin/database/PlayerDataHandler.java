package fr.loxydev.ttcplugin.database;

import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import fr.loxydev.ttcplugin.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class PlayerDataHandler extends DataHandler {

    private UUID uuid;

    public PlayerDataHandler(UUID uuid) {
        this.uuid = uuid;

        this.table = "players";
        this.prim_key = "uuid";
        this.prim_key_value = uuid.toString();
    }

    public static boolean createPlayerData(Player p) {
        try (Connection conn = TheTerrierCityPlugin.dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO `players` (uuid, name, team) VALUES (?, ?, ?)")) {
            stmt.setString(1, p.getUniqueId().toString());
            stmt.setString(2, p.getName());
            stmt.setInt(3, 0);

            stmt.execute();
            return true;
        } catch (SQLException e) {
            Bukkit.getLogger().info("Could not insert player " + p.getName() + " in database.");
        }

        return false;
    }

    // Following methods are used to access database fields of a player
    public UUID getUuid() {
        return uuid;
    }

    public String getPlayerName() {
        return getString("name");
    }

    public int getPointsAmount() {
        return getInt("points");
    }

    public int getHeads() {
        return getInt("heads");
    }

    public Team getTeam() {
        return new TeamDataHandler(getInt("team")).getTeam();
    }

    // The followings methods are used to set database fields
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
        pushUpdate("uuid", uuid);
    }

    public void setPlayerName() {
        setPlayerName((Objects.requireNonNull(Bukkit.getPlayer(this.uuid))).getName());
    }

    public boolean setPlayerName(String name) {
        return pushUpdate("name", name);
    }

    public boolean setPointsAmount(int amount) {
        return pushUpdate("points", amount);
    }

    public boolean addPoints(int amount) {
        return setPointsAmount(getPointsAmount() + amount);
    }

    public boolean setTeam(int teamId) {
        return pushUpdate("team", teamId);
    }

    public boolean increaseHeads() {
        return pushUpdate("heads", getHeads() + 1);
    }
}
