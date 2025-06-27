package cloud.thehsi.hsi_bedwars.BedwarsElements.Teams;

import cloud.thehsi.hsi_bedwars.BedwarsElements.Trader;
import cloud.thehsi.hsi_bedwars.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Team implements Listener {
    String color;
    ChatColor chatColor;
    Spawnpoint spawnpoint;
    Trader trader;
    Bed bed;
    List<Player> players;

    public Team(String color, Location bed1, Location bed2, Location spawnpoint, Location trader_location, Plugin plugin) {
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
        this.trader = new Trader(trader_location, this, Main.pluginItems, plugin);
        this.color = color;
        this.spawnpoint = new Spawnpoint(spawnpoint, this, plugin);
        this.bed = new Bed(bed1, bed2, this);
    }

    public Material getWool() {
        return switch (color) {
            case "red" -> Material.RED_WOOL;
            case "orange" -> Material.ORANGE_WOOL;
            case "yellow" -> Material.YELLOW_WOOL;
            case "green" -> Material.LIME_WOOL;
            case "blue" -> Material.LIGHT_BLUE_WOOL;
            case "purple" -> Material.PURPLE_WOOL;
            case "pink" -> Material.PINK_WOOL;
            case "white" -> Material.WHITE_WOOL;
            default -> null;
        };
    }

    public Material getGlass() {
        return switch (color) {
            case "red" -> Material.RED_STAINED_GLASS;
            case "orange" -> Material.ORANGE_STAINED_GLASS;
            case "yellow" -> Material.YELLOW_STAINED_GLASS;
            case "green" -> Material.LIME_STAINED_GLASS;
            case "blue" -> Material.LIGHT_BLUE_STAINED_GLASS;
            case "purple" -> Material.PURPLE_STAINED_GLASS;
            case "pink" -> Material.PINK_STAINED_GLASS;
            case "white" -> Material.WHITE_STAINED_GLASS;
            default -> null;
        };
    }

    public String renderIcon() {
        return getChatColor() + (bed.isDestroyed() ? "░" : "█");
    }

    public Spawnpoint getSpawnpoint() {
        return spawnpoint;
    }
    public Trader getTrader() {
        return trader;
    }

    public Bed getBed() {
        return bed;
    }

    public String getColor() {
        return color;
    }

    public void addPlayer(Player player) {
        Team pteam = TeamController.getPlayerTeam(player);
        if (pteam!=null) pteam.removePlayer(player);

        players.add(player);
        player.setDisplayName(getChatColor() + player.getName() + ChatColor.RESET);
        player.setPlayerListName(getChatColor() + player.getName() + ChatColor.RESET);
        player.setCustomName(getChatColor() + player.getName() + ChatColor.RESET);
    }

    public void removePlayer(Player player) {
        players.remove(player);
        player.setDisplayName(ChatColor.RESET + player.getName() + ChatColor.RESET);
        player.setPlayerListName(ChatColor.RESET + player.getName() + ChatColor.RESET);
        player.setCustomName(ChatColor.RESET + player.getName() + ChatColor.RESET);
    }

    public void tick() {
        players.removeIf(player->!player.isOnline()&&bed.isDestroyed());
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

    public List<Player> players() {
        return players;
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        if (!players.contains(p)) return;
        if (bed.isDestroyed()) players.remove(p);
    }
}
