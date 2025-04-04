package cloud.thehsi.hsi_bedwars.BedwarsElements.Teams;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Team {
    String color;
    ChatColor chatColor;
    Spawnpoint spawnpoint;
    Bed bed;
    List<Player> players;

    public Team(String color, Location bed1, Location bed2, Location spawnpoint, Plugin plugin) {
        players = new ArrayList<>();
        chatColor = switch (color) {
            case "red" -> ChatColor.RED;
            case "orange" -> ChatColor.GOLD;
            case "yellow" -> ChatColor.YELLOW;
            case "green" -> ChatColor.GREEN;
            case "blue" -> ChatColor.AQUA;
            case "purple" -> ChatColor.DARK_PURPLE;
            case "pink" -> ChatColor.LIGHT_PURPLE;
            case "white" -> ChatColor.WHITE;
            default -> null;
        };
        if (chatColor == null) {
            Bukkit.broadcastMessage(ChatColor.RED + "Unknown Team Color: '" + color + "'");
            return;
        }
        this.color = color;
        this.spawnpoint = new Spawnpoint(spawnpoint, this, plugin);
        this.bed = new Bed(bed1, bed2, this);
    }

    public String renderIcon() {
        return getChatColor() + (bed.isDestroyed() ? "░" : "█");
    }

    public Spawnpoint getSpawnpoint() {
        return spawnpoint;
    }

    public Bed getBed() {
        return bed;
    }

    public String getColor() {
        return color;
    }

    public void addPlayer(Player player) {
        players.add(player);
        player.setDisplayName(getChatColor() + player.getName() + ChatColor.RESET);
        player.setPlayerListName(getChatColor() + player.getName() + ChatColor.RESET);
    }

    public void tick() {
        players.removeIf(player->!player.isOnline());
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Team team) {
            return Objects.equals(team.color, color);
        }
        return false;
    }
}
