package cloud.thehsi.hsi_bedwars.Items.Custom;

import cloud.thehsi.hsi_bedwars.Items.BaseItem;
import cloud.thehsi.hsi_bedwars.Items.PluginItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;

public class TotemOfKeepInventoryItem extends BaseItem {
    public TotemOfKeepInventoryItem(PluginItems.ItemProvider provider) {
        super(provider, Material.TOTEM_OF_UNDYING, "totem_of_keep_inventory", "Totem of Keep Inventory", false, false, "Allows you to keep your inventory\non Death (Single Use)", meta -> {meta.setRarity(ItemRarity.RARE);return meta;});
    }

    @Override
    public void onUse(PlayerInteractEvent event) {}
    @Override
    public ItemStack inventoryTick(Player player, ItemStack stack) { return stack; }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(EntityResurrectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (countInInventory(player) == 0) return;
        getInInventory(player).setAmount(0);
        Inventory backup = Bukkit.createInventory(player, 36);
        ItemStack[] armor = {
                player.getInventory().getItem(EquipmentSlot.HEAD),
                player.getInventory().getItem(EquipmentSlot.CHEST),
                player.getInventory().getItem(EquipmentSlot.LEGS),
                player.getInventory().getItem(EquipmentSlot.FEET),
                player.getInventory().getItem(EquipmentSlot.OFF_HAND)
        };
        int slot = player.getInventory().getHeldItemSlot();
        for (int i = 0; i < backup.getSize(); i++) {
            backup.setItem(i, player.getInventory().getItem(i));
        }

        player.getInventory().clear();
        event.setCancelled(true);

        Bukkit.getScheduler().runTaskLater(plugin(), () -> {
            for (int i = 0; i < backup.getSize(); i++) {
                player.getInventory().setItem(i, backup.getItem(i));
            }
            player.getInventory().setItem(EquipmentSlot.HEAD, armor[0]);
            player.getInventory().setItem(EquipmentSlot.CHEST, armor[1]);
            player.getInventory().setItem(EquipmentSlot.LEGS, armor[2]);
            player.getInventory().setItem(EquipmentSlot.FEET, armor[3]);
            player.getInventory().setItem(EquipmentSlot.OFF_HAND, armor[4]);
            player.getInventory().setHeldItemSlot(slot);
        }, 20);
    }
}
