package cloud.thehsi.hsi_bedwars.Items.Custom;

import cloud.thehsi.hsi_bedwars.BedwarsElements.JsonParser;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Trader;
import cloud.thehsi.hsi_bedwars.Items.BaseItem;
import cloud.thehsi.hsi_bedwars.Items.PluginItems;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

import static cloud.thehsi.hsi_bedwars.BedwarsElements.Trader.removeItems;

public class PopupShopItem extends BaseItem {
    public PopupShopItem(PluginItems.ItemProvider provider) {
        super(provider, Material.CLAY_BALL, "popup_shop", "Popup Shop", false, false, "Right click to open a portable Shop.", meta -> {
            meta.setRarity(ItemRarity.RARE);
            return meta;
        });
    }
    List<Player> players = new ArrayList<>();

    @Override
    public void remove() {
        players.clear();
    }

    public void onUse(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        Inventory inv = generateInventory(player);
        player.openInventory(inv);
    }

    @Override
    public ItemStack inventoryTick(Player player, ItemStack stack) { return stack; }

    private Inventory generateInventory(Player viewer) {
        Inventory inv = Bukkit.createInventory(viewer, 9*6, "Item Shop");
        players.add(viewer);
        int index = 0;
        for (Trader.TradeableItem item : JsonParser.getTrades()) {
            inv.addItem(item.getAsPreviewItemStack(viewer, index));
            index++;
        }
        return inv;
    }

    @EventHandler
    private void onItemClick(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder() instanceof Player player)) return;
        if (!players.contains(player)) return;
        players.remove(player);
    }

    @EventHandler
    private void onItemClick(InventoryClickEvent event) {
        if (event.getClick() == ClickType.DOUBLE_CLICK) return;
        if (event.getClickedInventory() == null) return;
        if (!(event.getClickedInventory().getHolder() instanceof Player player)) return;
        if (!players.contains(player)) return;
        ItemStack stack = event.getCurrentItem();
        if (stack == null) return;
        ItemMeta meta = stack.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin(), "store_id");
        if (!container.has(key)) return;
        if (!container.has(key, PersistentDataType.INTEGER)) return;
        Integer index = container.get(key, PersistentDataType.INTEGER);
        if (index == null) return;
        List<Trader.TradeableItem> items = JsonParser.getTrades();
        if (items.size() <= index) return;
        if (index < 0) return;
        Trader.TradeableItem item = JsonParser.getTrades().get(index);
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
            event.getClickedInventory().setContents(generateInventory(player).getContents());
        }else {
            player.getInventory().addItem(new ItemStack(item.costMaterial, item.cost));
            player.sendMessage(ChatColor.RED + "Purchase Failed! Your Inventory is full!");
        }
        event.setCancelled(true);
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
}