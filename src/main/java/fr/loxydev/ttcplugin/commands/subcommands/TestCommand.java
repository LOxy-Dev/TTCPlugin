package fr.loxydev.ttcplugin.commands.subcommands;

import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import fr.loxydev.ttcplugin.commands.SubCommand;
import fr.loxydev.ttcplugin.utils.Hologram;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.ArrayList;

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
            /*// Test shop summon
            Villager v = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
            v.setCustomName("Test Shop");
            v.setSilent(true);
            v.setProfession(Villager.Profession.NITWIT);
            NBTEditor.set(v, true, "NoAI");
            */

            // Test hologram summon
            ArrayList<String> message = new ArrayList<>();
            message.add("This is a test hologram...");
            message.add("Here is a §4RED COLOR.");
            message.add("Are the lines correctly spaced???");
            message.add("I hope it works.");
            Hologram test = new Hologram(player.getLocation(), message);

            Bukkit.getScheduler().runTaskLater(TheTerrierCityPlugin.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    ArrayList<String> new_msg = new ArrayList<>();
                    new_msg.add("I just became updated!!!");

                    test.updateText(new_msg);
                }
            }, 20*5);

            Bukkit.getScheduler().runTaskLater(TheTerrierCityPlugin.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    test.remove();
                }
            }, 20*10);

        } else {
            player.sendMessage(ChatColor.RED + "This command is for admins.");
        }
    }
}
