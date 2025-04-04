package cloud.thehsi.hsi_bedwars.Listeners;

import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.Team;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.TeamController;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

public class CustomMessageListener extends AdvancedListener {
    public CustomMessageListener(Plugin plugin) {
        super(plugin);
    }

    @EventHandler
    private void onChatMessage(AsyncPlayerChatEvent event) {
        Bukkit.broadcastMessage(event.getPlayer().getDisplayName() + ": " + event.getMessage());
        event.setCancelled(true);
    }

    @EventHandler
    private void onDeath(PlayerDeathEvent event) {
        event.getEntity().getInventory().clear();
        event.getEntity().spigot().respawn();
        Bukkit.broadcastMessage(event.getEntity().getDisplayName() + " died!");
        Team team = TeamController.getPlayerTeam(event.getEntity());
        if (team == null) return;
        team.getSpawnpoint().respawnPlayer(event.getEntity());
    }
}
