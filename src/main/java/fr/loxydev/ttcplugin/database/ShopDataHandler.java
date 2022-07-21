package fr.loxydev.ttcplugin.database;

import java.util.List;

public class ShopDataHandler extends DataHandler {

    public ShopDataHandler(String shopName) {
        this.collectionName = shopName;

        this.collection = getCollection("shops");
        this.nameField = "name";
        update();
    }

    // Following methods are used to access database fields of a shop
    public String getShopName() {
        return collectionName.toString();
    }

    public boolean isShopOpen() {
        return data.getBoolean("open");
    }

    public int getPurchaseAmount() {
        return data.getInteger("total_purchases");
    }

    public int getActualLevel() {
        return data.getInteger("level");
    }

    public List<String> getItemList() {
        return data.getList("item_list", String.class);
    }

    // Following methods are used to set database fields
    public void setShopName(String shopName) {
        collectionName = shopName;

        pushUpdate("name", shopName);
    }

    public void setOpeningState(boolean openingState) {
        pushUpdate("open", openingState);
    }

    public void updateLevel() {
        // TODO Implement Shop level
    }

    public void setPurchaseAmount(int purchaseAmount) {
        pushUpdate("total_purchases", purchaseAmount);

        updateLevel();
    }

    // TODO Add methods to add/remove items to sell + update item data
    public void setItemList(List<String> itemList) {
        pushUpdate("item_list", itemList);
    }
}
