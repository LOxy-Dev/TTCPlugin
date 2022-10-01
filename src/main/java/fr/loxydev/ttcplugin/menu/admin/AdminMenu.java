package fr.loxydev.ttcplugin.menu.admin;

import fr.loxydev.ttcplugin.database.TeamDataHandler;
import fr.loxydev.ttcplugin.menu.Menu;
import fr.loxydev.ttcplugin.utils.PlayerUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

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
    public void handleMenu(InventoryClickEvent e, PlayerUtility playerMenuUtility) {
        if (e.getCurrentItem() == FILLER_GLASS || e.getCurrentItem() == null) return;

        Player player = playerMenuUtility.getPlayer();

        if (e.getCurrentItem().getType() == Material.BARRIER) player.closeInventory();

        if (e.getCurrentItem().getType() == Material.EGG) new SummonMenu().open(player);

        if (e.getCurrentItem().getType() == Material.RED_BED) /* TODO proper admin management*/ {
            TeamDataHandler admTeam = new TeamDataHandler("AdminTeam");
            admTeam.addPlayer(playerMenuUtility);
        }
    }

    @Override
    public void setMenuItems(PlayerUtility playerMenuUtility) {
        // Close menu Item
        inventory.setItem(22, makeItem(Material.BARRIER, "Close menu"));
        // Spawn NPC Item
        inventory.setItem(0, makeItem(Material.EGG, "Summon NPC"));
        // Manage Teams
        inventory.setItem(1, makeItem(Material.RED_BED, "Manage teams"));

        setFillerGlass();
    }
}
