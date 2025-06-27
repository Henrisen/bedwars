package cloud.thehsi.hsi_bedwars.Listeners.Custom;

import cloud.thehsi.hsi_bedwars.BuildTracker;
import cloud.thehsi.hsi_bedwars.Listeners.AdvancedListener;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class ExplosionListener extends AdvancedListener {
    List<Material> unbreakable = List.of(
            Material.RED_STAINED_GLASS,
            Material.ORANGE_STAINED_GLASS,
            Material.YELLOW_STAINED_GLASS,
            Material.LIME_STAINED_GLASS,
            Material.LIGHT_BLUE_STAINED_GLASS,
            Material.PURPLE_STAINED_GLASS,
            Material.PINK_STAINED_GLASS,
            Material.WHITE_STAINED_GLASS,
            Material.RED_BED,
            Material.ORANGE_BED,
            Material.YELLOW_BED,
            Material.LIME_BED,
            Material.LIGHT_BLUE_BED,
            Material.PURPLE_BED,
            Material.PINK_BED,
            Material.WHITE_BED,
            Material.OBSIDIAN
    );

    BuildTracker tracker;
    public ExplosionListener(Plugin plugin, BuildTracker tracker) {
        super(plugin);
        this.tracker = tracker;
    }

    @EventHandler
    private void explosionBlock(BlockExplodeEvent event) {
        event.blockList().removeIf(this::cantBlowUpBlock);
    }

    @EventHandler
    private void explosionEntity(EntityExplodeEvent event) {
        event.blockList().removeIf(this::cantBlowUpBlock);
    }
    private boolean cantBlowUpBlock(Block block) {return !canBlowUpBlock(block);}
    private boolean canBlowUpBlock(Block block) {
        if (unbreakable.contains(block.getType())) return false;
        return tracker.canBreak(block);
    }
}
