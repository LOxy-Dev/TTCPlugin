package fr.loxydev.ttcplugin.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerMenuUtility {

    private Menu lastMenu;

    private Player p;
    private String shopID;

    private Player playerToAdd;
    private UUID uuidToRemove;
    private UUID uuidToManage;

    public PlayerMenuUtility(Player p) {
        this.p = p;
    }

    public ItemStack makeItem(Material material, String name) {
        ItemStack is = new ItemStack(material);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        is.setItemMeta(im);

        return is;
    }

    public ItemStack makeItem(Material material, String name, ArrayList<String> lore) {
        ItemStack is = new ItemStack(material);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        im.setLore(lore);
        is.setItemMeta(im);

        return is;
    }

    public ItemStack makeItem(Material material, String name, int amount, ArrayList<String> lore) {
        ItemStack is = new ItemStack(material, amount);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        im.setLore(lore);
        is.setItemMeta(im);

        return is;
    }

    public ItemStack makeItem(Material material, ArrayList<String> lore) {
        ItemStack is = new ItemStack(material);
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);

        return is;
    }

    public Player getPlayer() {
        return p;
    }

    public Menu getLastMenu() {
        return lastMenu;
    }

    public void setLastMenu(Menu lastMenu) {
        this.lastMenu = lastMenu;
    }

    public String getShopID() {
        return shopID;
    }

    public Player getPlayerToAdd() {
        return playerToAdd;
    }

    public void setPlayerToAdd(Player playerToAdd) {
        this.playerToAdd = playerToAdd;
    }

    public UUID getUuidToRemove() {
        return uuidToRemove;
    }

    public void setUuidToRemove(UUID uuidToRemove) {
        this.uuidToRemove = uuidToRemove;
    }

    public UUID getUuidToManage() {
        return uuidToManage;
    }

    public void setUuidToManage(UUID uuidToManage) {
        this.uuidToManage = uuidToManage;
    }

    public int itemInInv(Material item) {
        int amount = 0;
        for (ItemStack stack : p.getInventory().getContents())
            if (stack != null) if (stack.getType() == item) amount += stack.getAmount();

        return amount;
    }

    public boolean hasItem(Material item) {
        for (ItemStack stack : p.getInventory().getContents())
            if (stack != null) if (stack.getType() == item) return true;

        return false;
    }
}
