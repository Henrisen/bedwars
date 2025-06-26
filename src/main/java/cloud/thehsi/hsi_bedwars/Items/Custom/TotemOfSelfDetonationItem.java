package cloud.thehsi.hsi_bedwars.Items.Custom;

import cloud.thehsi.hsi_bedwars.Items.BaseItem;
import cloud.thehsi.hsi_bedwars.Items.PluginItems;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class TotemOfSelfDetonationItem extends BaseItem {
    public TotemOfSelfDetonationItem(PluginItems.ItemProvider provider) {
        super(provider, Material.TOTEM_OF_UNDYING, "totem_of_self_detonation", "Totem of Self Detonation", false, false, "Explode after dying\n(Single Use)", meta -> {meta.setRarity(ItemRarity.RARE);return meta;});
    }

    @Override
    public void onUse(PlayerInteractEvent event) {}
    @Override
    public ItemStack inventoryTick(Player player, ItemStack stack) { return stack; }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(EntityResurrectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (countInInventory(player) == 0) return;
        for (int i = Math.min(10, countInInventory(player)); i != 0; i--) {
            int fuseTick = 20 * 2; // 2 Seconds
            TNTPrimed tnt = player.getWorld().spawn(player.getLocation().add(.5,0,.5), TNTPrimed.class);
            tnt.setFuseTicks(fuseTick);
            tnt.setCustomNameVisible(true);
            new BukkitRunnable() {
                @Override
                public void run() {
                    // Check if the TNT is still alive
                    if (!tnt.isDead()) {
                        int fuse = tnt.getFuseTicks() - 2;
                        int seconds = fuse / 20;
                        int ms = fuse % 20 * 5;
                        tnt.setCustomName(seconds + ":" + String.format("%02d", ms));
                    } else {
                        cancel();
                    }
                }
            }.runTaskTimer(plugin(), 0L, 1L); // Run every tick
            player.getInventory().clear();
            event.setCancelled(true);
        }
    }
}
