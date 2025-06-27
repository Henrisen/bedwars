package cloud.thehsi.hsi_bedwars.Items.Custom;

import cloud.thehsi.hsi_bedwars.Items.PluginItems;
import cloud.thehsi.hsi_bedwars.Items.SimpleItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemRarity;

public class ChestItem extends SimpleItem {
    public ChestItem(PluginItems.ItemProvider provider) {
        super(
                provider,
                Material.CHEST,
                "chest",
                "Chest",
                "Good for storing you Resources.",
                ItemRarity.COMMON
        );
    }
}