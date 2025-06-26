package cloud.thehsi.hsi_bedwars.BedwarsElements;

import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.Team;
import cloud.thehsi.hsi_bedwars.Items.BaseItem;
import cloud.thehsi.hsi_bedwars.Items.PluginItems;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static java.lang.Math.atan2;
import static java.lang.Math.toDegrees;

public class Trader implements Listener {
    public static class TradeableItem {
        BaseItem item;
        Integer cost;
        Integer count;
        Material costMaterial;
        String costName;

        public TradeableItem(BaseItem item, Integer count, Integer cost, Material costMaterial, String costName) {
            this.item = item;
            this.count = count;
            this.cost = cost;
            this.costMaterial = costMaterial;
            this.costName = costName;
        }

        public ItemStack getAsPreviewItemStack(Player viewer, Integer index) {
            ItemStack stack = item.getShopPreview(viewer, cost, costMaterial, costName, index);
            stack.setAmount(count);
            return stack;
        }

        public ItemStack getAsPurchaseItemStack(Player viewer) {
            ItemStack stack = item.getDefaultStack(viewer);
            stack.setAmount(count);
            return stack;
        }

        public String formattedCostName() {
            return costName.substring(0,1).toUpperCase() + costName.substring(1).toLowerCase();
        }
    }

    Location spawnpoint;
    Villager villager;
    Team team;
    PluginItems pluginItems;
    Plugin plugin;
    public Trader(Location spawnpoint, Team team, PluginItems pluginItems, Plugin plugin) {
        this.spawnpoint = spawnpoint;
        this.plugin = plugin;
        this.team = team;
        this.pluginItems = pluginItems;
        assert spawnpoint.getWorld() != null;

        Location spawnpointRotated = applyRotateToCenter(spawnpoint);

        villager = spawnpoint.getWorld().spawn(spawnpointRotated, Villager.class);
        villager.setAI(false);
        villager.setSilent(true);
        villager.setVillagerType(Villager.Type.TAIGA);
        villager.setProfession(Villager.Profession.LEATHERWORKER);
        villager.setVillagerLevel(3);
        villager.setGravity(false);
        villager.setInvulnerable(true);
        villager.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, PotionEffect.INFINITE_DURATION, 64));
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private Location applyRotateToCenter(Location location) {
        location = location.clone();
        Location center = JsonParser.getCenter();
        double dx = center.getX() - location.getX();
        double dz = center.getZ() - location.getZ();

        float yaw = (float) toDegrees(atan2(-dx, dz));

        yaw = Math.round(yaw / 90f) * 90f + 180f;

        location.setYaw(yaw);
        location.setPitch(0);

        return location;
    }

    public Location getSpawnLocation() { return spawnpoint; }
    public void setSpawnLocation(Location location) { this.spawnpoint = location; }

    private Inventory generateInventory(Player viewer) {
        Inventory inv = Bukkit.createInventory(villager, 9*6, "Item Shop");
        int index = 0;
        for (TradeableItem item : JsonParser.getTrades()) {
            inv.addItem(item.getAsPreviewItemStack(viewer, index));
            index++;
        }
        return inv;
    }

    @EventHandler
    private void onClick(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() != villager) return;
        event.setCancelled(true);
        Player p = event.getPlayer();
        Inventory inventory = generateInventory(p);
        p.closeInventory();
        p.openInventory(inventory);
    }

    private int countInInventory(Player player, Material material) {
        int i=0;
        for (ItemStack stack : player.getInventory()) {
            if (stack==null) continue;
            if (stack.getType()!=material) continue;
            i+=stack.getAmount();
        }
        return i;
    }

    public static void removeItems(Inventory inventory, Material type, int amount) {
        if (amount <= 0) return;
        int size = inventory.getSize();
        for (int slot = 0; slot < size; slot++) {
            ItemStack is = inventory.getItem(slot);
            if (is == null) continue;
            if (type == is.getType()) {
                int newAmount = is.getAmount() - amount;
                if (newAmount > 0) {
                    is.setAmount(newAmount);
                    break;
                } else {
                    inventory.clear(slot);
                    amount = -newAmount;
                    if (amount == 0) break;
                }
            }
        }
    }

    @EventHandler
    private void onItemClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getHolder() != villager) return;
        ItemStack stack = event.getCurrentItem();
        if (stack == null) return;
        ItemMeta meta = stack.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "store_id");
        if (!container.has(key)) return;
        if (!container.has(key, PersistentDataType.INTEGER)) return;
        Integer index = container.get(key, PersistentDataType.INTEGER);
        if (index == null) return;
        Player player = (Player) event.getWhoClicked();
        List<TradeableItem> items = JsonParser.getTrades();
        if (items.size() <= index) return;
        if (index > 0) return;
        TradeableItem item = JsonParser.getTrades().get(index);
        if (item == null) return;
        int count = countInInventory(player, item.costMaterial);
        if (count < item.cost) {
            player.sendMessage(ChatColor.RED + "You don't have enough " + item.formattedCostName() + "! Need " + (item.cost - count) + " more!");
            event.setCancelled(true);
            return;
        }
        int purchases = 1;
        if (event.getClick().isShiftClick())
            purchases = count / item.cost;
        removeItems(player.getInventory(), item.costMaterial, item.cost * purchases);
        int space = 0;
        for (ItemStack s : player.getInventory().getContents())
            if (s!=null) space++;
        if (player.getInventory().getSize() > space) {
            for (int i = 0; i < purchases; i++) {
                ItemStack purchase = item.getAsPurchaseItemStack(player);
                player.getInventory().addItem(purchase);
            }
            player.sendMessage(ChatColor.GREEN + "You purchased " + ChatColor.GOLD + item.item.getName());
        }else {
            player.getInventory().addItem(new ItemStack(item.costMaterial, item.cost));
            player.sendMessage(ChatColor.RED + "Purchase Failed! Your Inventory is full!");
        }
        event.setCancelled(true);
    }
}
