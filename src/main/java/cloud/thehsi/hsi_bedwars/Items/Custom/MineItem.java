package cloud.thehsi.hsi_bedwars.Items.Custom;

import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.Team;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.TeamController;
import cloud.thehsi.hsi_bedwars.Items.BaseItem;
import cloud.thehsi.hsi_bedwars.Items.PluginItems;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class MineItem extends BaseItem implements Listener {
    public MineItem(PluginItems.ItemProvider provider) {
        super(provider, Material.BLAZE_POWDER, "mine", "Mine", false, false, "A Disguised Mine\nPlace against block or Mine to\nDisguise Mine as that Block", meta -> {meta.setRarity(ItemRarity.RARE);return meta;});
    }

    private Player getPlayerOnBlock(Location location) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getLocation().getBlockX() == location.getBlockX() &&
                    p.getLocation().getBlockY() == location.getBlockY() &&
                    p.getLocation().getBlockZ() == location.getBlockZ())
                return p;
        }
        return null;
    }

    @EventHandler
    private void onClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK && event.getPlayer().isSneaking()) {
            double d = 1;
            BlockDisplay blockDisplay = null;
            Block target = event.getClickedBlock();
            assert target != null;
            for (BlockDisplay display : event.getPlayer().getWorld().getEntitiesByClass(BlockDisplay.class)) {
                double dist = display.getLocation().distance(target.getLocation());
                if (dist < d) {
                    d = dist;
                    blockDisplay = display;
                }
            }
            if (d != 1) {
                blockDisplay.remove();
                blockDisplay.getLocation().getBlock().setType(Material.AIR);
            }
        }
    }

    @Override
    public void onUse(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getPlayer().getCooldown(getDefaultStack()) != 0) return;
            Block clicked = event.getClickedBlock();
            assert clicked != null;
            Block target = clicked.getRelative(event.getBlockFace());
            if (target.getType() != Material.AIR) return;
            event.getPlayer().setCooldown(getDefaultStack(), 10);
            if (getPlayerOnBlock(target.getLocation()) != null) return;
            BlockDisplay display = target.getWorld().spawn(target.getLocation(), BlockDisplay.class);
            if (clicked.getBlockData().getMaterial() == Material.BARRIER) {
                double d = 1;
                BlockDisplay blockDisplay = null;
                for (BlockDisplay bd : event.getPlayer().getWorld().getEntitiesByClass(BlockDisplay.class)) {
                    double dist = bd.getLocation().distance(clicked.getLocation());
                    if (dist < d) {
                        d = dist;
                        blockDisplay = bd;
                    }
                }
                if (d != 1) {
                    display.setBlock(blockDisplay.getBlock());
                }else return;
            }else {
                display.setBlock(clicked.getBlockData());
            }
            getBuildTracker().registerChange(target, target.getBlockData(), false);
            target.setType(Material.BARRIER);
            Team team = TeamController.getPlayerTeam(event.getPlayer());
            final int[] tick = {0};
            int ticks = 60;
            new BukkitRunnable() {
                @Override
                public void run() {
                    tick[0]++;
                    tick[0] %= ticks;
                    double x = tick[0];
                    int d =
                            (int) (
                                Math.min(
                                    Math.sqrt(x/25) * 7, 7
                                ) + Math.min(
                                    Math.sqrt(-((x-ticks) / 25)) * 7, 7
                                ) + 1
                            );
                    if (
                        display.getLocation().getBlock().getLightFromBlocks() >
                        display.getLocation().getBlock().getLightFromSky()
                    )
                        display.setBrightness(new Display.Brightness(0, d));
                    else
                        display.setBrightness(new Display.Brightness(d, 0));
                    display.getWorld().spawnParticle(Particle.SMOKE, display.getLocation().add(.5,.5,.5), 1, 0.25, 0.25, 0.25, 0);
                    if (display.getLocation().getBlock().getType() != Material.BARRIER) cancel();
                    for (Player player : display.getWorld().getEntitiesByClass(Player.class)) {
                        if (player.getLocation().distance(display.getLocation()) < 2) {
                            if ((TeamController.getPlayerTeam(player) != team && team != null) || (event.getPlayer() != player && team == null)) {
                                TNTPrimed tnt = (TNTPrimed) display.getWorld().spawnEntity(display.getLocation(), EntityType.TNT);
                                tnt.setFuseTicks(1);
                                tnt.setYield(1.5f);
                                display.remove();
                                target.setType(Material.AIR);
                                cancel();
                                break;
                            }
                        }
                    }
                }
            }.runTaskTimer(plugin(), 0L, 1L);
            ItemStack stack = event.getItem();
            assert stack != null;
            stack.setAmount(stack.getAmount()-1);
            event.setCancelled(true);
        }
    }
    @Override
    public ItemStack inventoryTick(Player player, ItemStack stack) { return stack; }

}
