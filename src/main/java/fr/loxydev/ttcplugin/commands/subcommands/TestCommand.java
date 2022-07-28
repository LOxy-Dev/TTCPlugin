package fr.loxydev.ttcplugin.commands.subcommands;

import fr.loxydev.ttcplugin.commands.SubCommand;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

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
            if(player.hasPermission("test.setvillager")) {
                Villager v = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
                v.setCustomName("Test Shop");
                v.setSilent(true);
                v.setProfession(Villager.Profession.NITWIT);
                NBTEditor.set(v, true, "NoAI");
            }
        } else {
            player.sendMessage(ChatColor.RED + "This command is for admins.");
        }
    }
}
