package cloud.thehsi.hsi_bedwars.BedwarsElements.Spawners.Custom;

import cloud.thehsi.hsi_bedwars.BedwarsElements.Spawners.TextSpawner;
import org.bukkit.Location;
import org.bukkit.Material;

public class EmeraldSpawner extends TextSpawner {
    public EmeraldSpawner(Location spawnLocation) {
        super(Material.EMERALD, spawnLocation, 8, "Emerald", 500);
    }

    @Override
    public String getId() { return "emerald"; }
}
