package cloud.thehsi.hsi_bedwars.BedwarsElements;

import cloud.thehsi.hsi_bedwars.BedwarsElements.Spawners.*;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Spawners.Custom.DiamondSpawner;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Spawners.Custom.EmeraldSpawner;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Spawners.Custom.GoldSpawner;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Spawners.Custom.IronSpawner;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.Team;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.TeamController;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.Map;

public class JsonParser {
    static final Gson gson = new Gson();
    static File config = new File("./world/bedwars.json");
    static JsonObject json = null;
    public static void loadFile() {
        if (config.exists()) {
            try {
                json = gson.fromJson(new FileReader(config), JsonObject.class);
            } catch (FileNotFoundException e) {throw new RuntimeException(e);}
        }else {
            try {
                boolean ignored = config.createNewFile();
            } catch (IOException e) {throw new RuntimeException(e);}
            try (FileWriter writer = new FileWriter(config)) {
                writer.write("""
                        {
                            "teams": {},
                            "spawners": []
                        }""");
            } catch (IOException ex) {throw new RuntimeException(ex);}
            try {
                json = gson.fromJson(new FileReader(config), JsonObject.class);
            } catch (FileNotFoundException e) {throw new RuntimeException(e);}
        }
    }

    private static Location locationFromJsonArray(JsonArray locationArray) {
        assert locationArray.size() == 4;
        return new Location(
                Bukkit.getWorld(locationArray.get(0).getAsString()),
                locationArray.get(1).getAsFloat(),
                locationArray.get(2).getAsFloat(),
                locationArray.get(3).getAsFloat()
        );
    }

    private static JsonArray jsonArrayFromLocation(Location location) {
        assert location.getWorld() != null;
        JsonArray rawLocation = new JsonArray();
        rawLocation.add(location.getWorld().getName());
        rawLocation.add(location.getX());
        rawLocation.add(location.getY());
        rawLocation.add(location.getZ());
        return rawLocation;
    }


    public static void loadSpawners() {
        JsonArray spawners = json.get("spawners").getAsJsonArray();
        for (JsonElement element : spawners) {
            JsonObject spawnerData = element.getAsJsonObject();
            String type = spawnerData.get("type").getAsString();
            Location location = locationFromJsonArray(spawnerData.get("location").getAsJsonArray());
            switch (type) {
                case "iron" -> SpawnerController.addSpawner(new IronSpawner(location));
                case "gold" -> SpawnerController.addSpawner(new GoldSpawner(location));
                case "diamond" -> SpawnerController.addSpawner(new DiamondSpawner(location));
                case "emerald" -> SpawnerController.addSpawner(new EmeraldSpawner(location));
                default -> Bukkit.broadcastMessage(ChatColor.RED + "Unknown Spawner Type: '" + type + "'");
            }
        }
    }

    public static Location getCenter() {
        return locationFromJsonArray(json.get("center").getAsJsonArray());
    }

    public static void loadTeams(Plugin plugin) {
        JsonObject teams = json.get("teams").getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : teams.entrySet()) {
            String color = entry.getKey();
            JsonElement element = entry.getValue();
            JsonObject teamData = element.getAsJsonObject();

            Location spawnpoint = locationFromJsonArray(teamData.get("spawnpoint").getAsJsonArray());
            JsonObject bedArray = teamData.get("bed").getAsJsonObject();
            Location bed1 = locationFromJsonArray(bedArray.get("1").getAsJsonArray());
            Location bed2 = locationFromJsonArray(bedArray.get("2").getAsJsonArray());
            Team team = new Team(color, bed1, bed2, spawnpoint, plugin);
            TeamController.addTeam(team);
        }
    }

    public static void addTeam(Team team) {
        JsonObject team_data = new JsonObject();
        JsonObject teams = json.get("teams").getAsJsonObject();
        JsonArray spawnpoint = jsonArrayFromLocation(team.getSpawnpoint().getSpawnpointLocation());
        JsonObject bed = new JsonObject();
        JsonArray bed1 = jsonArrayFromLocation(team.getBed().getBedLocation(false));
        JsonArray bed2 = jsonArrayFromLocation(team.getBed().getBedLocation(true));
        bed.add("1", bed1);
        bed.add("2", bed2);
        team_data.add("bed", bed);
        team_data.add("spawnpoint", spawnpoint);
        teams.add(team.getColor(), team_data);
        updateFile();
    }

    public static void addSpawner(BaseSpawner spawner) {
        JsonObject spawner_data = new JsonObject();
        JsonArray spawners = json.get("spawners").getAsJsonArray();
        spawner_data.addProperty("type", spawner.getId());
        JsonArray rawLocation = jsonArrayFromLocation(spawner.getSpawnLocation());
        spawner_data.add("location", rawLocation);
        spawners.add(spawner_data);
        updateFile();
    }

    public static void updateFile() {
        try (FileWriter writer = new FileWriter(config)){
            writer.write(gson.toJson(json));
        } catch (IOException e) {throw new RuntimeException(e);}
    }
}
