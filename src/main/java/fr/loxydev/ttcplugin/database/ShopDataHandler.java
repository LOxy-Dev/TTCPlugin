package fr.loxydev.ttcplugin.database;

import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShopDataHandler extends DataHandler {

    public ShopDataHandler(int shopId) {
        this.table = "shops";
        this.prim_key = "shopid";
        this.prim_key_value = shopId;
    }

    // Following methods are used to access database fields of a shop
    public String getShopName() {
        return getString("name");
    }

    public boolean isOpen() {
    return getBoolean("open");
    }

    public int getPurchaseAmount() {
    return getInt("total_sales");
    }

    public int getActualLevel() {
    return getInt("level");
    }

    public ArrayList<Material> getItemList() {
        ArrayList<Material> list = new ArrayList<>();

        try (Connection conn = TheTerrierCityPlugin.dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT material FROM items WHERE shop = ?")) {
            stmt.setObject(1, prim_key_value);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(Material.getMaterial(rs.getString(1)));
            }
            return list;
        } catch (SQLException e) {
            Bukkit.getLogger().info("Could't retrieve items in shop " + getShopName());
        }

        return null;
    }

    // Following methods are used to set database fields
    public boolean setShopName(String shopName) {
        return pushUpdate("name", shopName);
    }

    public boolean setOpeningState(boolean openingState) {
        return pushUpdate("open", openingState);
    }

    public void updateLevel() {
        // TODO Implement Shop level
    }

    public boolean setPurchaseAmount(int purchaseAmount) {
        boolean updateSales = pushUpdate("total_sales", purchaseAmount);

        updateLevel();
        return updateSales;
    }

    public boolean increasePurchases(int amount) {
        return setPurchaseAmount(getPurchaseAmount() + amount);
    }

    // TODO Add methods to add/remove items to sell + update item data
    public boolean setItemList(List<String> itemList) {
        return false;
    }
}
