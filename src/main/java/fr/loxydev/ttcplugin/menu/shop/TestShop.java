package fr.loxydev.ttcplugin.menu.shop;

import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import fr.loxydev.ttcplugin.menu.Menu;
import fr.loxydev.ttcplugin.menu.PlayerMenuUtility;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;

public class TestShop extends Menu {
    @Override
    public String getMenuName() {
        return "Test Shop";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e, PlayerMenuUtility playerMenuUtility) {

    }

    @Override
    public void setMenuItems(PlayerMenuUtility playerMenuUtility) {
        // Query Shop info
        Document shopData = (Document) TheTerrierCityPlugin.getPlugin().getShopsCol().find(eq("name", "Test Shop")).first();

        ArrayList<String> itemList = (ArrayList<String>) shopData.get("item_list");
        for (int i = 0; i < itemList.size(); i++) {
            String material = itemList.get(i);

            Document itemData = (Document) TheTerrierCityPlugin.getPlugin().getItemsCol().find(eq("name", material)).first();

            if (itemData == null) {
                Bukkit.getLogger().info("No " + material + " entry in item database.");
                break;
            }

            ArrayList<String> itemLore = new ArrayList<>();
            itemLore.add("Sold: " + itemData.get("sold"));
            itemLore.add("\n");
            int itemLevel = itemData.getInteger("level");
            ArrayList<ArrayList<Integer>> itemPrices = (ArrayList<ArrayList<Integer>>) itemData.get("level_list");
            int itemPrice = ((ArrayList<Integer>) itemPrices.get(itemLevel)).get(0);
            itemLore.add("Price: " + itemPrice + " Points");
            int untilNext = ((ArrayList<Integer>) itemPrices.get(itemLevel)).get(1) - (Integer) itemData.get("sold");
            itemLore.add(untilNext + " until next price update");
            itemLore.add("\n");
            int inInventory = 0;
            for (ItemStack item : playerMenuUtility.getPlayer().getInventory().getContents())
                if (item != null) if (item.getType() == Material.getMaterial(material)) inInventory += item.getAmount();
            itemLore.add(inInventory + " in you inventory");

            inventory.setItem(i, playerMenuUtility.makeItem(Material.getMaterial(material), itemLore));
        }
    }
}
