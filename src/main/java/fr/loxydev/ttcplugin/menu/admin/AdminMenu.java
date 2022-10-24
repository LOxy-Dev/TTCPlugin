package fr.loxydev.ttcplugin.menu.admin;

import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import fr.loxydev.ttcplugin.database.TeamDataHandler;
import fr.loxydev.ttcplugin.menu.Menu;
import fr.loxydev.ttcplugin.utils.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;

public class AdminMenu extends Menu {
    @Override
    public String getMenuName() {
        return "TTC Admin";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e, PlayerUtility playerUtility) {
        if (e.getCurrentItem() == FILLER_GLASS || e.getCurrentItem() == null) return;

        Player player = playerUtility.getPlayer();

        if (e.getCurrentItem().getType() == Material.BARRIER) player.closeInventory();

        if (e.getCurrentItem().getType() == Material.EGG) new SummonMenu().open(player);

        if (e.getCurrentItem().getType() == Material.RED_BED) /* TODO proper admin management*/ {
            TeamDataHandler admTeam = new TeamDataHandler("AdminTeam");
            admTeam.addPlayer(playerUtility);
        }

        if (e.getCurrentItem().getType() == Material.OAK_SIGN) {
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

    @Override
    public void setMenuItems(PlayerUtility playerUtility) {
        // Close menu Item
        inventory.setItem(22, makeItem(Material.BARRIER, "Close menu"));
        // Spawn NPC Item
        inventory.setItem(0, makeItem(Material.EGG, "Summon NPC"));
        // Manage Teams
        inventory.setItem(1, makeItem(Material.RED_BED, "Manage teams"));
        // Scan signs position in lobby
        inventory.setItem(2, makeItem(Material.OAK_SIGN, "Scan for signs /!\\ VERY INTENSIVE"));

        // Info to remember
        inventory.setItem(17, makeItem(Material.LEATHER_BOOTS, "Jump: 9000 50 0"));

        setFillerGlass();
    }
}
