package cloud.thehsi.hsi_bedwars.BedwarsElements.Teams;

import cloud.thehsi.hsi_bedwars.BedwarsElements.JsonParser;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static cloud.thehsi.hsi_bedwars.Listeners.Custom.CustomMessageListener.respawnOnJoin;
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
            player.sendTitle(ChatColor.RED + "YOU DIED!", "" , 0 ,80 ,20);
            return;
        }

        for (int i = 5; i > 0; i--) {
            int finalI = i;
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (!player.isOnline()) {
                    respawnOnJoin.add(player);
                    return;
                }
                player.sendTitle(ChatColor.RED + (team.bed.isDestroyed()?"BED DESTROYED!":"YOU DIED!"), ChatColor.YELLOW  + "You will respawn in " +  ChatColor.RED + finalI + ChatColor.YELLOW + " seconds!" , 0 ,21 ,0);
                player.sendMessage(ChatColor.YELLOW  + "You will respawn in " +  ChatColor.RED + finalI + ChatColor.YELLOW + " seconds!");
            },(5 - i) * 20L);
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!player.isOnline()) {
                respawnOnJoin.add(player);
                return;
            }
            player.teleport(location);
            player.setGameMode(GameMode.SURVIVAL);
            player.sendTitle(ChatColor.GREEN + "RESPAWNED!", "" , 0 ,20 ,20);
            player.sendMessage(ChatColor.YELLOW + "You have respawned!");
        },100);
    }

    public Location getSpawnpointLocation() {
        return location;
    }
}
