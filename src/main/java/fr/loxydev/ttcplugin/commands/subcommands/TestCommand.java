package fr.loxydev.ttcplugin.commands.subcommands;

import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import fr.loxydev.ttcplugin.commands.SubCommand;
import fr.loxydev.ttcplugin.menu.PlayerMenuUtility;
import fr.loxydev.ttcplugin.menu.shop.TestShop;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TestCommand extends SubCommand {
    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getDescription() {
        return "test";
    }

    @Override
    public String getSyntax() {
        return "/ttc test";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (player.hasPermission("ttc.admin")) {
            PlayerMenuUtility playerMenuUtility = TheTerrierCityPlugin.getPlayerMenuUtility(player);

            new TestShop().open(player);
        } else {
            player.sendMessage(ChatColor.RED + "This command is for admins.");
        }
    }
}
