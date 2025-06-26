package cloud.thehsi.hsi_bedwars.Listeners.Custom;

import cloud.thehsi.hsi_bedwars.BuildTracker;
import cloud.thehsi.hsi_bedwars.Listeners.AdvancedListener;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.plugin.Plugin;

public class AntiDirtPathDestructionListener extends AdvancedListener {
    BuildTracker tracker;
    public AntiDirtPathDestructionListener(Plugin plugin, BuildTracker tracker) {super(plugin);this.tracker=tracker;}

    @EventHandler
    private void blockPhysics(BlockPhysicsEvent event)  {
        if (event.getBlock().getType() == Material.DIRT_PATH) {
            if (!event.getBlock().getRelative(BlockFace.UP).getType().isAir())
                tracker.registerChange(event.getBlock(), Material.DIRT_PATH.createBlockData(), true);
        }
    }
}
