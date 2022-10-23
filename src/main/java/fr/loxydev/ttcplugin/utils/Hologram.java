package fr.loxydev.ttcplugin.utils;

import fr.loxydev.ttcplugin.TheTerrierCityPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;

public class Hologram {

    private final String id;
    private final ArrayList<ArmorStand> armorStands;
    private final Location loc;

    public Hologram(Location loc, ArrayList<String> text, String id) {
        this.id = id;
        this.loc = loc;
        this.armorStands = new ArrayList<>();

        int level = 0;
        for (String line : text) {
            Location asLoc = new Location(loc.getWorld(), loc.getX(), loc.getY() - level*0.25, loc.getZ());
            ArmorStand armorStand = (ArmorStand) TheTerrierCityPlugin.lobby.spawnEntity(asLoc, EntityType.ARMOR_STAND);

            armorStand.setCustomName(ChatColor.translateAlternateColorCodes('§', line));

            armorStand.setGravity(false);
            armorStand.setVisible(false);
            armorStand.setCustomNameVisible(true);

            armorStands.add(armorStand);

            level++;
        }
    }

    public void updateText(ArrayList<String> text) {
        remove();

        for (int i = 0; i < text.size(); i++) {
            Location asLoc = new Location(loc.getWorld(), loc.getX(), loc.getY() - i*0.25, loc.getZ());
            ArmorStand armorStand = (ArmorStand) TheTerrierCityPlugin.lobby.spawnEntity(asLoc, EntityType.ARMOR_STAND);

            armorStand.setCustomName(ChatColor.translateAlternateColorCodes('§', text.get(i)));

            armorStand.setGravity(false);
            armorStand.setVisible(false);
            armorStand.setCustomNameVisible(true);

            armorStands.add(armorStand);
        }
    }

    public void remove() {
        for (ArmorStand armorStand : armorStands) {
            armorStand.remove();
        }
    }

    public String getId() {
        return this.id;
    }
}
