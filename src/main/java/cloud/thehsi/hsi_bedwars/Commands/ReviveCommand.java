package cloud.thehsi.hsi_bedwars.Commands;

import cloud.thehsi.hsi_bedwars.BedwarsElements.JsonParser;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.Team;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.TeamController;
import cloud.thehsi.hsi_bedwars.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.atan2;
import static java.lang.Math.toDegrees;

public class ReviveCommand extends AdvancedCommand {
    String[] colors = {"blue", "green", "orange", "pink", "purple", "red", "white", "yellow"};
    public ReviveCommand(Plugin plugin) {
        super(plugin);
    }

    private Location applyRotateToCenter(Location spawnpoint) {
        Location center = JsonParser.getCenter();
        double dx = center.getX() - spawnpoint.getX();
        double dz = center.getZ() - spawnpoint.getZ();

        float yaw = (float) toDegrees(atan2(-dx, dz));

        yaw = Math.round(yaw / 90f) * 90f;

        spawnpoint.setYaw(yaw);
        spawnpoint.setPitch(0);
        return spawnpoint;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        Team team;
        Player p;
        switch (strings.length) {
            case 2:
                team = TeamController.getTeamByColor(strings[1].toLowerCase());
                if (team == null) {
                    commandSender.sendMessage(Main.makeDisplay() + ChatColor.RED + "Unknown Team: " + strings[0]);
                    return false;
                }
                p = Bukkit.getPlayer(strings[0]);
                if (p == null) {
                    commandSender.sendMessage(Main.makeDisplay() + ChatColor.RED + "Unknown Player: " + strings[1]);
                    return false;
                }
                team.addPlayer(p);
                p.setGameMode(GameMode.SURVIVAL);
                p.teleport(applyRotateToCenter(team.getSpawnpoint().getSpawnpointLocation().clone()));
                commandSender.sendMessage(Main.makeDisplay() + "Revived: " + team.getChatColor() + p.getDisplayName());
                break;
            case 3:
                team = TeamController.getTeamByColor(strings[1].toLowerCase());
                if (team == null) {
                    commandSender.sendMessage(Main.makeDisplay() + ChatColor.RED + "Unknown Team: " + strings[0]);
                    return false;
                }
                p = Bukkit.getPlayer(strings[0]);
                if (p == null) {
                    commandSender.sendMessage(Main.makeDisplay() + ChatColor.RED + "Unknown Player: " + strings[1]);
                    return false;
                }
                if (Objects.equals(strings[2], "true")) {
                    team.getBed().place();
                }else if (!Objects.equals(strings[2], "false")) {
                    commandSender.sendMessage(Main.makeDisplay() + ChatColor.RED + "Unknown Boolean: " + strings[2]);
                    return false;
                }
                team.addPlayer(p);
                p.setGameMode(GameMode.SURVIVAL);
                p.teleport(applyRotateToCenter(team.getSpawnpoint().getSpawnpointLocation().clone()));
                commandSender.sendMessage(Main.makeDisplay() + "Revived: " + team.getChatColor() + p.getDisplayName());
                break;
            default:
                commandSender.sendMessage(Main.makeDisplay() + ChatColor.RED + "/revive <player> <team> [replaceBed]");
                break;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        return strings.length == 3 ? List.of("true", "false") : (strings.length == 2 ? List.of(colors) : (strings.length == 1 ? Bukkit.getOnlinePlayers().stream().map(Player::getName).toList() : new ArrayList<>()));
    }
}
