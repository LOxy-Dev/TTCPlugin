package fr.loxydev.ttcplugin.menu.admin;

import fr.loxydev.ttcplugin.menu.Menu;
import fr.loxydev.ttcplugin.utils.PlayerUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerSettingsAdminMenu extends Menu {
    @Override
    public String getMenuName() {
        return "TTC Admin > Player Settings";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e, PlayerUtility playerUtility) {
        Player p = playerUtility.getPlayer();
        if (e.getCurrentItem().getType().equals(Material.BARRIER)) {
            new ManagePlayerAdminMenu().open(p);
        }
    }

    @Override
    public void setMenuItems(PlayerUtility playerUtility) {
        ItemStack back = makeItem(Material.BARRIER, ChatColor.RED + "Back");

        inventory.setItem(22, back);

        setFillerGlass();
    }
}
