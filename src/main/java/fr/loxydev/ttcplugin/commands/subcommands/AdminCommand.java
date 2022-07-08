package fr.loxydev.ttcplugin.commands.subcommands;

import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import fr.loxydev.ttcplugin.commands.SubCommand;
import fr.loxydev.ttcplugin.menu.PlayerMenuUtility;
import fr.loxydev.ttcplugin.menu.admin.AdminMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AdminCommand extends SubCommand {
    @Override
    public String getName() {
        return "admin";
    }

    @Override
    public String getDescription() {
        return "Open the GUI to manage server and player settings";
    }

    @Override
    public String getSyntax() {
        return "/ttc admin";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (player.hasPermission("ttc.admin")) {
            PlayerMenuUtility playerMenuUtility = TheTerrierCityPlugin.getPlayerMenuUtility(player);

            new AdminMenu().open(player);
        } else {
            player.sendMessage(ChatColor.RED + "This command is for admins.");
        }
    }
}
