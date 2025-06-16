package cloud.thehsi.hsi_bedwars.Items.Custom;

import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.Team;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.TeamController;
import cloud.thehsi.hsi_bedwars.Items.BaseItem;
import cloud.thehsi.hsi_bedwars.Items.PluginItems;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.Objects;

public class BedItem extends BaseItem {
    public BedItem(PluginItems.ItemProvider provider) {
        super(provider, Material.RED_BED, "bed", "Replacement Bed", true, true, meta -> {
            meta.setRarity(ItemRarity.EPIC);
            return meta;
        });
    }

    private final Material[] beds = {Material.RED_BED, Material.ORANGE_BED, Material.YELLOW_BED, Material.LIME_BED, Material.LIGHT_BLUE_BED, Material.PURPLE_BED, Material.PINK_BED, Material.WHITE_BED};

    @Override
    public void onUse(PlayerInteractEvent event) {}
    @Override
    public ItemStack inventoryTick(Player player, ItemStack stack) {
        Team team = TeamController.getPlayerTeam(player);
        if (team == null) {
            stack.setAmount(0);
        }else if (team.getBed().isDestroyed()) {
            team.getBed().place();
            stack.setAmount(0);
        } else {
            stack.setType(team.getBed().getBedType());
            ItemMeta meta = stack.getItemMeta();
            assert meta != null;
            String color = team.getColor().toLowerCase();
            color = color.substring(0,1).toUpperCase() + color.substring(1);
            meta.setItemName(color + " Replacement Bed");
            stack.setItemMeta(meta);
        }
        return stack;
    }

    @EventHandler
    private void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!isThisItem(player.getInventory().getItemInMainHand())) {
            if (!isThisItem(player.getInventory().getItemInOffHand())) return;
        }
        event.getPlayer().getInventory().addItem(getDefaultStack(event.getPlayer()));
        event.setCancelled(true);
    }

    @Override
    public boolean isThisItem(ItemStack stack) {
        if (stack == null) return false;
        if (!Arrays.stream(beds).toList().contains(stack.getType())) return false;
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        String id = meta.getPersistentDataContainer().get(
                new NamespacedKey(this.plugin(), "item_id"),
                PersistentDataType.STRING
        );
        return Objects.equals(id, this.getId());
    }
}