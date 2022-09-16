package fr.loxydev.ttcplugin.database;

import com.mongodb.MongoException;
import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class ItemDataHandler extends DataHandler {

    public ItemDataHandler(String itemName) {
        this.collectionName = itemName;

        this.collection = getCollection("items");
        this.nameField = "name";
        update();
    }

    public static void createItemData(Material material, String shop, ArrayList<ArrayList<Integer>> prices) {
        try {
            TheTerrierCityPlugin.database.getCollection("items").insertOne(new Document()
                    .append("_id", new ObjectId())
                    .append("name", material.name())
                    .append("level", 0)
                    .append("level_list", prices)
                    .append("sold", 0)
                    .append("shop", shop));
        } catch (MongoException me) {
            System.err.println("Unable to insert Item " + material.name() + " due to an error: " + me);
        }
    }

    // Following methods are used to access database fields of an item
    public String getItemName() {
        return collectionName.toString();
    }

    public Material getMaterial() {
        return Material.getMaterial(getItemName());
    }

    public int getAmountSold() {
        return data.getInteger("sold");
    }

    public int getActualLevel() {
        return data.getInteger("level");
    }

    public List<List<Integer>> getLevelList() {
        return (List<List<Integer>>) data.get("level_list");
    }

    public int getPrice() {
        return getLevelList().get(getActualLevel()).get(0);
    }

    public int getNextLevelIn() {
        int levelMaxAmount = getLevelList().get(getActualLevel()).get(1);

        if (levelMaxAmount <= 0)
            return -1;
        else
            return levelMaxAmount - getAmountSold();
    }

    public String getShopName() {
        return data.getString("shop");
    }

    // The followings methods are used to set database fields
    public void setItemName(String itemName) {
        this.collectionName = itemName;

        pushUpdate("name", itemName);
    }

    public void setLevel(int level) {
        pushUpdate("level", level);
    }

    // TODO Add methods to remove/add a level
    public void setLevelList(List<List<Integer>> levelList) {
        pushUpdate("level_list", levelList);
    }

    public void updateLevel() {
        if (getNextLevelIn() <= 0) {
            setLevel(getActualLevel()+1);
            updateLevel();
        } else if (getNextLevelIn() > getLevelList().get(getActualLevel()).get(1)) {
            setLevel(getActualLevel()-1);
            updateLevel();
        }
    }

    public void setAmountSold(int amount) {
        pushUpdate("sold", amount);
        updateLevel();
    }

    public void increaseSales(int amount) {
        setAmountSold(getAmountSold() + amount);
    }

    // TODO update merchant data
    public void setShopName(String shopName) {
        pushUpdate("shop", shopName);
    }
}
