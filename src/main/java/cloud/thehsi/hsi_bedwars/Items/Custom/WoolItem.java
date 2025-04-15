package cloud.thehsi.hsi_bedwars.Items.Custom;

import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.Team;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.TeamController;
import cloud.thehsi.hsi_bedwars.Items.BaseItem;
import cloud.thehsi.hsi_bedwars.Items.PluginItems;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WoolItem extends BaseItem {
    public WoolItem(PluginItems.ItemProvider provider) {
        super(provider, Material.BLACK_WOOL, "wool", "Wool", true, meta -> {
            meta.setRarity(ItemRarity.COMMON);
            return meta;
        });
    }

    @Override
    public void onUse(PlayerInteractEvent event) {}
    @Override
    public void inventoryTick(Player player, ItemStack stack) {
        Team team = TeamController.getPlayerTeam(player);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        if (team == null) {
            stack.setType(Material.RED_WOOL);
            meta.setItemName("Red Wool");
        }else {
            stack.setType(team.getWool());
            String color = team.getColor().toLowerCase();
            color = color.substring(0,1).toUpperCase() + color.substring(1);
            meta.setItemName(color + " Wool");
        }
        stack.setItemMeta(meta);
    }
}