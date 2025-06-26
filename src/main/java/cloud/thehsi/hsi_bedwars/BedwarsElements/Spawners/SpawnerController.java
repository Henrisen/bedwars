package cloud.thehsi.hsi_bedwars.BedwarsElements.Spawners;

import cloud.thehsi.hsi_bedwars.BedwarsElements.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class SpawnerController {
    public static List<BaseSpawner> spawners = new ArrayList<>();
    static long time = 0;
    static BukkitTask task;
    public static void init(Plugin plugin) {
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            time++;
            for (BaseSpawner spawner : spawners) spawner.tick(time);
        }, 1, 1);
    }

    public static void remove() {
        for (BaseSpawner spawner : spawners) {
            spawner.remove();
        };
        spawners.clear();
        task.cancel();
        time = 0;
    }

    public static void addSpawner(BaseSpawner spawner) {
        spawners.add(spawner);
    }
    public static void addAndRegisterSpawner(BaseSpawner spawner) {
        spawners.add(spawner);
        JsonParser.addSpawner(spawner);
    }
}
