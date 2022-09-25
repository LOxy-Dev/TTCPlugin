package fr.loxydev.ttcplugin.scoreboard;

import fr.loxydev.ttcplugin.database.PlayerDataHandler;
import fr.loxydev.ttcplugin.menu.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Objects;

public class PlayerScoreboard {

    private final Scoreboard board;
    private final Objective obj;
    private String[] lastScores;

    private final Player player;
    private final PlayerDataHandler playerData;

    public PlayerScoreboard(PlayerMenuUtility player) {
        this.player = player.getPlayer();
        playerData = player.getPlayerData();

        board = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
        obj = board.registerNewObjective("TTCScoreboard-1", "dummy", ChatColor.DARK_GRAY + ">> " + ChatColor.YELLOW + ChatColor.BOLD + "Cité du Terrier" + ChatColor.RESET + ChatColor.DARK_GRAY + " <<");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.getPlayer().setScoreboard(board);

        lastScores = new String[]{" > " + ChatColor.AQUA + playerData.getPointsAmount() + " points", " > " + ChatColor.AQUA + playerData.getTeam().getPoints() + " points "};
        // Board
        obj.getScore(ChatColor.GREEN + "" + ChatColor.STRIKETHROUGH + "                                 ").setScore(-1);
        obj.getScore(" ").setScore(-2);

        obj.getScore(ChatColor.BOLD + playerData.getPlayerName()).setScore(-3);
        obj.getScore(lastScores[0]).setScore(-4);

        obj.getScore("  ").setScore(-5);

        obj.getScore(playerData.getTeam().getColor() + playerData.getTeam().getName()).setScore(-6);
        obj.getScore(lastScores[1]).setScore(-7);

        obj.getScore("   ").setScore(-8);
        obj.getScore(ChatColor.GREEN + "" + ChatColor.STRIKETHROUGH + "                                 " + ChatColor.RESET).setScore(-9);
    }

    public void update() {
        board.resetScores(lastScores[0]);
        board.resetScores(lastScores[1]);

        lastScores = new String[]{" > " + ChatColor.AQUA + playerData.getPointsAmount() + " points", " > " + ChatColor.AQUA + playerData.getTeam().getPoints() + " points "};
        obj.getScore(lastScores[0]).setScore(-4);
        obj.getScore(lastScores[1]).setScore(-7);
    }
}
