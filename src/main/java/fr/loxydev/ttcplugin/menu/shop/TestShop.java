package fr.loxydev.ttcplugin.menu.shop;

import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import fr.loxydev.ttcplugin.menu.Menu;
import fr.loxydev.ttcplugin.menu.PlayerMenuUtility;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import java.util.List;

public class TestShop extends Menu {

    private ArrayList<Document> itemsData = new ArrayList<>();

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
        if (e.getCurrentItem() == FILLER_GLASS || e.getCurrentItem() == null) return;

        Player player = playerMenuUtility.getPlayer();
        new ItemInterfaceMenu(itemsData.get(e.getSlot())).open(player); // If pagination of shop is modified, index of document has to be updated
    }

    @Override
    public void setMenuItems(PlayerMenuUtility playerMenuUtility) {
        // Query and store Shop info
        Document shopData = (Document) TheTerrierCityPlugin.getPlugin().getShopsCol().find(eq("name", "Test Shop")).first();

        List<String> itemList = shopData.getList("item_list", String.class);

        for (int i = 0; i < itemList.size(); i++) {
            String material = itemList.get(i);

            Document itemData = (Document) TheTerrierCityPlugin.getPlugin().getItemsCol().find(eq("name", material)).first();

            if (itemData == null) {
                Bukkit.getLogger().info("No " + material + " entry in item database.");
                break;
            }

            // Store item data's Document
            itemsData.add(itemData);

            // Create item's lore
            ArrayList<String> itemLore = new ArrayList<>();
            itemLore.add("Sold: " + itemData.getInteger("sold"));
            itemLore.add("\n");
            int itemLevel = itemData.getInteger("level");
            List<List<Integer>> itemPrices = (List<List<Integer>>) itemData.get("level_list"); // Have to cast instead of .getList because can't figure how to replace List<Integer>.class
            int itemPrice = itemPrices.get(itemLevel).get(0);
            itemLore.add("Price: " + itemPrice + " Points");
            int untilNext = itemPrices.get(itemLevel).get(1) - itemData.getInteger("sold");
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
