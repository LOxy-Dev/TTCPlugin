package fr.loxydev.ttcplugin.commands.subcommands;

import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import fr.loxydev.ttcplugin.commands.SubCommand;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CityCommand extends SubCommand {
    @Override
    public String getName() {
        return "city";
    }

    @Override
    public String getDescription() {
        return "Teleport you to the spawn";
    }

    @Override
    public String getSyntax() {
        return "/ttc city";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (player.getWorld() == TheTerrierCityPlugin.lobby)
            return;

        player.teleport(new Location(TheTerrierCityPlugin.lobby, 0, 59, -15));
    }
}
