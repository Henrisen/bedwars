package cloud.thehsi.hsi_bedwars.Items.Custom;

import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.Team;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.TeamController;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Utils;
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
        super(provider, Material.RED_BED, "bed", "Replacement Bed", true, true, "An Extra Bed\nAutomatic", meta -> {
            meta.setRarity(ItemRarity.EPIC);
            return meta;
        });
    }

    private final Material[] beds = {Material.RED_BED, Material.ORANGE_BED, Material.YELLOW_BED, Material.LIME_BED, Material.LIGHT_BLUE_BED, Material.PURPLE_BED, Material.PINK_BED, Material.WHITE_BED};

    @Override
    public void onUse(PlayerInteractEvent event) {}
    /**
     * Updates the replacement bed item in the player's inventory based on their team and bed status.
     *
     * If the player is not on a team, the item is removed. If the team's bed is destroyed, the bed is replaced and the item is consumed. Otherwise, the item's appearance and name are updated to match the team's color.
     *
     * @return the updated ItemStack reflecting the current team and bed state
     */
    @Override
    public ItemStack inventoryTick(Player player, ItemStack stack) {
        Team team = TeamController.getPlayerTeam(player);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        if (team == null) {
            stack.setAmount(0);
        }else if (team.getBed().isDestroyed()) {
            team.getBed().place();
            Utils.broadcastBedReplacement(team, player);
            stack.setAmount(0);
        } else {
            stack.setType(team.getBed().getBedType());
            String color = team.getColor().toLowerCase();
            color = color.substring(0,1).toUpperCase() + color.substring(1);
            meta.setItemName(color + " Replacement Bed");
            stack.setItemMeta(meta);
        }
        return stack;
    }

    /**
     * Generates a preview ItemStack for the shop UI, displaying a replacement bed item customized to the viewer's team color.
     *
     * If the viewer is not on a team, the preview defaults to a red replacement bed.
     *
     * @param viewer the player viewing the shop
     * @return an ItemStack representing the replacement bed for the viewer's team
     */
    @Override
    public ItemStack getShopPreviewStack(Player viewer) {
        ItemStack stack = getDefaultStack();
        Team team = TeamController.getPlayerTeam(viewer);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        if (team == null) {
            stack.setType(Material.RED_BED);
            meta = stack.getItemMeta();
            meta.setItemName("Red Replacement Bed");
        } else {
            stack.setType(team.getBed().getBedType());
            meta = stack.getItemMeta();
            String color = team.getColor().toLowerCase();
            color = color.substring(0,1).toUpperCase() + color.substring(1);
            meta.setItemName(color + " Replacement Bed");
        }
        stack.setItemMeta(meta);
        return stack;
    }

    /**
     * Prevents placement of the replacement bed item and returns it to the player's inventory.
     *
     * Cancels the block placement event if the player attempts to place a replacement bed item,
     * and adds a default replacement bed item back to the player's inventory.
     *
     * @param event the block placement event triggered by the player
     */
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