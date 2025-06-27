package cloud.thehsi.hsi_bedwars.Items;

import cloud.thehsi.hsi_bedwars.BedwarsElements.Utils;
import cloud.thehsi.hsi_bedwars.BuildTracker;
import cloud.thehsi.hsi_bedwars.Items.Custom.*;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PluginItems implements Listener {
    public List<BaseItem> items = new ArrayList<>();
    Plugin plugin;
    BuildTracker buildTracker;
    BukkitTask task;

    public BaseItem TOTEM_OF_SELF_DETONATION,
            MINE,
            STAFF_OF_LIGHTNING,
            BRIDGE_EGG,
            TOTEM_OF_KEEP_INVENTORY,
            JUMP_PAD,
            THE_CHANCE,
            WOOL,
            BED,
            FIREBALL,
            TNT,
            SNOWBALL,
            BLAST_PROOF_GLASS,
            END_STONE,
            WOOD_PLANKS,
            CHEST,
            POPUP_SHOP,
            KNOCKBACK_STICK;

    public PluginItems(Plugin plugin, BuildTracker buildTracker) {
        this.plugin = plugin;
        this.buildTracker = buildTracker;

        TOTEM_OF_SELF_DETONATION = registerItem(TotemOfSelfDetonationItem::new);
        MINE = registerItem(MineItem::new);
        STAFF_OF_LIGHTNING = registerItem(StaffOfLightningItem::new);
        BRIDGE_EGG = registerItem(BridgeEggItem::new);
        TOTEM_OF_KEEP_INVENTORY = registerItem(TotemOfKeepInventoryItem::new);
        JUMP_PAD = registerItem(JumpPadItem::new);
        THE_CHANCE = registerItem(TheChanceItem::new);
        WOOL = registerItem(WoolItem::new);
        BED = registerItem(BedItem::new);
        FIREBALL = registerItem(FireballItem::new);
        TNT = registerItem(TNTItem::new);
        SNOWBALL = registerItem(SnowballItem::new);
        BLAST_PROOF_GLASS = registerItem(BlastProofGlassItem::new);
        END_STONE = registerItem(EndStoneItem::new);
        WOOD_PLANKS = registerItem(WoodPlanksItem::new);
        CHEST = registerItem(ChestItem::new);
        POPUP_SHOP = registerItem(PopupShopItem::new);
        KNOCKBACK_STICK = registerItem(KnockBackStickItem::new);

        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> items.forEach(BaseItem::tick), 1, 1);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void remove() {
        task.cancel();
        for (BaseItem item : items) {
            item.remove();
            Utils.unregisterEvents(item);
        }
        items.clear();
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

    @EventHandler
    private void onUse(PlayerInteractEvent event) {
        BaseItem item = getItem(event.getItem());
        if (item != null) item.onUse(event);
    }
}
