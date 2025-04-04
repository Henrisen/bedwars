package cloud.thehsi.hsi_bedwars.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class SnowballHitPlayerListener extends AdvancedListener{
    public SnowballHitPlayerListener(Plugin plugin) {
        super(plugin);
    }

    @EventHandler
    private void snowballHitPlayer(ProjectileHitEvent event) {
        if (!(event.getHitEntity() instanceof Player player)) return;
        if (!(event.getEntity() instanceof Snowball)) return;

        player.damage(1, event.getEntity());
        Vector velocity = player.getVelocity().clone();
        velocity.add(event.getEntity().getVelocity().multiply(.5));
        player.setVelocity(new Vector(velocity.getX(), player.getVelocity().getY() + .5, velocity.getZ()));
    }
}
