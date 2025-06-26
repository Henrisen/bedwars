package cloud.thehsi.hsi_bedwars.Items.Custom;

import cloud.thehsi.hsi_bedwars.Items.BaseItem;
import cloud.thehsi.hsi_bedwars.Items.PluginItems;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.concurrent.atomic.AtomicInteger;

public class StaffOfLightningItem extends BaseItem {
    public StaffOfLightningItem(PluginItems.ItemProvider provider) {
        super(provider, Material.BLAZE_ROD, "staff_of_lightning", "Staff of Lightning", false, false, "Freezes Everyone for 2 Seconds,\nShrinks and Slows everyone\nfor 7 Seconds", meta -> {meta.setRarity(ItemRarity.RARE);meta.setMaxStackSize(1);meta.setEnchantmentGlintOverride(true);return meta;});
    }

    @Override
    public void onUse(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR || event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack stack = event.getItem();
        assert stack != null;
        stack.setAmount(stack.getAmount()-1);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.getWorld().strikeLightningEffect(p.getLocation());
            AttributeInstance scale = p.getAttribute(Attribute.SCALE);
            AttributeInstance speed = p.getAttribute(Attribute.MOVEMENT_SPEED);
            assert scale != null;
            assert speed != null;
            scale.setBaseValue(.5);
            speed.setBaseValue(.05);
            Vector velocity = p.getVelocity();
            Location location = p.getLocation();

            AtomicInteger i = new AtomicInteger(40);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (i.getAndDecrement() == 0) cancel();
                    p.setVelocity(new Vector(0,0,0));
                    p.teleport(location);
                }
            }.runTaskTimer(plugin(), 0, 1);
            Bukkit.getScheduler().runTaskLater(plugin(), () -> {
                speed.setBaseValue(.05);
                p.teleport(location);
                p.setVelocity(velocity);
            }, 40);
            Bukkit.getScheduler().runTaskLater(plugin(), () -> {
                scale.setBaseValue(1);
                speed.setBaseValue(.1);
            }, 140);
        }
    }

    @Override
    public ItemStack inventoryTick(Player player, ItemStack stack) { return stack; }
}
