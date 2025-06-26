package cloud.thehsi.hsi_bedwars.Items.Custom;

import cloud.thehsi.hsi_bedwars.Items.BaseItem;
import cloud.thehsi.hsi_bedwars.Items.PluginItems;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class TNTItem extends BaseItem {
    public TNTItem(PluginItems.ItemProvider provider) {
        super(provider, Material.TNT, "tnt", "TNT", false, false, "Instantly ignites, appropriate to\nexplode things!", meta -> {
            meta.setRarity(ItemRarity.RARE);
            return meta;
        });
    }

    @Override
    public void onUse(PlayerInteractEvent event) {}

    @EventHandler
    private void TntDamageReduction(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)
            event.setDamage(10);

        if (event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!event.getEntity().isDead())
                        event.getEntity().setVelocity(event.getEntity().getVelocity().multiply(1.75));
                }
            }.runTaskLater(plugin(), 2);
        }
    }

    @EventHandler
    private void tntPlaced(BlockPlaceEvent event) {
        int fuseTick = 20 * 3; // 3 Seconds
        if (event.getBlockPlaced().getType() != Material.TNT) return;
        event.getBlockPlaced().setType(Material.AIR);
        TNTPrimed tnt = event.getPlayer().getWorld().spawn(event.getBlockPlaced().getLocation().add(.5,0,.5), TNTPrimed.class);
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
                    ChatColor color = ChatColor.GREEN;
                    if ((double) tnt.getFuseTicks() / fuseTick < .5) color = ChatColor.YELLOW;
                    if ((double) tnt.getFuseTicks() / fuseTick < .1) color = ChatColor.RED;
                    tnt.setCustomName(color + "" + seconds + ":" + String.format("%02d", ms));
                } else {
                    // Stop the task if the TNT is no longer alive
                    cancel();
                }
            }
        }.runTaskTimer(plugin(), 0L, 1L); // Run every tick
    }

    @Override
    public ItemStack inventoryTick(Player player, ItemStack stack) { return stack; }
}