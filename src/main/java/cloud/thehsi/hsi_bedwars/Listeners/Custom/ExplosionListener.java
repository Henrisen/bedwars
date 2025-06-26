package cloud.thehsi.hsi_bedwars.Listeners.Custom;

import cloud.thehsi.hsi_bedwars.BuildTracker;
import cloud.thehsi.hsi_bedwars.Listeners.AdvancedListener;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class ExplosionListener extends AdvancedListener {
    List<Material> unbreakable = new ArrayList<>();
    BuildTracker tracker;
    public ExplosionListener(Plugin plugin, BuildTracker tracker) {
        super(plugin);
        this.tracker = tracker;
        unbreakable.add(Material.RED_STAINED_GLASS);
        unbreakable.add(Material.ORANGE_STAINED_GLASS);
        unbreakable.add(Material.YELLOW_STAINED_GLASS);
        unbreakable.add(Material.LIME_STAINED_GLASS);
        unbreakable.add(Material.LIGHT_BLUE_STAINED_GLASS);
        unbreakable.add(Material.PURPLE_STAINED_GLASS);
        unbreakable.add(Material.PINK_STAINED_GLASS);
        unbreakable.add(Material.WHITE_STAINED_GLASS);
        unbreakable.add(Material.RED_BED);
        unbreakable.add(Material.ORANGE_BED);
        unbreakable.add(Material.YELLOW_BED);
        unbreakable.add(Material.LIME_BED);
        unbreakable.add(Material.LIGHT_BLUE_BED);
        unbreakable.add(Material.PURPLE_BED);
        unbreakable.add(Material.PINK_BED);
        unbreakable.add(Material.WHITE_BED);
        unbreakable.add(Material.OBSIDIAN);
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
