package fr.loxydev.ttcplugin.menu.admin;

import fr.loxydev.ttcplugin.menu.Menu;
import fr.loxydev.ttcplugin.menu.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

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

    }

    @Override
    public void setMenuItems(PlayerMenuUtility playerMenuUtility) {
        ItemStack close = new ItemStack(Material.BARRIER);

        inventory.setItem(22, close);

        setFillerGlass();
    }
}
