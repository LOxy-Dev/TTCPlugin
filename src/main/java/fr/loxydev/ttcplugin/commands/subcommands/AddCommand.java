package fr.loxydev.ttcplugin.commands.subcommands;

import fr.loxydev.ttcplugin.commands.SubCommand;
import fr.loxydev.ttcplugin.database.ItemDataHandler;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class AddCommand extends SubCommand {
    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "Add the held item in the database with default values.";
    }

    @Override
    public String getSyntax() {
        return "/ttc add <shopid>";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (!player.hasPermission("ttc.admin")) {
            return;
        }

        ArrayList<int[]> price = new ArrayList<>();
        price.add(new int[]{64,10});
        price.add(new int[]{500,5});
        price.add(new int[]{-1,1});
        ItemDataHandler.createItemData(player.getInventory().getItemInMainHand().getType(), Integer.parseInt(args[1]), price);
    }
}
