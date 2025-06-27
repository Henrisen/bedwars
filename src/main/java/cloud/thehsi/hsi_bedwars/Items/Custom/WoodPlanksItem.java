package cloud.thehsi.hsi_bedwars.Items.Custom;

import cloud.thehsi.hsi_bedwars.Items.PluginItems;
import cloud.thehsi.hsi_bedwars.Items.SimpleItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemRarity;

public class WoodPlanksItem extends SimpleItem {
    public WoodPlanksItem(PluginItems.ItemProvider provider) {
        super(
                provider,
                Material.OAK_PLANKS,
                "wood_planks",
                "Oak Wood Planks",
                "Good block to defend your bed.\nStrong against pickaxes.",
                ItemRarity.COMMON
        );
    }
}