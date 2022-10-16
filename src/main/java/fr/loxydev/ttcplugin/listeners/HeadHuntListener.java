package fr.loxydev.ttcplugin.listeners;

import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import fr.loxydev.ttcplugin.database.HeadDataHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class HeadHuntListener implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        Location loc = event.getClickedBlock().getLocation();

        if (event.getClickedBlock().getType() == Material.PLAYER_HEAD) {
            HeadDataHandler head = HeadDataHandler.getHeadByPos(loc);

            if (head == null) return;

            head.clicked(TheTerrierCityPlugin.getPlayerUtility(player));
        }
    }

}