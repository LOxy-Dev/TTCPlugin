package fr.loxydev.ttcplugin.database;

import java.util.List;

public class ItemDataHandler extends DataHandler {

    public ItemDataHandler(String itemName) {
        this.collectionName = itemName;

        this.collection = getCollection("items");
        this.nameField = "name";
        update();
    }

    // Following methods are used to access database fields of an item
    public String getItemName() {
        return collectionName.toString();
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
        int level = getActualLevel();
        List<List<Integer>> levelList = getLevelList();
        int amount = getAmountSold();

        while (amount >= levelList.get(level-1).get(1) && amount <= levelList.get(level).get(1)) {
            if (amount > levelList.get(level).get(1))
                level++;
            else
                level--;
        }

        setLevel(level);
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
