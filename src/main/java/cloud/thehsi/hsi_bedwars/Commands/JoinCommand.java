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

public class JoinCommand extends AdvancedCommand {
    String[] colors = {"blue", "green", "orange", "pink", "purple", "red", "white", "yellow"};
    public JoinCommand(Plugin plugin) {
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
        switch (strings.length) {
            default -> commandSender.sendMessage(Main.makeDisplay() + ChatColor.RED + "/join <team> [player]");
            case 1 -> {
                Team team = TeamController.getTeamByColor(strings[0].toLowerCase());
                if (team == null) {
                    commandSender.sendMessage(Main.makeDisplay() + ChatColor.RED + "Unknown Team: " + strings[0]);
                    return false;
                }
                Team in = TeamController.getPlayerTeam((Player) commandSender);
                if (in != null) in.removePlayer((Player) commandSender);
                team.addPlayer((Player) commandSender);
                commandSender.sendMessage(Main.makeDisplay() + "Joined Team: " + team.getChatColor() + team.getColor());
            }
            case 2 -> {
                Team team;
                if (Objects.equals(strings[0], "*"))
                    if (Objects.equals(strings[1], "*")) {
                        List<Team> used = new ArrayList<>();
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            team = TeamController.getRandomTeam();
                            if (used.contains(team)) {
                                if (used.size() == TeamController.teams.size()) {
                                    Bukkit.broadcastMessage("[JoinCommand.java][onCommand] Too many Players"); // TODO: Add many Player Support
                                    return false;
                                }
                                while (used.contains(team)) {
                                    team = TeamController.getRandomTeam();
                                }
                            }
                            team.addPlayer(player);
                            player.setGameMode(GameMode.SURVIVAL);
                            player.teleport(applyRotateToCenter(team.getSpawnpoint().getSpawnpointLocation().clone()));
                            used.add(team);
                        }
                        commandSender.sendMessage(Main.makeDisplay() + "Shuffle Finished!");
                    } else {
                        commandSender.sendMessage(Main.makeDisplay() + ChatColor.RED + "If using Full Shuffle (*), Full Shuffle (*) must be Selected for Team and Player");
                        return false;
                    }
                else {
                    team = TeamController.getTeamByColor(strings[0].toLowerCase());
                    if (team == null) {
                        commandSender.sendMessage(Main.makeDisplay() + ChatColor.RED + "Unknown Team: " + strings[0]);
                        return false;
                    }
                    Player p = Bukkit.getPlayer(strings[1]);
                    if (p == null) {
                        commandSender.sendMessage(Main.makeDisplay() + ChatColor.RED + "Unknown Player: " + strings[1]);
                        return false;
                    }
                    team.addPlayer(p);
                    commandSender.sendMessage(Main.makeDisplay() + "Joined Team: " + team.getChatColor() + team.getColor());
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        List<String> _colors = new ArrayList<>(List.of(colors)); _colors.add("*");
        List<String> _players = new ArrayList<>();
        if (strings.length>0)
            if (Objects.equals(strings[0], "*"))
                _players.add("*");
            else
                _players.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
        return strings.length == 1 ? _colors : (strings.length == 2 ? _players : new ArrayList<>());
    }
}
