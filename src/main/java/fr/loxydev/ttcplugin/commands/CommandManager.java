package fr.loxydev.ttcplugin.commands;

import fr.loxydev.ttcplugin.commands.subcommands.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandManager implements CommandExecutor {

    private final ArrayList<SubCommand> subcommands = new ArrayList<>();

    public CommandManager() {
        // Add subcommands here
        subcommands.add(new HelpCommand());
        subcommands.add(new AdminCommand());
        subcommands.add(new RefreshCommand());
        subcommands.add(new BuyCommand());
        subcommands.add(new SignCommand());
        subcommands.add(new TestCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            if (args.length == 0) {
                HelpCommand help = new HelpCommand();
                help.perform(p, args);
            } else if (args.length > 0) {
                boolean isValidSubcommand = false;
                for (int i = 0; i < this.getSubcommands().size(); i++) {
                    if (args[0].equalsIgnoreCase(this.getSubcommands().get(i).getName())) {
                        isValidSubcommand = true;
                        // The subcommand matches the argument given in /ttc [subcommand]
                        this.getSubcommands().get(i).perform(p, args);

                        break;
                    }
                }
                if (!isValidSubcommand) {
                    p.sendMessage(ChatColor.RED + "[TTC] That is not a valid command.");
                    p.sendMessage(ChatColor.GRAY + "Do " + ChatColor.YELLOW + "/ttc help" + ChatColor.GRAY + " for more info.");
                }
                return true;
            }
        }

        return true;
    }

    public ArrayList<SubCommand> getSubcommands() {
        return subcommands;
    }
}
