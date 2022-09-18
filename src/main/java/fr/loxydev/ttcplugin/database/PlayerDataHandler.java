package fr.loxydev.ttcplugin.database;

import com.mongodb.MongoException;
import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import fr.loxydev.ttcplugin.team.Team;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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

    public static void createPlayerData(Player p) {
        try {
            TheTerrierCityPlugin.database.getCollection("playersdata").insertOne(new Document()
                .append("_id", new ObjectId())
                .append("uuid", p.getUniqueId())
                .append("name", p.getDisplayName())
                .append("points", 0)
                .append("team", "default"));
        } catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
        }
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

    // TODO Implements Team
    public Team getTeam() {
        return null;
    }

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

    public void setTeam(Team team) {
        pushUpdate("team", team.getName());
    }

    public void setTeam(String teamName) {
        pushUpdate("team", teamName);
    }
}
