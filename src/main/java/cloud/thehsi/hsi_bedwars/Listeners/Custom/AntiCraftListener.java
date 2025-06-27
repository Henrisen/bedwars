package cloud.thehsi.hsi_bedwars.Listeners.Custom;

import cloud.thehsi.hsi_bedwars.Listeners.AdvancedListener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.plugin.Plugin;

public class AntiCraftListener extends AdvancedListener {
    public AntiCraftListener(Plugin plugin) {
        super(plugin);
        Bukkit.resetRecipes();
        Bukkit.clearRecipes();
    }

    @EventHandler
    private void onCraft(CraftItemEvent event) {
        event.setCancelled(true);
    }
}
