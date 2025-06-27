package cloud.thehsi.hsi_bedwars.Items.Custom;

import cloud.thehsi.hsi_bedwars.Items.BaseItem;
import cloud.thehsi.hsi_bedwars.Items.PluginItems;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class SnowballItem extends BaseItem {
    public SnowballItem(PluginItems.ItemProvider provider) {
        super(provider, Material.SNOWBALL, "snowball", "Snowball", false, false, "Used to knock enemies far away,\nalso deals 1 HP damage.", meta -> {
            meta.setRarity(ItemRarity.COMMON);
            return meta;
        });
    }

    @Override
    public void onUse(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK)
            return;
        if (event.getPlayer().getCooldown(getType()) != 0) {
            event.setCancelled(true);
        }else {
            event.getPlayer().setCooldown(getType(), 20);
        }
    }

    @EventHandler
    private void snowballHitPlayer(ProjectileHitEvent event) {
        if (!(event.getHitEntity() instanceof Player player)) return;
        if (!(event.getEntity() instanceof Snowball)) return;

        player.damage(1, event.getEntity());
        Vector velocity = player.getVelocity().clone();
        velocity.add(event.getEntity().getVelocity().multiply(.5));
        player.setVelocity(new Vector(velocity.getX(), player.getVelocity().getY() + .4, velocity.getZ()));
    }

    @Override
    public ItemStack inventoryTick(Player player, ItemStack stack) { return stack; }
}