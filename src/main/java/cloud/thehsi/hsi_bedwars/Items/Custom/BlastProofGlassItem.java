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
    public BlastProofGlassItem(PluginItems.ItemProvider provider) {
        super(provider, Material.RED_STAINED_GLASS, "blast_proof_glass", "Blast-Proof Glass", true, true, "Immune to explosions!", meta -> {
            meta.setRarity(ItemRarity.COMMON);
            return meta;
        });
    }

    @Override
    public void onUse(PlayerInteractEvent event) {}
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