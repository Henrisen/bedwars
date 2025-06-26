package cloud.thehsi.hsi_bedwars.BedwarsElements.Spawners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public abstract class BaseSpawner {
    Location itemLocation;
    int max;
    int interval;
    Material item;
    String itemName;
    public BaseSpawner(Material item, Location spawnLocation, int max, String name, int ticksPerSpawn) {
        World world = spawnLocation.getWorld();
        assert world != null;

        this.item = item;
        assert spawnLocation.getWorld() != null;
        this.itemLocation = spawnLocation;
        this.max = max;
        this.interval = ticksPerSpawn;
        this.itemName = name;
    }

    abstract public String getId();
    public Location getSpawnLocation() {
        return itemLocation;
    }

    void spawnItem(Location location, ItemStack stack) {
        World world = location.getWorld();
        assert world != null;
        if (!world.isChunkLoaded(location.getChunk())) return;
        if (world.getPlayers().isEmpty()) return;
        Item item = world.dropItem(location, stack);
        item.setVelocity(item.getVelocity().multiply(.25));
    }

    int countItems(Location location, Material type) {
        World world = location.getWorld();
        if (world==null) return Integer.MAX_VALUE;
        if (!world.isChunkLoaded(location.getChunk())) return Integer.MAX_VALUE;
        if (world.getPlayers().isEmpty()) return Integer.MAX_VALUE;
        int count = 0;
        for (Entity entity : world.getEntities())
        {
            if (entity instanceof Item _item)
                if (_item.getLocation().distance(location) < 4 && _item.getItemStack().getType() == type)
                    count += _item.getItemStack().getAmount();
        }
        return count;
    }

    public void tick(long time) {
        if (time % interval == 0)
            if (countItems(itemLocation, item) < max)
                spawnItem(itemLocation, new ItemStack(item));
    }

    public void remove() {}
}