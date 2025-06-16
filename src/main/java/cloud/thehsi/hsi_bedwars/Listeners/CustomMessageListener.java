package cloud.thehsi.hsi_bedwars.Listeners;

import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.Team;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.TeamController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
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
        assert event.getEntity().getLastDamageCause() != null;
        EntityDamageEvent.DamageCause cause = event.getEntity().getLastDamageCause().getCause();
        //noinspection UnstableApiUsage
        String causer = event.getDamageSource().getCausingEntity()==null?"":" by "+event.getDamageSource().getCausingEntity().getCustomName()+ChatColor.GRAY;
        Bukkit.broadcastMessage(event.getEntity().getDisplayName() + ChatColor.GRAY + " " + switch (cause) {
            case BLOCK_EXPLOSION, ENTITY_EXPLOSION -> "was blown into pieces" + causer + "!";
            case FALL -> "noticed gravity exists!";
            case ENTITY_ATTACK, ENTITY_SWEEP_ATTACK -> "was slain" + causer + "!";
            case FIRE, FIRE_TICK, HOT_FLOOR -> "went up in flames!";
            case LAVA -> "took a bath in hot sauce!";
            case WORLD_BORDER -> "left this world!";
            case PROJECTILE -> "got shot" + causer + "!";
            case POISON -> "ate some bad food!";
            case VOID -> causer.isEmpty()?"fell into the void!":"got pushed into the void" + causer + "!";
            default -> "died!";
        });
        Team team = TeamController.getPlayerTeam(event.getEntity());
        if (team == null) return;
        team.getSpawnpoint().respawnPlayer(event.getEntity());
    }
}
