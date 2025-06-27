package cloud.thehsi.hsi_bedwars.Items.Custom;

import cloud.thehsi.hsi_bedwars.Items.PluginItems;
import cloud.thehsi.hsi_bedwars.Items.SimpleItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemRarity;

public class EndStoneItem extends SimpleItem {
    public EndStoneItem(PluginItems.ItemProvider provider) {
        super(
                provider,
                Material.END_STONE,
                "end_stone",
                "End Stone",
                "Solid block to defend your bed.",
                ItemRarity.COMMON
        );
    }
}