package fr.loxydev.ttcplugin.listeners;

import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import fr.loxydev.ttcplugin.database.ShopDataHandler;
import fr.loxydev.ttcplugin.menu.Menu;
import fr.loxydev.ttcplugin.utils.PlayerUtility;
import fr.loxydev.ttcplugin.menu.standard.shop.ShopMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.InventoryHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MenuListener implements Listener {

    @EventHandler
    public void onNPCClick(PlayerInteractEntityEvent event) {

        Entity entity = event.getRightClicked();

        if (!entity.isSilent()) return;

        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskAsynchronously(TheTerrierCityPlugin.getPlugin(), new Runnable() {
            @Override
            public void run() {
                try (Connection conn = TheTerrierCityPlugin.dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                        "SELECT shopid FROM shops WHERE name = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE
                )) {
                    stmt.setString(1, entity.getCustomName());

                    ResultSet rs = stmt.executeQuery();

                    if (rs.first()) {

                        final int shopId = rs.getInt(1);

                        Bukkit.getScheduler().scheduleSyncDelayedTask(TheTerrierCityPlugin.getPlugin(), new Runnable() {
                            @Override
                            public void run() {
                                if (new ShopDataHandler(shopId).isOpen()) {
                                    new ShopMenu(shopId).open(player);
                                } else {
                                    player.sendMessage(ChatColor.RED + "Ce magasin est fermé par les administrateurs.");
                                }
                            }
                        });
                    }
                } catch (SQLException e) {
                    Bukkit.getLogger().info("Couldn't retrieve shop named " + entity.getCustomName());
                }
            }
        });
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        // Make sure the player has a menu system object
        TheTerrierCityPlugin.getPlayerUtility(player);
        PlayerUtility playerUtility = TheTerrierCityPlugin.playerList.get(player);

        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof Menu menu) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null)
                return;

            menu.handleMenu(event, playerUtility);
        }
    }
}
