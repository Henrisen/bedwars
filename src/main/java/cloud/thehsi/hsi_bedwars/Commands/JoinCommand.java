package cloud.thehsi.hsi_bedwars.Commands;

import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.Team;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.TeamController;
import cloud.thehsi.hsi_bedwars.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class JoinCommand extends AdvancedCommand {
    String[] colors = {"blue", "green", "orange", "pink", "purple", "red", "white", "yellow"};
    public JoinCommand(Plugin plugin) {
        super(plugin);
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
                Team team = TeamController.getTeamByColor(strings[0].toLowerCase());
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
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        return strings.length == 1 ? List.of(colors) : (strings.length == 2 ? Bukkit.getOnlinePlayers().stream().map(Player::getName).toList() : new ArrayList<>());
    }
}
