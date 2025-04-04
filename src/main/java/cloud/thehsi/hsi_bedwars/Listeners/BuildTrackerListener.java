package cloud.thehsi.hsi_bedwars.Listeners;

import cloud.thehsi.hsi_bedwars.BuildTracker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

public class BuildTrackerListener extends AdvancedListener {
    BuildTracker tracker;
    public BuildTrackerListener(Plugin plugin, BuildTracker tracker) {super(plugin);this.tracker=tracker;}

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        tracker.registerChange(event.getBlockPlaced(), event.getBlockReplacedState().getType(), false);
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(!tracker.canBreakWithEvent(event.getBlock(), event));
    }
}
