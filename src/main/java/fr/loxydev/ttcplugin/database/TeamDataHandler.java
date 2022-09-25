package fr.loxydev.ttcplugin.database;

import fr.loxydev.ttcplugin.menu.PlayerMenuUtility;
import fr.loxydev.ttcplugin.team.Team;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamDataHandler extends DataHandler {

    Team team;

    public TeamDataHandler(Team team) {
        this.team = team;
        this.collectionName = team.getName();

        this.collection = getCollection("teams");
        this.nameField = "name";
        update();
    }

    public TeamDataHandler(String teamName) {
        this.collectionName = teamName;

        this.collection = getCollection("teams");
        this.nameField = "name";
        update();
    }

    // Followings methods are used to access data of a team
    public String getTeamName() {
        return data.getString("name");
    }

    public ChatColor getColor() {
        return ChatColor.getByChar(data.getString("color"));
    }

    public int getPoints() {
        return data.getInteger("points");
    }

    public ArrayList<PlayerDataHandler> getPlayers() {
        List<UUID> uuids = data.getList("players", UUID.class);
        ArrayList<PlayerDataHandler> players = new ArrayList<>();

        for (UUID uuid : uuids) {
            players.add(new PlayerDataHandler(uuid));
        }

        return players;
    }

    // Following methods are used to set data
    public void setTeamName() {
        if (team == null) return;

        pushUpdate("name", team.getName());
    }

    public void setTeamName(String teamName)
    {
        this.collectionName = teamName;

        pushUpdate("name", teamName);
    }

    public void setColor(ChatColor color) {
        setColor(color.toString());
    }

    public void setColor(String color) {
        pushUpdate("color", color);
    }

    public void setPoints(int points) {
        pushUpdate("points", points);
    }

    public void setPlayers(ArrayList<PlayerMenuUtility> players) {
        ArrayList<UUID> uuids = new ArrayList<>();
        for (PlayerMenuUtility player : players)
            uuids.add(player.getPlayer().getUniqueId());

        pushUpdate("players", players);
    }

    public void addPlayer(PlayerMenuUtility player) {
        update();
        ArrayList<PlayerDataHandler> players = getPlayers();
        players.add(player.getPlayerData());


        ArrayList<UUID> uuids = new ArrayList<>();
        for (PlayerDataHandler p : getPlayers())
            uuids.add(p.getUuid());
        pushUpdate("players", uuids);
    }

    public void removePlayer(PlayerMenuUtility player) {
        update();
        ArrayList<PlayerDataHandler> players = getPlayers();
        players.remove(player.getPlayerData());

        ArrayList<UUID> uuids = new ArrayList<>();
        for (PlayerDataHandler p : players)
            uuids.add(p.getUuid());

        pushUpdate("players", uuids);
    }
}
