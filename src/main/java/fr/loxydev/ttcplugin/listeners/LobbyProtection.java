package fr.loxydev.ttcplugin.listeners;

import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import fr.loxydev.ttcplugin.database.FlatDataHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public class LobbyProtection implements Listener {

    @EventHandler
    public void onBlockBuild(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("ttc.admin"))
            return;

        if (player.getWorld() != TheTerrierCityPlugin.lobby)
            return;

        // Flat management
        // Find if player owns a flat or not
        if (FlatDataHandler.ownsFlat(TheTerrierCityPlugin.getPlayerUtility(player)) == -1) {
            event.setCancelled(true);
            return;
        }
        // Get coords
        FlatDataHandler flatData = new FlatDataHandler(FlatDataHandler.ownsFlat(TheTerrierCityPlugin.getPlayerUtility(player)));
        ArrayList<Location> corners = flatData.getCorners();

        Location blocLoc = event.getBlock().getLocation();

        if ((Math.min(corners.get(0).getBlockX(), corners.get(1).getBlockX()) <= blocLoc.getBlockX()) && (blocLoc.getBlockX() <= Math.max(corners.get(0).getBlockX(), corners.get(1).getBlockX()))) {
            if ((Math.min(corners.get(0).getBlockY(), corners.get(1).getBlockY()) <= blocLoc.getBlockY()) && (blocLoc.getBlockY() <= Math.max(corners.get(0).getBlockY(), corners.get(1).getBlockY()))) {
                if ((Math.min(corners.get(0).getBlockZ(), corners.get(1).getBlockZ()) <= blocLoc.getBlockZ()) && (blocLoc.getBlockZ() <= Math.max(corners.get(0).getBlockZ(), corners.get(1).getBlockZ()))) {
                    event.setCancelled(false);

                    return;
                }
            }
        }

        player.sendMessage(ChatColor.RED + "[TTC Lobby] You can't build here");
        event.setCancelled(true);
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("ttc.admin"))
            return;

        if (player.getWorld() != TheTerrierCityPlugin.lobby)
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("ttc.admin"))
            return;

        if (player.getWorld() != TheTerrierCityPlugin.lobby)
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onEggSpawned(PlayerEggThrowEvent event) {
        if (event.getPlayer().getWorld() != TheTerrierCityPlugin.lobby)
            return;

        event.setHatching(false);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("ttc.admin")) {
            return;
        }

        if (player.getWorld() != TheTerrierCityPlugin.lobby) {
            return;
        }

        // Flat management
        // Find if player owns a flat or not
        if (FlatDataHandler.ownsFlat(TheTerrierCityPlugin.getPlayerUtility(player)) == -1) {
            event.setCancelled(true);
            return;
        }
        // Get coords
        FlatDataHandler flatData = new FlatDataHandler(FlatDataHandler.ownsFlat(TheTerrierCityPlugin.getPlayerUtility(player)));
        ArrayList<Location> corners = flatData.getCorners();

        Location blocLoc = event.getBlock().getLocation();

        if ((Math.min(corners.get(0).getBlockX(), corners.get(1).getBlockX()) <= blocLoc.getBlockX()) && (blocLoc.getBlockX() <= Math.max(corners.get(0).getBlockX(), corners.get(1).getBlockX()))) {
            if ((Math.min(corners.get(0).getBlockY(), corners.get(1).getBlockY()) <= blocLoc.getBlockY()) && (blocLoc.getBlockY() <= Math.max(corners.get(0).getBlockY(), corners.get(1).getBlockY()))) {
                if ((Math.min(corners.get(0).getBlockZ(), corners.get(1).getBlockZ()) <= blocLoc.getBlockZ()) && (blocLoc.getBlockZ() <= Math.max(corners.get(0).getBlockZ(), corners.get(1).getBlockZ()))) {
                    event.setCancelled(false);
                    return;
                }
            }
        }

        player.sendMessage(ChatColor.RED + "[TTC Lobby] You can't build here");
        event.setCancelled(true);
    }

    @EventHandler
    public void onExplosion(ExplosionPrimeEvent event) {
        if (event.getEntity().getWorld() != TheTerrierCityPlugin.lobby)
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onItemFrameInteract(EntityDamageByEntityEvent event) {
        if (event.getEntity().getWorld() != TheTerrierCityPlugin.lobby)
            return;

        event.setCancelled(true);
    }
}
