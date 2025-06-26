package cloud.thehsi.hsi_bedwars.Listeners.Custom;

import cloud.thehsi.hsi_bedwars.BuildTracker;
import cloud.thehsi.hsi_bedwars.Listeners.AdvancedListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

public class BuildTrackerListener extends AdvancedListener {
    BuildTracker tracker;
    public BuildTrackerListener(Plugin plugin, BuildTracker tracker) {super(plugin);this.tracker=tracker;}

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        tracker.registerChange(event.getBlockPlaced(), event.getBlockReplacedState().getBlockData(), false);
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(!tracker.canBreakWithEvent(event.getBlock(), event));
    }

    @EventHandler
    private void onBlockUpdate(BlockPhysicsEvent event) {
        event.setCancelled(tracker.canBreak(event.getBlock()));
    }
}
