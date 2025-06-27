package cloud.thehsi.hsi_bedwars.Items.Custom;

import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.Team;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.TeamController;
import cloud.thehsi.hsi_bedwars.Items.BaseItem;
import cloud.thehsi.hsi_bedwars.Items.PluginItems;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.Objects;

public class BlastProofGlassItem extends BaseItem {
    private final Material[] glasses = {Material.RED_STAINED_GLASS, Material.ORANGE_STAINED_GLASS, Material.YELLOW_STAINED_GLASS, Material.LIME_STAINED_GLASS, Material.LIGHT_BLUE_STAINED_GLASS, Material.PURPLE_STAINED_GLASS, Material.PINK_STAINED_GLASS, Material.WHITE_STAINED_GLASS};
    /**
     * Constructs a Blast-Proof Glass item with default properties, including a red stained glass material,
     * unique internal ID, display name, common rarity, and lore indicating explosion immunity.
     */
    public BlastProofGlassItem(PluginItems.ItemProvider provider) {
        super(provider, Material.RED_STAINED_GLASS, "blast_proof_glass", "Blast-Proof Glass", true, true, "Immune to explosions!", meta -> {
            meta.setRarity(ItemRarity.COMMON);
            return meta;
        });
    }

    /**
     * Handles player interaction with the item, but this item has no special use behavior.
     *
     * This method intentionally does nothing when the item is used.
     */
    @Override
    public void onUse(PlayerInteractEvent event) {}
    /**
     * Updates the item's material to match the player's team color each inventory tick.
     *
     * If the player is not on a team, the item is set to red stained glass. Otherwise, it is set to the stained glass color associated with the player's team. The item's metadata is preserved during this update.
     *
     * @param player the player holding the item
     * @param stack the item stack to update
     * @return the updated item stack with the appropriate glass color
     */
    @Override
    public ItemStack inventoryTick(Player player, ItemStack stack) {
        Team team = TeamController.getPlayerTeam(player);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        if (team == null) {
            stack.setType(Material.RED_STAINED_GLASS);
        }else {
            stack.setType(team.getGlass());
        }
        stack.setItemMeta(meta);
        return stack;
    }

    /**
     * Determines whether the given ItemStack represents this Blast-Proof Glass item.
     *
     * The check verifies that the item is one of the allowed stained glass types and that its persistent data container holds the correct item ID.
     *
     * @param stack the ItemStack to check
     * @return true if the stack matches this custom item; false otherwise
     */
    @Override
    public boolean isThisItem(ItemStack stack) {
        if (stack == null) return false;
        if (!Arrays.stream(glasses).toList().contains(stack.getType())) return false;
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        String id = meta.getPersistentDataContainer().get(
                new NamespacedKey(this.plugin(), "item_id"),
                PersistentDataType.STRING
        );
        return Objects.equals(id, this.getId());
    }
}