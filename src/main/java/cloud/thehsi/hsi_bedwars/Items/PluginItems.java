package cloud.thehsi.hsi_bedwars.Items;

import cloud.thehsi.hsi_bedwars.BuildTracker;
import cloud.thehsi.hsi_bedwars.Items.Custom.*;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PluginItems {
    public List<BaseItem> items = new ArrayList<>();
    Plugin plugin;
    BuildTracker buildTracker;

    @SuppressWarnings("unused")
    public PluginItems(Plugin plugin, BuildTracker buildTracker) {
        this.plugin = plugin;
        this.buildTracker = buildTracker;

        BaseItem TOTEM_OF_SELF_DETONATION = registerItem(TotemOfSelfDetonationItem::new);
        BaseItem MINE = registerItem(MineItem::new);
        BaseItem STAFF_OF_LIGHTNING = registerItem(StaffOfLightningItem::new);
        BaseItem BRIDGE_EGG = registerItem(BridgeEggItem::new);
        BaseItem TOTEM_OF_KEEP_INVENTORY = registerItem(TotemOfKeepInventoryItem::new);
        BaseItem JUMP_PAD = registerItem(JumpPadItem::new);
        BaseItem THE_CHANCE = registerItem(TheChanceItem::new);
        BaseItem WOOL = registerItem(WoolItem::new);
        BaseItem BED = registerItem(BedItem::new);

        Bukkit.getScheduler().runTaskTimer(plugin, () -> items.forEach(BaseItem::tick), 0, 1);
    }

    private BaseItem registerItem(Function<ItemProvider, BaseItem> factory) {
        ItemProvider provider = new ItemProvider(plugin, buildTracker);
        BaseItem item = factory.apply(provider);
        items.add(item);
        return item;
    }
    public List<BaseItem> getItems() {
        return items;
    }

    public BaseItem getItem(ItemStack stack) {
        for (BaseItem item : items) {
            if (item.isThisItem(stack)) return item;
        }
        return null;
    }

    public static class ItemProvider {
        Plugin plugin;
        BuildTracker buildTracker;

        public ItemProvider(Plugin plugin, BuildTracker buildTracker) {
            this.plugin = plugin;
            this.buildTracker = buildTracker;
        }

        public Plugin plugin() {
            return plugin;
        }

        public BuildTracker buildTracker() {
            return buildTracker;
        }
    }
}
