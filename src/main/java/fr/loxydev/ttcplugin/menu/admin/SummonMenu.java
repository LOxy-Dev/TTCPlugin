package fr.loxydev.ttcplugin.menu.admin;

import fr.loxydev.ttcplugin.menu.Menu;
import fr.loxydev.ttcplugin.menu.PlayerMenuUtility;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SummonMenu extends Menu {

    @Override
    public String getMenuName() {
        return "Summon a NPC";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e, PlayerMenuUtility playerMenuUtility) {
        String shopName;
        Player p = playerMenuUtility.getPlayer();
        switch (e.getSlot()) {
            case 0 -> shopName = "Trash";
            case 1 -> shopName = "Culture";
            case 2 -> shopName = "Material";
            case 3 -> shopName = "Mob";
            case 4 -> shopName = "Manufactured";
            case 5 -> shopName = "Redstone";
            case 6 -> shopName = "Dimensional";
            case 7 -> shopName = "Legendary";
            default -> { return; }
        }

        Villager v = (Villager) p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
        v.setCustomName(shopName);
        v.setSilent(true);
        v.setProfession(Villager.Profession.NITWIT);
        NBTEditor.set(v, true, "NoAI");
    }

    @Override
    public void setMenuItems(PlayerMenuUtility playerMenuUtility) {
        inventory.setItem(0, makeItem(Material.DIRT, "Trash Shopkeeper"));
        inventory.setItem(1, makeItem(Material.WHEAT, "Culture Shopkeeper"));
        inventory.setItem(2, makeItem(Material.IRON_ORE, "Material Shopkeeper"));
        inventory.setItem(3, makeItem(Material.GUNPOWDER, "Mob Loots Shopkeeper"));
        inventory.setItem(4, makeItem(Material.CRAFTING_TABLE, "Manufactured Shopkeeper"));
        inventory.setItem(5, makeItem(Material.REDSTONE, "Redstone Shopkeeper"));
        inventory.setItem(6, makeItem(Material.NETHERRACK, "Dimensional Shopkeeper"));
        inventory.setItem(7, makeItem(Material.NETHER_STAR, "Legendary Shopkeeper"));
    }
}
