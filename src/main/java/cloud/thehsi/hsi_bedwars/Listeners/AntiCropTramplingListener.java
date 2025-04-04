package cloud.thehsi.hsi_bedwars.Listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public class AntiCropTramplingListener extends AdvancedListener{
    public AntiCropTramplingListener(Plugin plugin) {
        super(plugin);
    }

    @EventHandler
    private void trampleBlock(PlayerInteractEvent event)  {
        // Physical means jump on it
        if (event.getAction() == Action.PHYSICAL) {
            Block block = event.getClickedBlock();
            if (block == null) return;
            // If the block is farmland (soil)
            if (block.getType() == Material.FARMLAND) {
                // Deny event and set the block
                event.setUseInteractedBlock(org.bukkit.event.Event.Result.DENY);
                event.setCancelled(true);
                //block.setType(block.getType(), true);
            }
        }
    }
}
