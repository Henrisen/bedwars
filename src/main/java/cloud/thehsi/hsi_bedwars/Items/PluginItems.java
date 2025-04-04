package cloud.thehsi.hsi_bedwars.Items;

import cloud.thehsi.hsi_bedwars.Items.Custom.TotemOfSelfDetonationItem;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PluginItems {
    public List<BaseItem> items = new ArrayList<>();
    Plugin plugin;

    public PluginItems(Plugin plugin) {
        this.plugin = plugin;

        TOTEM_OF_SELF_DETONATION = registerItem(TotemOfSelfDetonationItem::new);
    }

    public BaseItem TOTEM_OF_SELF_DETONATION;


    private BaseItem registerItem(Function<Plugin, BaseItem> factory) {
        BaseItem item = factory.apply(plugin);
        Bukkit.getPluginManager().registerEvents(item, plugin);
        items.add(item);
        return item;
    }
    public List<BaseItem> getItems() {
        return items;
    }
}
