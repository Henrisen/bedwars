package cloud.thehsi.hsi_bedwars.BedwarsElements.Spawners.Custom;

import cloud.thehsi.hsi_bedwars.BedwarsElements.Spawners.BaseSpawner;
import org.bukkit.Location;
import org.bukkit.Material;

public class IronSpawner extends BaseSpawner {
    public IronSpawner(Location spawnLocation) {
        super(Material.IRON_INGOT, spawnLocation, 64, "Iron", 20);
    }

    @Override
    public String getId() { return "iron"; }
}
