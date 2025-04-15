package cloud.thehsi.hsi_bedwars.Items.Custom;

import cloud.thehsi.hsi_bedwars.BuildTracker;
import cloud.thehsi.hsi_bedwars.Items.BaseItem;
import cloud.thehsi.hsi_bedwars.Items.PluginItems;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JumpPadItem extends BaseItem implements Listener {
    List<JumpPad> pads = new ArrayList<>();
    public JumpPadItem(PluginItems.ItemProvider provider) {
        super(provider, Material.SLIME_BLOCK, "jump_pad", "Pump Pad", false, meta -> {meta.setRarity(ItemRarity.RARE);return meta;});
    }

    static class JumpPad {
        Random random = new Random();
        Location center;
        boolean active;
        BuildTracker tracker;
        BlockDisplay outline;
        static private final List<Material> cannotReplace = List.of(Material.BARRIER, Material.RED_BED, Material.ORANGE_BED, Material.YELLOW_BED, Material.LIME_BED, Material.LIGHT_BLUE_BED, Material.PURPLE_BED, Material.PINK_BED, Material.WHITE_BED, Material.EMERALD_BLOCK, Material.DIAMOND_BLOCK, Material.RAW_GOLD_BLOCK, Material.RAW_IRON_BLOCK);
        public void remove() {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    Block b = center.clone().add(x,0,z).getBlock();
                    if (cannotReplace.contains(b.getType())) continue;
                    tracker.undoChangesAtBlock(b);
                }
            }
            outline.remove();
            active = false;
        }

        public boolean isThisPad(Block block) {
            if (block.getType()!=Material.SLIME_BLOCK) return false;
            Location l = block.getLocation();
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    if(center.clone().add(x,0,z).equals(l)) return true;
                }
            }
            return false;
        }

        public JumpPad(Location center, Plugin plugin, BuildTracker tracker) {
            this.center = center;
            active = true;
            this.tracker = tracker;
            assert center.getWorld() != null;
            outline = center.getWorld().spawn(center.clone().add(-0.995,.005,-0.995), BlockDisplay.class);
            outline.setTransformation(new Transformation(new Vector3f(), new AxisAngle4f(), new Vector3f(2.99f,.99f,2.99f), new AxisAngle4f()));
            outline.setBlock(Material.SLIME_BLOCK.createBlockData());
            outline.setGlowing(true);
            outline.setGlowColorOverride(Color.GREEN);
            outline.setViewRange(16f);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (active)
                        remove();
                }
            }.runTaskLater(plugin, 15*20);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!active) {
                        cancel();
                        return;
                    }
                    for (int x = -1; x <= 1; x++) {
                        for (int z = -1; z <= 1; z++) {
                            Location l = center.clone().add(x, 0, z);
                            if (l.getBlock().getType()==Material.SLIME_BLOCK) continue;
                            if (cannotReplace.contains(l.getBlock().getType())) continue;
                            tracker.registerChange(l.getBlock(), l.getBlock().getBlockData(), true);
                            l.getBlock().setType(Material.SLIME_BLOCK, false);
                        }
                    }
                    List<Player> options = new ArrayList<>(Bukkit.getOnlinePlayers());
                    List<Player> ignore = new ArrayList<>();
                    options.removeIf(player -> player.getLocation().distance(center) > 4);
                    for (int x = -20; x < 40; x++) {
                        for (int z = -20; z < 40; z++) {
                            Location l = center.clone().add(x/20d,1.1, z/20d);
                            assert center.getWorld() != null;
                            if (random.nextDouble() < .00625d && !l.getBlock().getType().isSolid())
                                center.getWorld().spawnParticle(Particle.BLOCK, l, 1, Material.SLIME_BLOCK.createBlockData());
                            for (Player player : options) {
                                if (ignore.contains(player)) continue;
                                if (player.getLocation().distance(l) < 0.25) {
                                    ignore.add(player);
                                    Vector velocity = player.getVelocity();
                                    velocity.multiply(16);
                                    velocity.setY(2);
                                    player.setVelocity(velocity);
                                }
                            }
                        }
                    }
                }
            }.runTaskTimer(plugin, 0L, 1L);
        }

        public static JumpPad place(Location center, BuildTracker buildTracker, Plugin plugin) {
            JumpPad pad = new JumpPad(center, plugin, buildTracker);
            pad.center = center;
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    Location l = center.clone().add(x, 0, z);
                    if (l.getBlock().getType() == Material.SLIME_BLOCK) continue;
                    if (cannotReplace.contains(l.getBlock().getType())) continue;
                    buildTracker.registerChange(l.getBlock(), l.getBlock().getBlockData(), true);
                    l.getBlock().setType(Material.SLIME_BLOCK, false);

                    // Removing Grass and other
                    switch (l.add(0,1,0).getBlock().getType()) {
                        case Material.SHORT_GRASS:
                        case Material.BUSH:
                        case Material.POTATOES:
                            buildTracker.registerChange(l.getBlock(), l.getBlock().getBlockData(), true);
                            l.getBlock().setType(Material.AIR, false);
                            break;
                        case Material.PITCHER_CROP:
                        case Material.TALL_GRASS:
                            buildTracker.registerChange(l.getBlock(), l.getBlock().getBlockData(), true);
                            l.getBlock().setType(Material.AIR, false);
                            if (l.add(0,1,0).getBlock().getType().isAir()) break;
                            buildTracker.registerChange(l.getBlock(), l.getBlock().getBlockData(), true);
                            l.getBlock().setType(Material.AIR, false);
                    }
                }
            }
            return pad;
        }
    }

    @Override
    public void onUse(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getPlayer().getCooldown(getDefaultStack()) != 0) return;
            Block clicked = event.getClickedBlock();
            assert clicked != null;
            Block target = clicked.isPassable() ? clicked : clicked.getRelative(event.getBlockFace());
            pads.add(JumpPad.place(target.getLocation(), getBuildTracker(), plugin()));
            ItemStack stack = event.getItem();
            assert stack != null;
            stack.setAmount(stack.getAmount()-1);
            event.setCancelled(true);
        }else if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (event.getPlayer().getCooldown(getDefaultStack()) != 0) return;
            Location l = event.getPlayer().getLocation().clone();
            int depth = -1;
            for (int i = -1; i >= -5; i--) {
                if (l.clone().add(0,i,0).getBlock().getType().isSolid()) break;
                depth--;
            }
            l.add(0,depth+1,0);
            if (depth==-1) event.getPlayer().teleport(event.getPlayer().getLocation().add(0,1,0));

            Location target = new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ());

            pads.add(JumpPad.place(target, getBuildTracker(), plugin()));
            ItemStack stack = event.getItem();
            assert stack != null;
            stack.setAmount(stack.getAmount()-1);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDestroy(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;

        List<JumpPad> pads2 = new ArrayList<>(List.copyOf(pads));
        for (JumpPad pad : pads) {
            if (pad.center.distance(event.getClickedBlock().getLocation()) > 4d) continue;
            if (!pad.isThisPad(event.getClickedBlock())) continue;
            pad.remove();
            pads2.remove(pad);
        }
        pads.clear();
        pads.addAll(pads2);
    }

    @Override
    public void inventoryTick(Player player, ItemStack stack) {}

}
