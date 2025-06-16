package cloud.thehsi.hsi_bedwars.BedwarsElements;

import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.Team;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.TeamController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Utils {
    @SuppressWarnings("unused")
    public static void message(Team team, String message) {
        for (Player player : team.players()) {
            player.sendMessage("[" + team.getColor() + "System" + ChatColor.RESET + "]: " + message);
        }
    }

    @SuppressWarnings("unused")
    public static void message(Player player, String message) {
        ChatColor color = ChatColor.WHITE;
        Team team = TeamController.getPlayerTeam(player);
        if (team != null) {
            color = team.getChatColor();
        }
        player.sendMessage("[" + color + "System" + ChatColor.RESET + "]: " + message);
    }

    public static void broadcastBedDestruct(Team team, Player destroyer) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (team.players().contains(player)) {
                Team destroyerTeam = TeamController.getPlayerTeam(destroyer);
                ChatColor destroyerColor = destroyerTeam==null?ChatColor.GRAY:destroyerTeam.getChatColor();
                player.sendTitle(ChatColor.RED + "BED DESTROYED!", "You will no longer respawn!", 5, 80, 15);
                player.sendMessage(ChatColor.BOLD + "BED DESTRUCTION > " + ChatColor.RESET + ChatColor.GRAY + "Your Bed was destroyed by\n " + destroyerColor + destroyer.getDisplayName() + ChatColor.RESET + ChatColor.GRAY + "!");
            }else {
                Team destroyerTeam = TeamController.getPlayerTeam(destroyer);
                ChatColor destroyerColor = destroyerTeam==null?ChatColor.GRAY:destroyerTeam.getChatColor();
                String color = team.getColor().toLowerCase();
                color = color.substring(0,1).toUpperCase() + color.substring(1);
                player.sendMessage(ChatColor.BOLD + "BED DESTRUCTION > " + ChatColor.RESET + team.getChatColor() + color + " Bed" + ChatColor.RESET + ChatColor.GRAY + " was destroyed by " + destroyerColor + destroyer.getDisplayName() + ChatColor.RESET + ChatColor.GRAY + "!");
            }
        }
    }
}
