package fr.loxydev.ttcplugin.commands.subcommands;

import fr.loxydev.ttcplugin.commands.CommandManager;
import fr.loxydev.ttcplugin.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HelpCommand extends SubCommand {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Show this menu";
    }

    @Override
    public String getSyntax() {
        return "/ttc help";
    }

    @Override
    public void perform(Player player, String[] args) {
        CommandManager commandManager = new CommandManager();
        player.sendMessage(ChatColor.DARK_RED + "=======" + ChatColor.BLUE + ChatColor.BOLD + "The Terrier" + ChatColor.RED + ChatColor.ITALIC + " City" + ChatColor.YELLOW + " Commands" + ChatColor.DARK_RED + "=======");
        for (int i = 0; i < commandManager.getSubcommands().size(); i++) {
            player.sendMessage(ChatColor.YELLOW + commandManager.getSubcommands().get(i).getSyntax() + " - " + ChatColor.GRAY + ChatColor.ITALIC + commandManager.getSubcommands().get(i).getDescription());
        }
        player.sendMessage(ChatColor.DARK_RED + "======================================");
    }
}
