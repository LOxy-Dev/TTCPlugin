package fr.loxydev.ttcplugin.database;

import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import fr.loxydev.ttcplugin.utils.PlayerUtility;
import fr.loxydev.ttcplugin.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class TeamDataHandler extends DataHandler {

    Team team;

    public TeamDataHandler(int teamID) {
        this.table = "teams";
        this.prim_key = "idteam";
        this.prim_key_value = teamID;

        this.team = new Team(teamID, getTeamName(), getTag(), getColor(), getPlayers());
    }

    public TeamDataHandler(String teamName) {
    }

    // Followings methods are used to access data of a team
    public Team getTeam() {
        return team;
    }

    public String getTeamName() {
        return getString("name");
    }

    public String getTag() {
        return getString("tag");
    }

    public ChatColor getColor() {
        return ChatColor.getByChar(getString("color"));
    }

    public int getPoints() {
        return getInt("points");
    }

    public int getEventScore() {
        return getInt("eventscore");
    }

    public int getScoreRank() {
        return getInt("rankscore");
    }

    public ArrayList<PlayerDataHandler> getPlayers() {
        try (Connection conn = TheTerrierCityPlugin.dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT uuid FROM players WHERE team = ?")) {
            stmt.setObject(1, prim_key_value);

            ResultSet rs = stmt.executeQuery();

            ArrayList<PlayerDataHandler> list = new ArrayList<>();

            while(rs.next()) {
                list.add(new PlayerDataHandler(UUID.fromString(rs.getString(1))));
            }

            return list;
        } catch (SQLException e) {
            Bukkit.getLogger().info("Couldn't retrieve players in team " + getTeamName());
        }

        return null;
    }

    // Following methods are used to set data
    public boolean setTeamName() {
        if (team == null) return false;

        return pushUpdate("name", team.getName());
    }

    public boolean setTeamName(String teamName)
    {
        return pushUpdate("name", teamName);
    }

    public boolean setColor(ChatColor color) {
        return setColor(color.toString());
    }

    public boolean setColor(String color) {
        return pushUpdate("color", color);
    }

    public boolean setPoints(int points) {
        return pushUpdate("points", points);
    }

    public boolean setPlayers(ArrayList<PlayerUtility> players) {
        ArrayList<UUID> uuids = new ArrayList<>();
        for (PlayerUtility player : players)
            uuids.add(player.getPlayer().getUniqueId());

        return pushUpdate("players", players);
    }

    public boolean addPlayer(PlayerUtility player) {
        ArrayList<PlayerDataHandler> players = getPlayers();
        players.add(player.getPlayerData());

        ArrayList<UUID> uuids = new ArrayList<>();
        for (PlayerDataHandler p : getPlayers())
            uuids.add(p.getUuid());
        return pushUpdate("players", uuids);
    }

    public boolean removePlayer(PlayerUtility player) {
        ArrayList<PlayerDataHandler> players = getPlayers();
        players.remove(player.getPlayerData());

        ArrayList<UUID> uuids = new ArrayList<>();
        for (PlayerDataHandler p : players)
            uuids.add(p.getUuid());

        return pushUpdate("players", uuids);
    }
}
