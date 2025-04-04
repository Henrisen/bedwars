package cloud.thehsi.hsi_bedwars.BedwarsElements.Spawners.Custom;

import cloud.thehsi.hsi_bedwars.BedwarsElements.Spawners.BaseSpawner;
import org.bukkit.Location;
import org.bukkit.Material;

public class GoldSpawner extends BaseSpawner {
    public GoldSpawner(Location spawnLocation) {
        super(Material.GOLD_INGOT, spawnLocation, 32, "Gold", 100);
    }

    @Override
    public String getId() { return "gold"; }
}
