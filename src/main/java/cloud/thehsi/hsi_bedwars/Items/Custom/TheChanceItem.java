package cloud.thehsi.hsi_bedwars.Items.Custom;

import cloud.thehsi.hsi_bedwars.Items.BaseItem;
import cloud.thehsi.hsi_bedwars.Items.PluginItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class TheChanceItem extends BaseItem implements Listener {
    Random random = new Random(System.currentTimeMillis());
    public TheChanceItem(PluginItems.ItemProvider provider) {
        super(provider, Material.BRUSH, "the_chance", "The Chance", false, meta -> {meta.setRarity(ItemRarity.EPIC);return meta;});
    }

    @Override
    public void onUse(PlayerInteractEvent event) {}
    @Override
    public void inventoryTick(Player player, ItemStack stack) {}

    @EventHandler
    public void onHitPlayer(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player attacker)) return;
        if (!(event.getEntity() instanceof Player attacked)) return;
        if (!isThisItem(attacker.getInventory().getItemInMainHand())) return;
        attacker.getInventory().getItemInMainHand().setAmount(0);
        if (random.nextBoolean()) {
            Bukkit.broadcastMessage(attacker.getDisplayName() + " defeated the Odds against " + attacked.getDisplayName());
            attacked.damage(1000, attacker);
        }else {
            Bukkit.broadcastMessage(attacker.getDisplayName() + " fell victim to the Odds");
            attacker.damage(1000, attacker);
        }
        event.setCancelled(true);
    }
}
