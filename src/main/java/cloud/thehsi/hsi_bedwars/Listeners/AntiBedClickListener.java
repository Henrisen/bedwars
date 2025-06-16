package cloud.thehsi.hsi_bedwars.Listeners;

import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.Bed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class AntiBedClickListener extends AdvancedListener{
    public AntiBedClickListener(Plugin plugin) {
        super(plugin);
    }

    @EventHandler
    private void bedClick(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (!Arrays.stream(Bed.Beds).toList().contains(event.getClickedBlock().getType())) return;
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        event.setCancelled(true);
    }
}
