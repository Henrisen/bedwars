package cloud.thehsi.hsi_bedwars.Items.Custom;

import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.Team;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.TeamController;
import cloud.thehsi.hsi_bedwars.Items.BaseItem;
import cloud.thehsi.hsi_bedwars.Items.PluginItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

import static cloud.thehsi.hsi_bedwars.BedwarsElements.Utils.killPlayer;

public class TheChanceItem extends BaseItem implements Listener {
    Random random = new Random(System.currentTimeMillis());
    public TheChanceItem(PluginItems.ItemProvider provider) {
        super(provider, Material.BRUSH, "the_chance", "The Chance", false, false, "Hit Someone,\n50% You Die\n50% They Die", meta -> {meta.setRarity(ItemRarity.EPIC);return meta;});
    }

    @Override
    public void onUse(PlayerInteractEvent event) {}
    @Override
    public ItemStack inventoryTick(Player player, ItemStack stack) { return stack; }

    @EventHandler
    public void onHitPlayer(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player attacker)) return;
        if (!(event.getEntity() instanceof Player attacked)) return;
        if (!isThisItem(attacker.getInventory().getItemInMainHand())) return;
        attacker.getInventory().getItemInMainHand().setAmount(0);
        if (random.nextBoolean()) {
            Bukkit.broadcastMessage(attacker.getDisplayName() + ChatColor.GRAY + " defeated the Odds against " + attacked.getDisplayName());
            attacked.damage(1000, attacker);
        }else {
            Bukkit.broadcastMessage(attacker.getDisplayName() + ChatColor.GRAY + " fell victim to the Odds");
            Team team = TeamController.getPlayerTeam(attacker);
            if (team != null) {
                if (team.players().size() == 1)
                    team.getBed().remove();
                team.removePlayer(attacker);
            }

            attacker.damage(1000, attacker);
            killPlayer(attacker);
        }
        event.setCancelled(true);
    }
}
