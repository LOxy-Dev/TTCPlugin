package fr.loxydev.ttcplugin.menu.shop;

import fr.loxydev.ttcplugin.database.ItemDataHandler;
import fr.loxydev.ttcplugin.database.PlayerDataHandler;
import fr.loxydev.ttcplugin.database.ShopDataHandler;
import fr.loxydev.ttcplugin.menu.Menu;
import fr.loxydev.ttcplugin.menu.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ItemInterfaceMenu extends Menu {

    private final ItemDataHandler itemData;

    public ItemInterfaceMenu(ItemDataHandler itemData) {
        this.itemData = itemData;
    }

    @Override
    public String getMenuName() {
        return itemData.getShopName() + "> " + itemData.getMaterial().toString();
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent e, PlayerMenuUtility playerMenuUtility) {
        int itemInInv = playerMenuUtility.itemInInv(itemData.getMaterial());
        if (itemInInv == 0) {
            playerMenuUtility.getPlayer().closeInventory();
            return;
        }

        int nextLevelIn = itemData.getNextLevelIn();

        if (e.getCurrentItem().getType() == Material.PAPER) {
            int amount = Math.min(e.getCurrentItem().getAmount(), itemInInv);

            if (amount <= nextLevelIn) {
                sellItems(amount, playerMenuUtility);
            } else {
                sellItems(nextLevelIn, playerMenuUtility);
                playerMenuUtility.getPlayer().closeInventory();
            }
        }
        else if (e.getCurrentItem().getType() == Material.CHEST)
            if (itemInInv <= nextLevelIn) {
                sellItems(itemInInv, playerMenuUtility);
            } else {
                sellItems(nextLevelIn, playerMenuUtility);
                playerMenuUtility.getPlayer().closeInventory();
            }
    }

    @Override
    public void setMenuItems(PlayerMenuUtility playerMenuUtility) {
        // Create items' lore
        int price = itemData.getPrice();
        int nextLevelIn = itemData.getNextLevelIn();

        ArrayList<String> iLore = new ArrayList<>();
        iLore.add("Sell " + itemData.getMaterial().toString() + " for " + price + " points per item.");
        iLore.add("\n");
        iLore.add("Next level in " + nextLevelIn + " items.");

        ArrayList<String> i1Lore = new ArrayList<>();
        i1Lore.add("Sell 1" + itemData.getMaterial().name() + " for " + price + " points.");

        ArrayList<String> i10Lore = new ArrayList<>();
        if (10 > nextLevelIn) {
            i10Lore.add("You will sell only " + nextLevelIn + " items for " + price*nextLevelIn + " points.");
            i10Lore.add("\n");
            i10Lore.add("This sale will change the level.");
        }
        else {
            i10Lore.add("Sell 10 items for " + price*10 + "points.");
        }

        ArrayList<String> i64Lore = new ArrayList<>();
        if (64 > nextLevelIn) {
            i64Lore.add("You will sell only " + nextLevelIn + " items for " + price*nextLevelIn + " points.");
            i64Lore.add("\n");
            i64Lore.add("This sale will change the level.");
        }
        else {
            i64Lore.add("Sell 64 items for " + price*64 + " points.");
        }

        ArrayList<String> iAllLore = new ArrayList<>();
        if (playerMenuUtility.itemInInv(itemData.getMaterial()) > nextLevelIn) {
            iAllLore.add("You will sell only " + nextLevelIn + " items for " + price*nextLevelIn + " points.");
            iAllLore.add("\n");
            iAllLore.add("This sale will change the level.");
        }
        else {
            iAllLore.add("Sell all items in your inventory for " + price* playerMenuUtility.itemInInv(itemData.getMaterial()) + " points.");
        }

        // Place items
        inventory.setItem(2, playerMenuUtility.makeItem(itemData.getMaterial(), iLore));
        inventory.setItem(3, playerMenuUtility.makeItem(Material.PAPER, "Sell 1 item", i1Lore));
        inventory.setItem(4, playerMenuUtility.makeItem(Material.PAPER, "Sell 10 items", 10, i10Lore));
        inventory.setItem(5, playerMenuUtility.makeItem(Material.PAPER, "Sell a stack", 64, i64Lore));
        inventory.setItem(6, playerMenuUtility.makeItem(Material.CHEST, "Sell all your items", iAllLore));
    }

    private void sellItems(int amount, PlayerMenuUtility playerMenuUtility) {
        for (ItemStack item : playerMenuUtility.getPlayer().getInventory().getContents()) {
            if (amount <= 0) break;

            if (item != null) if (item.getType() == itemData.getMaterial()) {
                if (item.getAmount() >= amount) { // If there is enough item to delete in the slot
                    item.setAmount(item.getAmount()-amount);
                    break;
                }
                else {
                    amount -= item.getAmount();
                    item.setAmount(0);
                }
            }
        }

        int totalSale = amount * itemData.getPrice();

        PlayerDataHandler playerData = playerMenuUtility.getPlayerData();
        playerData.addPoints(totalSale);
        playerMenuUtility.getPlayer().sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + "[TTC] You sold " + amount + " " + itemData.getItemName() + " for " + totalSale + " points.");
        playerMenuUtility.getPlayer().playSound(playerMenuUtility.getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

        itemData.increaseSales(amount);

        ShopDataHandler shopData = new ShopDataHandler(itemData.getShopName());
        shopData.increasePurchases(amount);

        playerMenuUtility.getScoreboard().update();
    }
}
