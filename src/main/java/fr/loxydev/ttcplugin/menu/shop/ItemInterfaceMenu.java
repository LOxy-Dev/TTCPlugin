package fr.loxydev.ttcplugin.menu.shop;

import fr.loxydev.ttcplugin.database.ItemDataHandler;
import fr.loxydev.ttcplugin.menu.Menu;
import fr.loxydev.ttcplugin.menu.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ItemInterfaceMenu extends Menu {

    private ItemDataHandler itemData;

    public ItemInterfaceMenu(ItemDataHandler itemData) {
        this.itemData = itemData;
    }

    @Override
    public String getMenuName() {
        return itemData.getShopName() + "> " + getMaterial().name();
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent e, PlayerMenuUtility playerMenuUtility) {

    }

    @Override
    public void setMenuItems(PlayerMenuUtility playerMenuUtility) {
        // Create items' lore
        int price = itemData.getPrice();
        int nextLevelIn = itemData.getNextLevelIn();

        ArrayList<String> iLore = new ArrayList<>();
        iLore.add("Sell " + getMaterial().name() + " for " + price + " points per item.");
        iLore.add("\n");
        iLore.add("Next level in " + nextLevelIn + " items.");

        ArrayList<String> i1Lore = new ArrayList<>();
        i1Lore.add("Sell 1" + getMaterial().name() + " for " + price + "points.");

        ArrayList<String> i10Lore = new ArrayList<>();
        if (10 > nextLevelIn) {
            i10Lore.add("You will sell only " + nextLevelIn + " items for " + price * nextLevelIn + "points.");
            i10Lore.add("\n");
            i10Lore.add("This sale will change the level.");
        }
        else {
            i10Lore.add("Sell 10 items for " + price*10 + "points.");
        }

        ArrayList<String> i64Lore = new ArrayList<>();
        if (64 > nextLevelIn) {
            i10Lore.add("You will sell only " + nextLevelIn + " items for " + price*nextLevelIn + "points.");
            i10Lore.add("\n");
            i10Lore.add("This sale will change the level.");
        }
        else {
            i10Lore.add("Sell 64 items for " + price*64 + "points.");
        }

        ArrayList<String> iAllLore = new ArrayList<>();
        if (getItemsInInv(playerMenuUtility) > nextLevelIn) {
            i10Lore.add("You will sell only " + nextLevelIn + " items for " + price*nextLevelIn + "points.");
            i10Lore.add("\n");
            i10Lore.add("This sale will change the level.");
        }
        else {
            i10Lore.add("Sell all items in your inventory for " + price*getItemsInInv(playerMenuUtility) + "points.");
        }

        // Place items
        inventory.setItem(2, playerMenuUtility.makeItem(getMaterial(), iLore));
        inventory.setItem(3, playerMenuUtility.makeItem(Material.PAPER, "Sell 1 item", i1Lore));
        inventory.setItem(4, playerMenuUtility.makeItem(Material.PAPER, "Sell 10 items", 10, i10Lore));
        inventory.setItem(5, playerMenuUtility.makeItem(Material.PAPER, "Sell a stack", 64, i64Lore));
        inventory.setItem(6, playerMenuUtility.makeItem(Material.CHEST, "Sell all your items", iAllLore));
    }

    private int getItemsInInv(PlayerMenuUtility playerMenuUtility) {
        int inInventory = 0;
        for (ItemStack item : playerMenuUtility.getPlayer().getInventory().getContents())
            if (item != null) if (item.getType() == getMaterial()) inInventory += item.getAmount();

        return inInventory;
    }

    private Material getMaterial()
    {
        return Material.getMaterial(itemData.getItemName());
    }

    private void sellItems(int amount, PlayerMenuUtility playerMenuUtility) {
        int totalSale = 0;
        for (ItemStack item : playerMenuUtility.getPlayer().getInventory().getContents()) {
            if (item != null) if (item.getType() == getMaterial()) {
                if (item.getAmount() >= amount) { // If there is enough item to delete in the slot
                    totalSale += itemData.getPrice() * item.getAmount();
                    item.setAmount(item.getAmount()-amount);
                    break;
                }
                else {
                    totalSale += itemData.getPrice() * item.getAmount();
                    item.setAmount(0);
                    amount -= item.getAmount();
                }
            }
        }

        // TODO update player's data
        // TODO Update item's data
        // TODO update shop's data
    }
}
