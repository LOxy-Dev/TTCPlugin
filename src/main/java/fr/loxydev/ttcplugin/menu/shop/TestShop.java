package fr.loxydev.ttcplugin.menu.shop;

import fr.loxydev.ttcplugin.database.ItemDataHandler;
import fr.loxydev.ttcplugin.database.ShopDataHandler;
import fr.loxydev.ttcplugin.menu.Menu;
import fr.loxydev.ttcplugin.menu.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TestShop extends Menu {

    private ArrayList<ItemDataHandler> itemsData = new ArrayList<>();

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
        ItemDataHandler item = itemsData.get(e.getSlot());
        if (playerMenuUtility.hasItem(item.getMaterial()))
            new ItemInterfaceMenu(item).open(player); // If pagination of shop is modified, index of document has to be updated
    }

    @Override
    public void setMenuItems(PlayerMenuUtility playerMenuUtility) {
        // Query and store Shop info
        ShopDataHandler shopData = new ShopDataHandler("Test Shop");

        List<String> itemList = shopData.getItemList();

        for (int i = 0; i < itemList.size(); i++) {
            String material = itemList.get(i);

            ItemDataHandler itemData = new ItemDataHandler(material);

            if (itemData == null) {
                Bukkit.getLogger().info("No " + material + " entry in item database.");
                break;
            }

            // Store item data's Document
            itemsData.add(itemData);

            // Create item's lore
            ArrayList<String> itemLore = new ArrayList<>();
            itemLore.add("Sold: " + itemData.getAmountSold());
            itemLore.add("\n");
            int itemPrice = itemData.getPrice();
            itemLore.add("Price: " + itemPrice + " Points");
            int nextLevelIn = itemData.getNextLevelIn();
            itemLore.add(nextLevelIn + " until next price update");
            itemLore.add("\n");
            int inInventory = 0;
            for (ItemStack item : playerMenuUtility.getPlayer().getInventory().getContents())
                if (item != null) if (item.getType() == Material.getMaterial(material)) inInventory += item.getAmount();
            itemLore.add(inInventory + " in you inventory");

            inventory.setItem(i, playerMenuUtility.makeItem(Material.getMaterial(material), itemLore));
        }
    }
}
