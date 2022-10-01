package fr.loxydev.ttcplugin.menu;

import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import fr.loxydev.ttcplugin.utils.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public abstract class Menu implements InventoryHolder{

    protected Inventory inventory;
    protected ItemStack FILLER_GLASS = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

    public Menu() {
        ItemMeta filler_meta = FILLER_GLASS.getItemMeta();
        filler_meta.setDisplayName(" ");
        FILLER_GLASS.setItemMeta(filler_meta);
    }

    public abstract String getMenuName();

    public abstract int getSlots();

    public abstract void handleMenu(InventoryClickEvent e, PlayerUtility playerMenuUtility);

    public abstract void setMenuItems(PlayerUtility playerMenuUtility);

    public void open(Player p) {
        inventory = Bukkit.createInventory(this, getSlots(), getMenuName());

        this.setMenuItems(TheTerrierCityPlugin.getPlayerUtility(p));

        p.openInventory(inventory);
    }

    public void setFillerGlass() {
        for (int i =0; i < getSlots(); i++) {
            if (inventory.getItem(i) == null)
                inventory.setItem(i, FILLER_GLASS);
        }
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

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
