package cloud.thehsi.hsi_bedwars.BedwarsElements.Spawners;

import cloud.thehsi.hsi_bedwars.BedwarsElements.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class SpawnerController {
    public static List<BaseSpawner> spawners = new ArrayList<>();
    static long time;
    public static void init(Plugin plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            time++;
            for (BaseSpawner spawner : spawners) spawner.tick(time);
        }, 1, 1);
    }

    public static void remove() {
        for (BaseSpawner spawner : spawners) spawner.remove();
    }

    public static void addSpawner(BaseSpawner spawner) {
        spawners.add(spawner);
    }
    public static void addAndRegisterSpawner(BaseSpawner spawner) {
        spawners.add(spawner);
        JsonParser.addSpawner(spawner);
    }
}
