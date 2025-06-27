package cloud.thehsi.hsi_bedwars.Items.Custom;

import cloud.thehsi.hsi_bedwars.Items.BaseItem;
import cloud.thehsi.hsi_bedwars.Items.PluginItems;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;

public class KnockBackStickItem extends BaseItem {
    public KnockBackStickItem(PluginItems.ItemProvider provider) {
        super(provider, Material.STICK, "knockback_stick", "Knockback Stick", false, false, "Knock back Players with great power.", meta -> {
            meta.setRarity(ItemRarity.RARE);
            meta.addEnchant(Enchantment.KNOCKBACK, 2, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setMaxStackSize(1);
            return meta;
        });
    }

    @Override
    public void onUse(PlayerInteractEvent event) {}

    @Override
    public ItemStack inventoryTick(Player player, ItemStack stack) {return null;}
}