package cloud.thehsi.hsi_bedwars.BedwarsElements.Teams;

import cloud.thehsi.hsi_bedwars.BedwarsElements.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class Spawnpoint {
    Location location;
    Team team;
    Plugin plugin;
    public Spawnpoint(Location location, Team team, Plugin plugin) {
        this.location = location;
        this.team = team;
        this.plugin = plugin;
    }

    public void update(Location location) {
        this.location = location;
    }

    public void respawnPlayer(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(JsonParser.getCenter());

        if (team.bed.isDestroyed()) {
            player.sendTitle(ChatColor.RED + "You Died!", "" , 0 ,80 ,20);
            return;
        }

        player.sendTitle(ChatColor.RED + "You Died!", "Respawning in 5..." , 0 ,21 ,0);
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.sendTitle(ChatColor.RED + "You Died!", "Respawning in 4..." , 0 ,21 ,0),20);
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.sendTitle(ChatColor.RED + "You Died!", "Respawning in 3..." , 0 ,21 ,0),40);
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.sendTitle(ChatColor.RED + "You Died!", "Respawning in 2..." , 0 ,21 ,0),60);
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.sendTitle(ChatColor.RED + "You Died!", "Respawning in 1..." , 0 ,21 ,0),80);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.teleport(location);
            player.setGameMode(GameMode.SURVIVAL);
            player.sendTitle(ChatColor.RED + "You Died!", "Respawning Now" , 0 ,20 ,20);
        },100);
    }

    public Location getSpawnpointLocation() {
        return location;
    }
}
