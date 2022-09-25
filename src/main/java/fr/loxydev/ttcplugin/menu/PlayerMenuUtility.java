package fr.loxydev.ttcplugin.menu;

import fr.loxydev.ttcplugin.database.PlayerDataHandler;
import fr.loxydev.ttcplugin.scoreboard.PlayerScoreboard;
import fr.loxydev.ttcplugin.team.Team;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerMenuUtility {

    private Menu lastMenu;

    private final Player p;
    private final PlayerDataHandler playerData;
    private final PlayerScoreboard pBoard;

    private final Team team;

    private Player playerToAdd;

    private UUID uuidToRemove;
    private UUID uuidToManage;

    public PlayerMenuUtility(Player p) {
        this.p = p;
        this.playerData = new PlayerDataHandler(p.getUniqueId());

        if (playerData.isNull())
            PlayerDataHandler.createPlayerData(p);

        this.team = playerData.getTeam();
        pBoard = new PlayerScoreboard(this);
    }

    public ItemStack makeItem(Material material, String name) {
        ItemStack is = new ItemStack(material);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        is.setItemMeta(im);

        return is;
    }

    public ItemStack makeItem(Material material, String name, ArrayList<String> lore) {
        ItemStack is = new ItemStack(material);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        im.setLore(lore);
        is.setItemMeta(im);

        return is;
    }

    public ItemStack makeItem(Material material, String name, int amount, ArrayList<String> lore) {
        ItemStack is = new ItemStack(material, amount);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        im.setLore(lore);
        is.setItemMeta(im);

        return is;
    }

    public ItemStack makeItem(Material material, ArrayList<String> lore) {
        ItemStack is = new ItemStack(material);
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);

        return is;
    }

    public Player getPlayer() {
        return p;
    }

    public PlayerDataHandler getPlayerData() {
        return playerData;
    }

    public Team getTeam() {
        return team;
    }

    public Menu getLastMenu() {
        return lastMenu;
    }

    public void setLastMenu(Menu lastMenu) {
        this.lastMenu = lastMenu;
    }

    public Player getPlayerToAdd() {
        return playerToAdd;
    }

    public void setPlayerToAdd(Player playerToAdd) {
        this.playerToAdd = playerToAdd;
    }

    public UUID getUuidToRemove() {
        return uuidToRemove;
    }

    public void setUuidToRemove(UUID uuidToRemove) {
        this.uuidToRemove = uuidToRemove;
    }

    public UUID getUuidToManage() {
        return uuidToManage;
    }

    public PlayerScoreboard getScoreboard() {
        return pBoard;
    }

    public void setUuidToManage(UUID uuidToManage) {
        this.uuidToManage = uuidToManage;
    }

    public int itemInInv(Material item) {
        int amount = 0;
        for (ItemStack stack : p.getInventory().getContents())
            if (stack != null) if (stack.getType() == item) amount += stack.getAmount();

        return amount;
    }

    public boolean hasItem(Material item) {
        for (ItemStack stack : p.getInventory().getContents())
            if (stack != null) if (stack.getType() == item) return true;

        return false;
    }
}
