package fr.loxydev.ttcplugin.listeners;

import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import fr.loxydev.ttcplugin.database.FlatDataHandler;
import fr.loxydev.ttcplugin.menu.standard.ElevatorMenu;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.Objects;

public class TeleportListener implements Listener {

    @EventHandler
    public void onCrouched(PlayerToggleSneakEvent event) {
        if (!event.isSneaking()) return;

        Player player = event.getPlayer();
        Location pos = player.getLocation();

        if (pos.getWorld() != TheTerrierCityPlugin.lobby)
            return;

        if (Math.abs(pos.getBlockX()) <= 1 && Math.abs(pos.getBlockZ()) <= 1) {
            new ElevatorMenu().open(player);
            return;
        }

        if (FlatDataHandler.isTPIn(pos.getBlock().getLocation())) {
            if (FlatDataHandler.getFlatDataByTpCoords(pos.getBlock().getLocation()).getOwner() == null) {
                player.teleport(FlatDataHandler.getLinkedLoc(pos));
            } else {
                if (Objects.equals(FlatDataHandler.getFlatDataByTpCoords(pos.getBlock().getLocation()).getOwner(), player.getUniqueId().toString())) {
                    player.teleport(FlatDataHandler.getLinkedLoc(pos));
                } else {
                }
            }
            return;
        }
        if (FlatDataHandler.isTPOut(pos.getBlock().getLocation())) {
            player.teleport(FlatDataHandler.getLinkedLoc(pos));
        }
    }
}
