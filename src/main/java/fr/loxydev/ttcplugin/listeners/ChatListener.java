package fr.loxydev.ttcplugin.listeners;

import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import fr.loxydev.ttcplugin.utils.PlayerUtility;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.setDisplayName(TheTerrierCityPlugin.getPlayerUtility(player).getTeam().getColor() + player.getName());
        player.setCustomName(player.getDisplayName());
        player.setPlayerListName(TheTerrierCityPlugin.getPlayerUtility(player).getTeam().getColor() + player.getName());
        player.setCustomNameVisible(true);

        event.setJoinMessage("[" + ChatColor.GREEN + "+" + ChatColor.RESET + "] " + ChatColor.ITALIC + ChatColor.BOLD + player.getDisplayName());

        // Every time a player joins, assign them a personal MenuUtility object
        TheTerrierCityPlugin.getPlayerUtility(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        event.setQuitMessage("[" + ChatColor.DARK_RED + "-" + ChatColor.RESET + "] " + ChatColor.ITALIC + ChatColor.BOLD + player.getDisplayName());

        // Since the player left, get rid of the ShopManagerMenu objects for that player
        TheTerrierCityPlugin.playerList.remove(player);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        PlayerUtility player = TheTerrierCityPlugin.getPlayerUtility(event.getPlayer());

        // Team chat
        if (event.getMessage().startsWith("!")) {
            player.getTeam().sendMessage("[" + player.getTeam().getColor() + player.getTeam().getName() + ChatColor.RESET + "] " + player.getPlayer().getName() + ": " + event.getMessage().replaceFirst("!", ""));
            event.setCancelled(true);
            return;
        }

        // Chat global
        String prefix = player.getTeam().getColor() + "" + ChatColor.BOLD + player.getTeam().getTag() + ChatColor.RESET + " ";

        event.setFormat(prefix + event.getPlayer().getName() + "> " + event.getMessage());
    }
}
