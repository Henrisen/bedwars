package cloud.thehsi.hsi_bedwars.Listeners;

import cloud.thehsi.hsi_bedwars.Items.BaseItem;
import cloud.thehsi.hsi_bedwars.Items.PluginItems;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public class PlayerItemUseListener extends AdvancedListener {
    PluginItems pluginItems;
    public PlayerItemUseListener(Plugin plugin, PluginItems pluginItems) {
        super(plugin);
        this.pluginItems = pluginItems;
    }

    @EventHandler
    private void onUse(PlayerInteractEvent event) {
        BaseItem item = pluginItems.getItem(event.getItem());
        if (item != null) item.onUse(event);
    }
}
