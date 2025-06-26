package cloud.thehsi.hsi_bedwars.BedwarsElements.Teams;

import cloud.thehsi.hsi_bedwars.BedwarsElements.JsonParser;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class TeamController {
    public static List<Team> teams = new ArrayList<>();
    static Plugin plugin;
    static final Random rng = new Random(new Date().getTime());
    public static void init(Plugin plugin) {
        TeamController.plugin = plugin;
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            StringBuilder bar = new StringBuilder();
            for (Team team : teams) {
                bar.append(team.players.isEmpty() ? "":team.renderIcon()+" ");
                team.tick();
            }
            for (Player player : Bukkit.getOnlinePlayers()) player.spigot().sendMessage(
                ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(bar.toString().stripTrailing())
            );
        }, 0, 1);
    }

    public static void addTeam(Team team) {
        teams.add(team);
        Bukkit.getPluginManager().registerEvents(team, plugin);
        team.getBed().place();
    }
    public static void addAndRegisterTeam(Team team) {
        teams.add(team);
        JsonParser.addTeam(team);
    }
    public static void updateTeam(Team team) {
        JsonParser.addTeam(team);
    }
    public static Team getPlayerTeam(Player player) {
        for (Team team : teams) {
            if (team.players.contains(player)) return team;
        }
        return null;
    }

    public static Team getTeamByColor(String color) {
        for (Team team : teams) {
            if (Objects.equals(team.color, color)) return team;
        }
        return null;
    }

    public static void remove() {
        for (Team team : teams) {
            team.getBed().remove();
        }
    }

    public static Team getBedOwner(Location location) {
        for (Team team : teams) {
            if (team.getBed().part1.equals(location) || team.getBed().part2.equals(location)) return team;
        }
        return null;
    }

    public static Team getRandomTeam() {
        return teams.get(rng.nextInt(teams.size()));
    }
}
