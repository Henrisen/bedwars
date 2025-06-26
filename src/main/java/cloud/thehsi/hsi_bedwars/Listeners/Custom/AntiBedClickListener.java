package cloud.thehsi.hsi_bedwars.Listeners.Custom;

import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.Bed;
import cloud.thehsi.hsi_bedwars.Listeners.AdvancedListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class AntiBedClickListener extends AdvancedListener {
    public AntiBedClickListener(Plugin plugin) {
        super(plugin);
    }

    @EventHandler
    private void bedClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        if (!Arrays.stream(Bed.Beds).toList().contains(event.getClickedBlock().getType())) return;
        event.setCancelled(true);
    }
}
