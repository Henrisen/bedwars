package cloud.thehsi.hsi_bedwars.Items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;

public class SimpleItem extends BaseItem {
    public SimpleItem(PluginItems.ItemProvider provider, Material type, String id, String name, String description, ItemRarity rarity) {
        super(provider, type, id, name, false, false, description, meta -> {
            meta.setRarity(rarity);
            return meta;
        });
    }

    @Override
    public void onUse(PlayerInteractEvent event) {}

    @Override
    public ItemStack inventoryTick(Player player, ItemStack stack) { return stack; }
}