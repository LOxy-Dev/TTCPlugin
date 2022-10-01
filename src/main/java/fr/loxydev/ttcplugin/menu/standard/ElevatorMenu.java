package fr.loxydev.ttcplugin.menu.standard;

import fr.loxydev.ttcplugin.menu.Menu;
import fr.loxydev.ttcplugin.utils.PlayerUtility;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ElevatorMenu extends Menu {
    @Override
    public String getMenuName() {
        return "[TTC] Elevator";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent e, PlayerUtility playerUtility) {
        Player player = playerUtility.getPlayer();
        Location destination = player.getLocation();
        switch (e.getSlot()) {
            case 0 -> destination.setY(59);
            case 1 -> destination.setY(111);
            case 2 -> destination.setY(160);
            case 3 -> destination.setY(214);

            default -> throw new IllegalStateException("Unexpected value: " + e.getSlot());
        }
        player.teleport(destination);
    }

    @Override
    public void setMenuItems(PlayerUtility playerUtility) {
        inventory.setItem(0, makeItem(Material.DIRT, "Ground level"));
        inventory.setItem(1, makeItem(Material.GOLD_NUGGET, "Shop level 1"));
        inventory.setItem(2, makeItem(Material.DIAMOND_BLOCK, "Shop level 2"));
        inventory.setItem(3, makeItem(Material.ITEM_FRAME, "Statistics"));
    }
}
