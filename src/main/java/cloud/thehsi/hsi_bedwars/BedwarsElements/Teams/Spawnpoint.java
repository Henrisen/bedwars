package cloud.thehsi.hsi_bedwars.BedwarsElements.Teams;

import cloud.thehsi.hsi_bedwars.BedwarsElements.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static java.lang.Math.atan2;
import static java.lang.Math.toDegrees;


public class Spawnpoint {
    Location location;
    Team team;
    Plugin plugin;
    public Spawnpoint(Location location, Team team, Plugin plugin) {
        this.location = location;
        this.team = team;
        this.plugin = plugin;
    }

    private void applyRotateToCenter() {
        Location center = JsonParser.getCenter();
        double dx = center.getX() - location.getX();
        double dz = center.getZ() - location.getZ();

        float yaw = (float) toDegrees(atan2(-dx, dz));

        yaw = Math.round(yaw / 90f) * 90f;

        location.setYaw(yaw);
        location.setPitch(0);
    }

    private Location rotateToSpawnpoint(Location l) {
        double dx = location.getX() - l.getX();
        double dz = location.getZ() - l.getZ();

        float yaw = (float) toDegrees(atan2(-dx, dz));

        yaw = Math.round(yaw / 90f) * 90f;

        l.setYaw(yaw);
        l.setPitch(0);
        return l;
    }

    public void update(Location location) {
        this.location = location;
    }

    public void respawnPlayer(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        Location center = JsonParser.getCenter().clone();
        player.teleport(rotateToSpawnpoint(center));
        applyRotateToCenter();

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
