package fr.loxydev.ttcplugin.listeners;

import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import fr.loxydev.ttcplugin.menu.Menu;
import fr.loxydev.ttcplugin.menu.PlayerMenuUtility;
import fr.loxydev.ttcplugin.menu.shop.TestShop;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.InventoryHolder;

public class MenuListener implements Listener {

    @EventHandler
    public void onNPCClick(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();

        if (!NBTEditor.getBoolean(entity, "NoAI")) return;

        Player player = event.getPlayer();

        switch (entity.getCustomName()) {
            case "Test Shop":
                new TestShop().open(player);
                break;
        }
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        // Make sure the player has a menu system object
        TheTerrierCityPlugin.getPlayerMenuUtility(player);
        // Get the player's ShopManagerMenu
        PlayerMenuUtility playerMenuUtility = TheTerrierCityPlugin.playerList.get(player);

        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof Menu menu) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null)
                return;

            menu.handleMenu(event, playerMenuUtility);
        }
    }
}
