package cloud.thehsi.hsi_bedwars.Items.Custom;

import cloud.thehsi.hsi_bedwars.Items.BaseItem;
import cloud.thehsi.hsi_bedwars.Items.PluginItems;
import org.bukkit.*;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class FireballItem extends BaseItem {
    public FireballItem(PluginItems.ItemProvider provider) {
        super(provider, Material.FIRE_CHARGE, "fireball", "Fireball", false, false, "Right click to Launch! Great to knock\nback enemies walking on thin bridges.", meta -> {
            meta.setRarity(ItemRarity.RARE);
            return meta;
        });
    }

    private Vector rotateAroundYAxisAndPitch(Vector vector, float yaw, float pitch) {
        double yawAngle = Math.toRadians(yaw);
        double pitchAngle = Math.toRadians(pitch);

        // First, rotate around Y-axis (yaw)
        double cosYaw = Math.cos(yawAngle);
        double sinYaw = Math.sin(yawAngle);

        double x = vector.getX() * cosYaw - vector.getZ() * sinYaw;
        double z = vector.getX() * sinYaw + vector.getZ() * cosYaw;

        // Then, apply pitch-based rotation
        double cosPitch = Math.cos(pitchAngle);
        double sinPitch = Math.sin(pitchAngle);

        // Adjust the Y-offset based on pitch
        double y = vector.getY() * cosPitch - vector.getZ() * sinPitch;

        // Return the new vector
        return new Vector(x, y, z);
    }

    private static Vector getOffset(int ticksLived, double radius, float fireballPitch) {
        double angle = Math.PI / 10 * ticksLived;

        // Calculate the offset to make the particle move in a circular pattern
        double xOffset = radius * Math.cos(angle);
        double zOffset = radius * Math.sin(angle);

        // Calculate Y offset based on fireball's pitch
        double yOffset = radius * Math.sin(Math.toRadians(fireballPitch)); // Use pitch for vertical offset

        // Get the horizontal rotation of the particle based on fireball's yaw
        return new Vector(xOffset, yOffset, zOffset);
    }

    @Override
    public void onUse(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        Location eyes = player.getEyeLocation();

        Vector acceleration = (eyes.clone().add(eyes.getDirection())).subtract(eyes).toVector();
        acceleration = acceleration.multiply(0.05d);

        Fireball fireball = player
                .getWorld()
                .spawn(eyes.add(eyes.getDirection()), Fireball.class);

        fireball.setShooter(player);
        fireball.setYield(2.5f);
        fireball.setDirection(eyes.getDirection());
        fireball.setAcceleration(acceleration);

        event.setCancelled(true);
        ItemStack stack = event.getItem();
        assert stack != null;
        stack.setAmount(stack.getAmount()-1);

        new BukkitRunnable() {
            @Override
            public void run() {
                // Check if the fireball is still alive
                if (!fireball.isDead()) {
                    // Get the ticks the fireball has been alive
                    int ticksLived = fireball.getTicksLived();

                    // Calculate how far from the fireball the particles should spread
                    double radius = Math.min(1 + (ticksLived / 100.0), 3); // Max radius of 3, adjust as needed

                    // Number of particles in the circle
                    int particleCount = 1; // Only one particle will be placed at a time per tick

                    // Get fireball's current yaw (direction the fireball is facing)
                    float fireballYaw = fireball.getLocation().getYaw();
                    float fireballPitch = fireball.getLocation().getPitch(); // Get the fireball's pitch

                    // Loop to spawn particles around the fireball
                    for (int i = 0; i < particleCount; i++) {
                        // Calculate the angle for the particle (increment each tick)
                        Vector offset = getOffset(ticksLived, radius, fireballPitch);

                        // Rotate the offset based on both the fireball's yaw and pitch
                        offset = rotateAroundYAxisAndPitch(offset, fireballYaw, fireballPitch);

                        // Calculate the final position
                        fireball.getWorld().spawnParticle(
                                Particle.FLAME,                     // Particle type
                                fireball.getLocation().getX() + offset.getX(),  // X offset from fireball
                                fireball.getLocation().getY() + offset.getY(),  // Y offset from fireball
                                fireball.getLocation().getZ() + offset.getZ(),  // Z offset from fireball
                                0,                                  // Only one particle at a time per position
                                0, 0, 0,                            // No extra offsets
                                0 // No extra speed
                        );
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin(), 0L, 1L); // Run every tick
    }

    @EventHandler
    private void fireballDamageReduction(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
            event.setDamage(5);

        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!event.getEntity().isDead())
                        event.getEntity().setVelocity(event.getEntity().getVelocity().multiply(1.75));
                }
            }.runTaskLater(plugin(), 2);
        }
    }

    @Override
    public ItemStack inventoryTick(Player player, ItemStack stack) { return stack; }
}