package fr.loxydev.ttcplugin.database;

/* TODO Implements Team
import fr.loxydev.ttcplugin.team.Team; */
import org.bukkit.Bukkit;

import java.util.UUID;

public class PlayerDataHandler extends DataHandler {

    private UUID uuid;

    public PlayerDataHandler(UUID uuid) {
        this.uuid = uuid;
        collectionName = uuid;

        this.collection = getCollection("playersdata");
        this.nameField = "uuid";
        update();
    }

    // Following methods are used to access database fields of a player
    public UUID getUuid() {
        return uuid;
    }

    @Override
    protected String getObjectName() {
        return getPlayerName();
    }

    public String getPlayerName() {
        return data.getString("name");
    }

    public int getPointsAmount() {
        return data.getInteger("points");
    }

    /* TODO Implements Team
    public Team getTeam() {
        return Team.find(data.getString("team"));
    }*/

    // The followings methods are used to set database fields
    public void setUuid(UUID uuid) {
        this.uuid = uuid;

        pushUpdate("uuid", uuid);
    }

    public void setPlayerName() {
        pushUpdate("name", Bukkit.getPlayer(this.uuid));
    }

    public void setPlayerName(String name) {
        pushUpdate("name", name);
    }

    public void setPointsAmount(int amount) {
        pushUpdate("points", amount);
    }

    public void addPoints(int amount) {
        setPointsAmount(getPointsAmount() + amount);
    }

    /* TODO Implements Team
    public void setTeam(Team team) {
        pushUpdate("team", team.name);
    }

    public void setTeam(String teamName) {
        pushUpdate("team", teamName);
    }*/
    }
