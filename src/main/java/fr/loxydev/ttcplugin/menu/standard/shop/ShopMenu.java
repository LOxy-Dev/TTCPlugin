package fr.loxydev.ttcplugin.menu.standard.shop;

import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import fr.loxydev.ttcplugin.database.ItemDataHandler;
import fr.loxydev.ttcplugin.database.ShopDataHandler;
import fr.loxydev.ttcplugin.menu.Menu;
import fr.loxydev.ttcplugin.utils.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ShopMenu extends Menu {

    private final ArrayList<ItemDataHandler> itemsData = new ArrayList<>();
    private final ShopDataHandler shopData;

    public ShopMenu(int shopId) {
        this.shopData = new ShopDataHandler(shopId);
    }

    @Override
    public String getMenuName() {
        return shopData.getShopName();
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e, PlayerUtility playerUtility) {
        if (e.getCurrentItem() == FILLER_GLASS || e.getCurrentItem() == null) return;

        Player player = playerUtility.getPlayer();
        ItemDataHandler item = itemsData.get(e.getSlot());
        if (playerUtility.hasItem(item.getMaterial()))
            new ItemInterfaceMenu(item).open(player); // If pagination of shop is modified, index of document has to be updated
    }

    @Override
    public void setMenuItems(PlayerUtility playerUtility) {
        // Query and store Shop info
        final ArrayList<Material> itemList = shopData.getItemList();

        Bukkit.getScheduler().runTaskAsynchronously(TheTerrierCityPlugin.getPlugin(), new Runnable() {
            @Override
            public void run() {
                final ArrayList<ItemStack> inventoryItems = new ArrayList<>();
                for (int i = 0; i < itemList.size(); i++) {
                    Material material = itemList.get(i);

                    ItemDataHandler itemData = new ItemDataHandler(material);

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

                    Bukkit.getScheduler().scheduleSyncDelayedTask(TheTerrierCityPlugin.getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            int inInventory = 0;
                            for (ItemStack item : playerUtility.getPlayer().getInventory().getContents())
                                if (item != null) if (item.getType() == material) inInventory += item.getAmount();
                            itemLore.add(inInventory + " in you inventory");
                            inventoryItems.add(playerUtility.makeItem(material, itemLore));
                        }
                    });
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(TheTerrierCityPlugin.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        for (ItemStack is : inventoryItems) {
                            inventory.addItem(is);
                        }
                    }
                });

            }
        });
    }
}
