package cloud.thehsi.hsi_bedwars.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

public class PlayerMovementListener extends AdvancedListener {
    public PlayerMovementListener(Plugin plugin) {
        super(plugin);
    }

    @EventHandler
    private void onMove(PlayerMoveEvent event) {
        if (event.getFrom().getY() < 0) event.getPlayer().damage(1000);
    }
}
