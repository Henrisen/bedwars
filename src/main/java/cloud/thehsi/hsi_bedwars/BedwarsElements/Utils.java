package cloud.thehsi.hsi_bedwars.BedwarsElements;

import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.Team;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.TeamController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
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
            player.playSound(player, Sound.ENTITY_ENDER_DRAGON_AMBIENT, 1, 1);
            Team destroyerTeam = TeamController.getPlayerTeam(destroyer);
            ChatColor destroyerColor = destroyerTeam==null?ChatColor.GRAY:destroyerTeam.getChatColor();
            if (team.players().contains(player)) {
                player.sendTitle(ChatColor.RED + "BED DESTROYED!", "You will no longer respawn!", 5, 80, 15);
                player.sendMessage(ChatColor.BOLD + "BED DESTRUCTION > " + ChatColor.RESET + ChatColor.GRAY + "Your Bed was destroyed by\n " + destroyerColor + destroyer.getDisplayName() + ChatColor.RESET + ChatColor.GRAY + "!");
            }else {
                String color = team.getColor().toLowerCase();
                color = color.substring(0,1).toUpperCase() + color.substring(1);
                player.sendMessage(ChatColor.BOLD + "BED DESTRUCTION > " + ChatColor.RESET + team.getChatColor() + color + " Bed" + ChatColor.RESET + ChatColor.GRAY + " was destroyed by\n " + destroyerColor + destroyer.getDisplayName() + ChatColor.RESET + ChatColor.GRAY + "!");
            }
        }
    }

    public static void broadcastBedReplacement(Team team, Player restorer) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player, Sound.BLOCK_END_PORTAL_FRAME_FILL, 1, 1);
            if (team.players().contains(player)) {
                player.sendTitle(ChatColor.GREEN + "BED RESTORED!", "You will respawn once again!", 5, 40, 15);
                player.sendMessage(ChatColor.BOLD + "BED DESTRUCTION > " + ChatColor.RESET + ChatColor.GRAY + "Your Bed was restored by\n " + team.getChatColor() + restorer.getDisplayName() + ChatColor.RESET + ChatColor.GRAY + "!");
            }else {
                String color = team.getColor().toLowerCase();
                color = color.substring(0,1).toUpperCase() + color.substring(1);
                player.sendMessage(ChatColor.BOLD + "BED RESTORATION > " + ChatColor.RESET + team.getChatColor() + color + " Bed" + ChatColor.RESET + ChatColor.GRAY + " was restored by\n " + team.getChatColor() + restorer.getDisplayName() + ChatColor.RESET + ChatColor.GRAY + "!");
            }
        }
    }
}
