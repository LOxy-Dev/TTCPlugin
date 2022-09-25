package fr.loxydev.ttcplugin.commands.subcommands;

import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import fr.loxydev.ttcplugin.commands.SubCommand;
import org.bukkit.entity.Player;

public class RefreshCommand extends SubCommand {
    @Override
    public String getName() {
        return "refresh";
    }

    @Override
    public String getDescription() {
        return "Refresh the player scoreboard";
    }

    @Override
    public String getSyntax() {
        return "/ttc refresh";
    }

    @Override
    public void perform(Player player, String[] args) {
        TheTerrierCityPlugin.getPlayerMenuUtility(player).getScoreboard().update();
    }
}
