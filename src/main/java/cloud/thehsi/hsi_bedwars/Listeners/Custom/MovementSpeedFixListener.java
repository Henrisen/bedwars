package cloud.thehsi.hsi_bedwars.Listeners.Custom;

import cloud.thehsi.hsi_bedwars.Listeners.AdvancedListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class MovementSpeedFixListener extends AdvancedListener {
    private final double maxSpeed = 10.0;
    public MovementSpeedFixListener(Plugin plugin) {
        super(plugin);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    Vector vel = p.getVelocity();
                    if (vel.length() > maxSpeed) {
                        p.setVelocity(vel.normalize().multiply(maxSpeed));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L); // every tick
    }

    @EventHandler
    private void moveEvent(PlayerMoveEvent e)  {
        Vector vel = e.getPlayer().getVelocity();
        double maxSpeed = 10.0;
        if (vel.length() > maxSpeed) {
            e.getPlayer().setVelocity(vel.normalize().multiply(maxSpeed));
        }
    }
}
