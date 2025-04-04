package cloud.thehsi.hsi_bedwars.Listeners;

import org.bukkit.Material;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TntPlaceListener extends AdvancedListener {
    public TntPlaceListener(Plugin plugin) {
        super(plugin);
    }

    @EventHandler
    private void tntPlaced(BlockPlaceEvent event) {
        int fuseTick = 20 * 3; // 3 Seconds
        if (event.getBlockPlaced().getType() != Material.TNT) return;
        event.getBlockPlaced().setType(Material.AIR);
        TNTPrimed tnt = event.getPlayer().getWorld().spawn(event.getBlockPlaced().getLocation().add(.5,0,.5), TNTPrimed.class);
        tnt.setFuseTicks(fuseTick);
        tnt.setCustomNameVisible(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                // Check if the fireball is still alive
                if (!tnt.isDead()) {
                    int fuse = tnt.getFuseTicks() - 2;
                    int seconds = fuse / 20;
                    int ms = fuse % 20 * 5;
                    tnt.setCustomName(seconds + ":" + String.format("%02d", ms));
                } else {
                    // Stop the task if the fireball is no longer alive
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L); // Run every tick
    }
}
