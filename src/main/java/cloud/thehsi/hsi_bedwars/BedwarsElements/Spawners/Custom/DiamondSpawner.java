package cloud.thehsi.hsi_bedwars.BedwarsElements.Spawners.Custom;

import cloud.thehsi.hsi_bedwars.BedwarsElements.Spawners.TextSpawner;
import org.bukkit.Location;
import org.bukkit.Material;

public class DiamondSpawner extends TextSpawner {
    public DiamondSpawner(Location spawnLocation) {
        super(Material.DIAMOND, spawnLocation, 16, "Diamond", 200);
    }

    @Override
    public String getId() { return "diamond"; }
}
