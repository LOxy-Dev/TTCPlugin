package fr.loxydev.ttcplugin.team;

import fr.loxydev.ttcplugin.database.PlayerDataHandler;
import fr.loxydev.ttcplugin.menu.PlayerMenuUtility;
import org.bukkit.ChatColor;

import java.util.ArrayList;

public class Team {

    private final String name;
    private ChatColor color;

    private ArrayList<PlayerDataHandler> players = new ArrayList<>();
    private int points;

    public Team(String name) {
        this.name = name;
        calculatePoints();
    }

    public Team(String name, ChatColor color, ArrayList<PlayerDataHandler> players) {
        this.name = name;
        this.color = color;
        this.players = players;

        calculatePoints();
    }

    public void calculatePoints() {
        points = 0;
        for (PlayerDataHandler player : players) {
            player.update();
            points += player.getPointsAmount();
        }
    }

    public String getName() {
        return this.name;
    }

    public ChatColor getColor() {
        return this.color;
    }

    public int getPoints() {
        return points;
    }

    public void addPlayer(PlayerMenuUtility player) {
        if (players.contains(player)) return;

        players.add(player.getPlayerData());
    }

    public void setPlayers(ArrayList<PlayerDataHandler> players) {
        this.players = players;
    }

    public void removePlayer(PlayerMenuUtility player) {
        players.remove(player);
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }
}
