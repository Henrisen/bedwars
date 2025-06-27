package cloud.thehsi.hsi_bedwars.Items;

import cloud.thehsi.hsi_bedwars.BuildTracker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

abstract public class BaseItem implements Listener {
    Material itemType;
    String itemId;
    String itemName;
    Plugin plugin;
    Function<ItemMeta, ItemMeta> modifications;
    Boolean doesTick;
    Boolean doesTickEachItem;
    BuildTracker buildTracker;
    String description;

    public BaseItem(PluginItems.ItemProvider provider, Material type, String id, String name, Boolean doesTick, Boolean doesTickEachItem, String description, Function<ItemMeta, ItemMeta> modifications) {
        this.itemType = type;
        this.itemId = id;
        this.itemName = name;
        this.plugin = provider.plugin();
        this.doesTick = doesTick;
        this.doesTickEachItem = doesTickEachItem;
        this.modifications = modifications;
        this.buildTracker = provider.buildTracker();
        this.description = description;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @SuppressWarnings("unused")
    public static ItemMeta NONE(ItemMeta meta) {
        return meta;
    }

    public String getId() {return itemId;}
    public String getName() {return itemName;}
    public BuildTracker getBuildTracker() {return buildTracker;}

    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(itemType, 1);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.getPersistentDataContainer().set(
                new NamespacedKey(plugin, "item_id"),
                PersistentDataType.STRING,
                itemId
        );
        meta.setItemName(itemName);
        meta = modifications.apply(meta);
        stack.setItemMeta(meta);
        return stack;
    }

    public ItemStack getDefaultStack(Player player) {
        ItemStack stack = new ItemStack(itemType, 1);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.getPersistentDataContainer().set(
                new NamespacedKey(plugin, "item_id"),
                PersistentDataType.STRING,
                itemId
        );
        meta.setItemName(itemName);
        meta = modifications.apply(meta);
        stack.setItemMeta(meta);
        inventoryTick(player, stack);
        return stack;
    }

    public ItemStack getShopPreviewStack(Player viewer) {
        return getDefaultStack(viewer);
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

    public ItemStack getShopPreview(Player player, int cost, Material costMaterial, String materialName, Integer index) {
        ItemStack stack = getShopPreviewStack(player);
        ItemMeta meta = stack.getItemMeta();
        if (meta == null) {
            Bukkit.broadcastMessage(ChatColor.RED + "Error in Item '" + this.getId() + "'. Meta is null");
            return new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        }
        String itemName = meta.getItemName();
        List<String> lore = new ArrayList<>();
        if (costMaterial == Material.AIR)
            lore.add(ChatColor.GRAY + "Cost: " + ChatColor.WHITE + "FREE");
        else
            lore.add(ChatColor.GRAY + "Cost: " + switch (materialName.toLowerCase()) {
                case "gold" -> ChatColor.GOLD;
                case "diamond" -> ChatColor.AQUA;
                case "emerald" -> ChatColor.DARK_GREEN;
                default -> ChatColor.WHITE;
            }  + cost + " " + materialName);
        lore.add("");
        for (String line : description.split("\n"))
            lore.add(ChatColor.GRAY + line);
        lore.add("");
        if (countInInventory(player, costMaterial) >= cost) {
            meta.setItemName(ChatColor.GREEN + itemName);
            lore.add(ChatColor.YELLOW + "Click to purchase!");
        }else {
            meta.setItemName(ChatColor.RED + itemName);
            lore.add(ChatColor.RED + "You don't have enough " + materialName);
        }
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(
                new NamespacedKey(plugin, "store_id"),
                PersistentDataType.INTEGER,
                index
        );
        stack.setItemMeta(meta);
        return stack;
    }

    public Plugin plugin() {
        return plugin;
    }

    public boolean isThisItem(ItemStack stack) {
        if (stack == null) return false;
        if (stack.getType() != itemType) return false;
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        String id = meta.getPersistentDataContainer().get(
                new NamespacedKey(plugin, "item_id"),
                PersistentDataType.STRING
        );
        return Objects.equals(id, itemId);
    }

    abstract public void onUse(PlayerInteractEvent event);
    abstract public ItemStack inventoryTick(Player player, ItemStack stack);

    public ItemStack getInInventory(Player player) {
        for (ItemStack stack : player.getInventory()) {
            if (stack == null) continue;
            if (isThisItem(stack)) return stack;
        }
        return null;
    }

    public List<ItemStack> getAllInInventory(Player player) {
        List<ItemStack> stacks = new ArrayList<>();
        for (ItemStack stack : player.getInventory()) {
            if (stack == null) continue;
            if (isThisItem(stack)) stacks.add(stack);
        }
        return stacks;
    }

    public int countInInventory(Player player) {
        int i = 0;
        for (ItemStack stack : player.getInventory()) {
            if (stack == null) continue;
            if (isThisItem(stack)) i++;
        }
        return i;
    }

    public void tick() {
        if (!doesTick) return;
        for (Player p : Bukkit.getOnlinePlayers()) {
            for (ItemStack stack : getAllInInventory(p)) {
                if (stack != null) inventoryTick(p, stack);
                if (!doesTickEachItem) break;
            }
        }
    }
}
