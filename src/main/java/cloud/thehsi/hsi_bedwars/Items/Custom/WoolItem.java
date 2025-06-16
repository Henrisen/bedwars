package cloud.thehsi.hsi_bedwars.Items.Custom;

import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.Team;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.TeamController;
import cloud.thehsi.hsi_bedwars.Items.BaseItem;
import cloud.thehsi.hsi_bedwars.Items.PluginItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WoolItem extends BaseItem {
    static class Position {
        int x, y, z;
        World world;

        public Position(int x, int y, int z, World world) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.world = world;
        }

        public String id() {
            return this.x + ";" + this.y + ";" + this.z + ";" + this.world.toString();
        }
    }
    private final Map<String, Material> woolLocations = new HashMap<>();
    private final Material[] wools = {Material.RED_WOOL, Material.ORANGE_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL, Material.LIGHT_BLUE_WOOL, Material.PURPLE_WOOL, Material.PINK_WOOL, Material.WHITE_WOOL};
    public WoolItem(PluginItems.ItemProvider provider) {
        super(provider, Material.RED_WOOL, "wool", "Wool", true, true, meta -> {
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
            stack.setType(Material.RED_WOOL);
            meta.setItemName("Red Wool");
        }else {
            stack.setType(team.getWool());
            String color = team.getColor().toLowerCase();
            color = color.substring(0,1).toUpperCase() + color.substring(1);
            meta.setItemName(color + " Wool");
        }
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public boolean isThisItem(ItemStack stack) {
        if (stack == null) return false;
        if (!Arrays.stream(wools).toList().contains(stack.getType())) return false;
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        String id = meta.getPersistentDataContainer().get(
                new NamespacedKey(this.plugin(), "item_id"),
                PersistentDataType.STRING
        );
        return Objects.equals(id, this.getId());
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!isThisItem(player.getInventory().getItemInMainHand())) {
            if (!isThisItem(player.getInventory().getItemInOffHand())) return;
        }
        Location l = event.getBlockPlaced().getLocation();
        Position pos = new Position(l.getBlockX(), l.getBlockY(), l.getBlockZ(), l.getWorld());
        woolLocations.put(pos.id(), event.getBlockPlaced().getType());
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Location l = event.getBlock().getLocation();
        Position pos = new Position(l.getBlockX(), l.getBlockY(), l.getBlockZ(), l.getWorld());
        if (!woolLocations.containsKey(pos.id())) return;
        Material origin = woolLocations.get(pos.id());
        woolLocations.remove(pos.id());
        if (origin != event.getBlock().getType()) return;
        event.setDropItems(false);
        event.getBlock().getWorld().dropItemNaturally(l.add(.5,.5,.5), getDefaultStack(event.getPlayer()));
    }
}