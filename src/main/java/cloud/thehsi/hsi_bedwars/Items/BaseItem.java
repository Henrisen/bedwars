package cloud.thehsi.hsi_bedwars.Items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.Objects;
import java.util.function.Function;

abstract public class BaseItem implements Listener {
    Material itemType;
    String itemId;
    String itemName;
    Plugin plugin;
    Function<ItemMeta, ItemMeta> modifications;
    Boolean doesTick;
    public BaseItem(Plugin plugin, Material type, String id, String name, Boolean doesTick, Function<ItemMeta, ItemMeta> modifications) {
        this.itemType = type;
        this.itemId = id;
        this.itemName = name;
        this.plugin = plugin;
        this.doesTick = doesTick;
        this.modifications = modifications;
    }

    public static ItemMeta NONE(ItemMeta meta) {
        return meta;
    }

    public String getId() {return itemId;}
    public String getName() {return itemName;}

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
    abstract public void inventoryTick(Player player, ItemStack stack);

    @EventHandler
    private void use(PlayerInteractEvent event) {
        if (!isThisItem(event.getItem())) return;
        onUse(event);
    }

    public ItemStack getInInventory(Player player) {
        for (ItemStack stack : player.getInventory()) {
            if (stack == null) continue;
            if (isThisItem(stack)) return stack;
        }
        return null;
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
            ItemStack stack = getInInventory(p);
            if (stack != null) inventoryTick(p, stack);
        }
    }
}
