package cloud.thehsi.hsi_bedwars.Listeners;

import cloud.thehsi.hsi_bedwars.BuildTracker;
import cloud.thehsi.hsi_bedwars.Items.PluginItems;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.function.Function;

public class PluginListeners {
    Plugin plugin;
    PluginManager manager;
    BuildTracker tracker;
    PluginItems pluginItems;

    private void register() {
        registerListener(plugin -> new BuildTrackerListener(plugin, tracker));
        registerListener(plugin -> new FireballListener(plugin, tracker));
        registerListener(plugin -> new ExplosionListener(plugin, tracker));
        registerListener(TntPlaceListener::new);
        registerListener(SnowballHitPlayerListener::new);
        registerListener(AntiCropTramplingListener::new);
        registerListener(plugin -> new AntiDirtPathDestructionListener(plugin, tracker));
        registerListener(CustomMessageListener::new);
        registerListener(PlayerMovementListener::new);
        registerListener(MovementSpeedFixListener::new);
        registerListener(plugin -> new PlayerItemUseListener(plugin, pluginItems));
        registerListener(AntiBedClickListener::new);
    }

    private void registerListener(Function<Plugin, ? extends AdvancedListener> eventListener) {
        AdvancedListener listener = eventListener.apply(plugin);
        manager.registerEvents(listener, plugin);
    }

    public PluginListeners(Plugin plugin, BuildTracker tracker, PluginItems pluginItems) {
        this.manager = Bukkit.getPluginManager();
        this.plugin = plugin;
        this.tracker = tracker;
        this.pluginItems = pluginItems;
        register();
    }
}
