package fr.loxydev.ttcplugin.commands.subcommands;

import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import fr.loxydev.ttcplugin.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SignCommand extends SubCommand {
    @Override
    public String getName() {
        return "sign";
    }

    @Override
    public String getDescription() {
        return "tp to next sign";
    }

    @Override
    public String getSyntax() {
        return "/ttc sign";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (!player.hasPermission("ttc.admin"))
            return;

        ArrayList<Chunk> chunks = new ArrayList<>();

        for (int i = -42; i < 42; i++) {
            for (int j = -42; j < 42; j++) {
                chunks.add(TheTerrierCityPlugin.lobby.getChunkAt(i, j));
            }
        }

        for (Chunk chunk : chunks) {
            for (BlockState tileEntity : chunk.getTileEntities()) {
                if (tileEntity.getType() != Material.OAK_SIGN)
                    continue;

                player.sendMessage(tileEntity.getType().name() + ": " + tileEntity.getX() + ", " + tileEntity.getY() + ", " + tileEntity.getZ());
                Bukkit.getLogger().info(tileEntity.getType().name() + ": " + tileEntity.getX() + ", " + tileEntity.getY() + ", " + tileEntity.getZ());
                player.teleport(tileEntity.getLocation());
                return;
            }
        }
    }
}
