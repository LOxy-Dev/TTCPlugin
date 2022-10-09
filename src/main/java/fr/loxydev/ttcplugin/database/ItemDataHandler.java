package fr.loxydev.ttcplugin.database;

import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemDataHandler extends DataHandler {

    public ItemDataHandler(Material material) {
        this.table = "items";
        this.prim_key = "material";
        this.prim_key_value = material.name();
    }

    public static boolean createItemData(Material material, int shop, ArrayList<int[]> prices) {
        try (Connection conn = TheTerrierCityPlugin.dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO `items` (material, shop, level_prices) VALUES (?, ?, ?)")) {
            stmt.setString(1, material.name());
            stmt.setInt(2, shop);
            stmt.setString(3, levelListToString(prices));

            stmt.execute();
            return true;
        } catch (SQLException e) {
            Bukkit.getLogger().info("Could not insert item " + material.name() + " in database.");
        }

        return false;
    }

    // Following methods are used to access database fields of an item
    public String getItemName() {
        return (String) getString("material");
    }

    public Material getMaterial() {
        return Material.getMaterial(getItemName());
    }

    public int getAmountSold() {
        return getInt("sold");
    }

    public int getActualLevel() {
        return getInt("level");
    }

    public ArrayList<int[]> getLevelList() {
        return levelStringToList(getString("level_prices"));
    }

    public int getPrice() {
        return getLevelList().get(getActualLevel())[1];
    }

    public int getNextLevelIn() {
        ArrayList<int[]> list = getLevelList();

        int next_lvl = getLevelList().get(getActualLevel())[0];

        if (next_lvl == -1) return 2147483640;

        return next_lvl - getAmountSold();
    }

    public int getShopId() {
        return getInt("shop");
    }

    public String getShopName() {
        return new ShopDataHandler(getShopId()).getShopName();
    }

    public int getBoostPrice() {
        return getInt("boost_price");
    }

    public boolean isBoosted() {
        return getBoolean("boosted");
    }

    // The following methods are used to set database fields
    public boolean setLevel(int level) {
        return pushUpdate("level", level);
    }

    // TODO Add methods to remove/add a level
    public boolean setLevelList(ArrayList<int[]> levelList) {
        return pushUpdate("level_list", levelListToString(levelList));
    }

    public boolean updateLevel() {
        int soldAmount = getAmountSold();
        ArrayList<int[]> levelPrices = getLevelList();

        return levelUp(0, soldAmount, levelPrices);
    }

    public boolean levelUp(int lvl, int amountSold, ArrayList<int[]> levelPrices) {
        if (lvl >= levelPrices.size() - 1 || levelPrices.get(lvl)[0] > amountSold) {
            return setLevel(lvl);
        } else {
            return levelUp(lvl+1, amountSold, levelPrices);
        }
    }

    public boolean setAmountSold(int amount) {
        return pushUpdate("sold", amount) && updateLevel();
    }

    public boolean increaseSales(int amount) {
        return setAmountSold(getAmountSold() + amount);
    }

    // TODO update merchant data
    public boolean setShop(int shop) {
        return pushUpdate("shop", shop);
    }

    // UTILITIES
    public static String levelListToString(ArrayList<int[]> list) {
        StringBuilder builder = new StringBuilder();

        for (int[] pair : list) {
            if (pair[0] == -1)
                builder.append("+");
            else
                builder.append(pair[0]);
            builder.append(",")
                    .append(pair[1])
                    .append(";");
        }

        return builder.toString();
    }

    public static ArrayList<int[]> levelStringToList(String rawList) {
        ArrayList<int[]> list = new ArrayList<>();

        int a = 0, b = 0;
        boolean last = false;
        for (char c : rawList.toCharArray()) {
            if (c==',') {
                last ^= true;
            }
            else if (c==';') {
                list.add(new int[]{a, b});
                a = 0;
                b = 0;
                last ^= true;
            }
            else {
                if (last) {
                    b *= 10;
                    b += Character.getNumericValue(c);
                }
                else {
                    if (c == '+') {
                        a = -1;
                    }
                    else {
                        a *= 10;
                        a += Character.getNumericValue(c);
                    }
                }
            }
        }

        return list;
    }
}
