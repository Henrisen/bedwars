package cloud.thehsi.hsi_bedwars.Items.Custom;

import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.Team;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.TeamController;
import cloud.thehsi.hsi_bedwars.Items.BaseItem;
import cloud.thehsi.hsi_bedwars.Items.PluginItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;


public class BridgeEggItem extends BaseItem {
    public BridgeEggItem(PluginItems.ItemProvider provider) {
        super(provider, Material.BLUE_EGG, "bridge_egg", "Bridge Egg", false, false, "An Egg that creates a Bridge\nwhere ever it gets thrown.", meta -> {meta.setRarity(ItemRarity.UNCOMMON);return meta;});
    }

    @EventHandler
    private void onThrow(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();
        if (!(projectile instanceof Egg egg)) return;
        if (!isThisItem(egg.getItem())) return;
        ProjectileSource source = egg.getShooter();
        if (!(source instanceof Player player)) return;
        projectile.setGravity(false);
        Vector direction = projectile.getVelocity().normalize();
        projectile.setVelocity(new Vector(0,0,0));
        final int[] distance = {0};
        new BukkitRunnable() {
            @Override
            public void run() {
                if (egg.isDead()) {
                    egg.remove();
                    cancel();
                    return;
                }
                distance[0]++;
                if (distance[0] > 31) {
                    egg.remove();
                    cancel();
                    return;
                }
                Location location = projectile.getLocation();
                location.add(direction);
                projectile.teleport(location);
                Block block = location.add(0,-2, 0).getBlock();
                Team team = TeamController.getPlayerTeam(player);
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        Block b = block.getRelative(x, 0, z);
                        if (!b.getType().isAir()) continue;
                        getBuildTracker().registerChange(b, b.getBlockData(), false);
                        if (team == null)
                            b.setType(Material.RED_WOOL);
                        else
                            b.setType(team.getWool());
                    }
                }
            }
        }.runTaskTimer(plugin(), 0, 1);
    }

    @Override
    public void onUse(PlayerInteractEvent event) {}

    @Override
    public ItemStack inventoryTick(Player player, ItemStack stack) { return stack; }
}
