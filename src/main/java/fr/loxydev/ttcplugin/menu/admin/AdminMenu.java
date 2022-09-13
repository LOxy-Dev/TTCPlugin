package fr.loxydev.ttcplugin.menu.admin;

import fr.loxydev.ttcplugin.menu.Menu;
import fr.loxydev.ttcplugin.menu.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class AdminMenu extends Menu {
    @Override
    public String getMenuName() {
        return "TTC Admin";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e, PlayerMenuUtility playerMenuUtility) {
        if (e.getCurrentItem() == FILLER_GLASS || e.getCurrentItem() == null) return;

        Player player = playerMenuUtility.getPlayer();

        if (e.getCurrentItem().getType() == Material.BARRIER) player.closeInventory();
    }

    @Override
    public void setMenuItems(PlayerMenuUtility playerMenuUtility) {
        // Close menu Item
        inventory.setItem(22, makeItem(Material.BARRIER, "Close menu"));

        setFillerGlass();
    }
}
