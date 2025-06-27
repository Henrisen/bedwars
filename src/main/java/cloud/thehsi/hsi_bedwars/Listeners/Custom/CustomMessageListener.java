package cloud.thehsi.hsi_bedwars.Listeners.Custom;

import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.Team;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.TeamController;
import cloud.thehsi.hsi_bedwars.BuildTracker;
import cloud.thehsi.hsi_bedwars.Commands.GameResetCommand;
import cloud.thehsi.hsi_bedwars.Listeners.AdvancedListener;
import cloud.thehsi.hsi_bedwars.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class CustomMessageListener extends AdvancedListener {
    BuildTracker tracker;
    public CustomMessageListener(Plugin plugin, BuildTracker tracker) {
        super(plugin);
        this.tracker = tracker;
    }
    public static List<Player> respawnOnJoin = new ArrayList<>();

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
        Player player = null;
        //noinspection UnstableApiUsage
        if (event.getDamageSource().getCausingEntity() instanceof Player p)
            player = p;

        //noinspection UnstableApiUsage
        String causer = event.getDamageSource().getCausingEntity()==null?"":" by "+event.getDamageSource().getCausingEntity().getCustomName()+ChatColor.GRAY;
        Bukkit.broadcastMessage(event.getEntity().getDisplayName() + ChatColor.GRAY + " " + switch (cause) {
            case BLOCK_EXPLOSION, ENTITY_EXPLOSION -> "was blown into pieces" + causer + "!";
            case FALL -> "noticed gravity exists!";
            case ENTITY_ATTACK, ENTITY_SWEEP_ATTACK -> event.getEntity()==player?("committed suicide!"):("was slain" + causer + "!");
            case FIRE, FIRE_TICK, HOT_FLOOR -> "went up in flames!";
            case LAVA -> "took a bath in hot sauce!";
            case WORLD_BORDER -> "left this world!";
            case PROJECTILE -> "got shot" + causer + "!";
            case POISON -> "ate some bad food!";
            case VOID -> causer.isBlank()?"fell into the void!":"got pushed into the void" + causer + "!";
            default -> "died!";
        });
        Team team = TeamController.getPlayerTeam(event.getEntity());
        if (team == null) return;
        team.getSpawnpoint().respawnPlayer(event.getEntity());
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "▬".repeat(71));
        event.getPlayer().sendMessage(ChatColor.WHITE.toString() + ChatColor.BOLD + "                           Bed Wars");
        event.getPlayer().sendMessage("");
        event.getPlayer().sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "    Protect your bed and destroy the enemy beds.");
        event.getPlayer().sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "     Upgrade yourself and your team by collecting");
        event.getPlayer().sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "   Iron, Gold, Emerald and Diamond from generators");
        event.getPlayer().sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "              to access powerful upgrades.");
        event.getPlayer().sendMessage("");
        event.getPlayer().sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "▬".repeat(71));

        if (!Main.initialized) {
            GameResetCommand.reset(plugin, tracker);
            Main.initialized = true;
        }

        Team team = TeamController.getPlayerTeam(event.getPlayer());
        if (team == null) {
            event.getPlayer().setCustomName(event.getPlayer().getName());
            event.getPlayer().setDisplayName(event.getPlayer().getName());
            event.getPlayer().setPlayerListName(event.getPlayer().getName());
        }

        event.setJoinMessage(event.getPlayer().getCustomName() + ChatColor.GRAY + " connected.");

        if (respawnOnJoin.contains(event.getPlayer())) {
            if (team == null) {
                respawnOnJoin.remove(event.getPlayer());
                return;
            }
            team.getSpawnpoint().respawnPlayer(event.getPlayer());
        }
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        Team team = TeamController.getPlayerTeam(event.getPlayer());
        if (team != null) {
            if (team.getBed().isDestroyed()) team.players().remove(event.getPlayer());
            else respawnOnJoin.add(event.getPlayer());
        }
        event.setQuitMessage(ChatColor.GRAY + event.getPlayer().getCustomName() +  ChatColor.GRAY +" disconnected.");
    }
}
