package fr.loxydev.ttcplugin.commands.subcommands;

import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import fr.loxydev.ttcplugin.commands.SubCommand;
import fr.loxydev.ttcplugin.database.FlatDataHandler;
import fr.loxydev.ttcplugin.database.PlayerDataHandler;
import fr.loxydev.ttcplugin.utils.Hologram;
import fr.loxydev.ttcplugin.utils.PlayerUtility;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class BuyCommand extends SubCommand {
    @Override
    public String getName() {
        return "buy";
    }

    @Override
    public String getDescription() {
        return "achète un appartement";
    }

    @Override
    public String getSyntax() {
        return "/ttc buy <id>";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length != 2) {
            player.sendMessage(ChatColor.RED + "Wrong syntax");
            return;
        }

        PlayerUtility playerUtil = TheTerrierCityPlugin.getPlayerUtility(player);
        FlatDataHandler flatData = new FlatDataHandler(Integer.parseInt(args[1]));

        if (flatData.buy(playerUtil)) {
            player.sendMessage("[TTC] Vous avez acheté l'appartement #" + args[1]);
            playerUtil.getScoreboard().update();

            // TODO Switch to hash map to find faster the correct hologram
            for (Hologram h : TheTerrierCityPlugin.holograms) {
                if (!h.getId().equals("flat_"+args[1]))
                    continue;

                ArrayList<String> text = new ArrayList<>();

                text.add("Appartement §o#" + args[1]);

                try {
                    PlayerDataHandler playerData = new PlayerDataHandler(UUID.fromString(flatData.getOwner()));
                    text.add(playerData.getTeam().getColor() + playerData.getPlayerName());
                } catch (IllegalArgumentException e) {
                    text.add("§l" + flatData.getOwner());
                }
                h.updateText(text);
                break;
            }
        } else {
            player.sendMessage("[TTC] Vous ne pouvez pas acheter l'appartement demandé");
        }
    }
}
