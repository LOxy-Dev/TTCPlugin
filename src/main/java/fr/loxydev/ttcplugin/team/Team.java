package fr.loxydev.ttcplugin.team;

import fr.loxydev.ttcplugin.database.PlayerDataHandler;
import fr.loxydev.ttcplugin.utils.PlayerUtility;
import org.bukkit.ChatColor;

import java.util.ArrayList;

public class Team {

    private final int id;
    private final String name;
    private ArrayList<PlayerDataHandler> players;
    private ChatColor color;

    private int points;

    public Team(int id, String name, ChatColor color, ArrayList<PlayerDataHandler> players) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.players = players;

        calculatePoints();
    }

    public void calculatePoints() {
        int score = 0;

        for (PlayerDataHandler p : players) {
            score += p.getPointsAmount();
        }

        this.points = score;
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

    public void addPlayer(PlayerUtility player) {
        if (players.contains(player)) return;

        players.add(player.getPlayerData());
    }

    public void setPlayers(ArrayList<PlayerDataHandler> players) {
        this.players = players;
    }

    public void removePlayer(PlayerUtility player) {
        players.remove(player.getPlayerData());
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }
}
