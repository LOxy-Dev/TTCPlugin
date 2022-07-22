package fr.loxydev.ttcplugin.database;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;

import static com.mongodb.client.model.Filters.*;

public abstract class DataHandler {

    protected static MongoClient mongoClient;
    protected MongoCollection<Document> collection;
    protected String nameField;
    protected Object collectionName;
    protected Document data;

    public static MongoDatabase connect() {
        try {
            mongoClient = MongoClients.create(TheTerrierCityPlugin.getPlugin().getConfig().getString("mongo_connect"));
            return mongoClient.getDatabase("ttc_database");
        } catch (MongoException e) {
            Bukkit.getLogger().info("Failed to connect to MongoDB.");

            return null;
        }
    }

    protected MongoCollection<Document> getCollection(String name) {
        return TheTerrierCityPlugin.database.getCollection(name);
    }

    public void update() {
        data = collection.find(eq(nameField, collectionName)).first();
    }

    protected String getObjectName() {
        return collectionName.toString();
    }

    public void pushUpdates(Bson updates) {
        try {
            collection.updateOne(data, updates);
            update();
        } catch (MongoException me) {
            Bukkit.getLogger().info("Unable to update " + getObjectName() + " data due to an error: " + me);
        }
    }

    public void pushUpdate(String field, Object value) {
        pushUpdates(Updates.set(field, value));
    }
}
