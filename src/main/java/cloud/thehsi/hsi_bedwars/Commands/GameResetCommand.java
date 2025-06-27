package cloud.thehsi.hsi_bedwars.Commands;

import cloud.thehsi.hsi_bedwars.BedwarsElements.JsonParser;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Spawners.SpawnerController;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.TeamController;
import cloud.thehsi.hsi_bedwars.BuildTracker;
import cloud.thehsi.hsi_bedwars.Items.BaseItem;
import cloud.thehsi.hsi_bedwars.Items.PluginItems;
import cloud.thehsi.hsi_bedwars.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static cloud.thehsi.hsi_bedwars.Main.pluginItems;

public class GameResetCommand extends AdvancedCommand {
    BuildTracker tracker;
    public GameResetCommand(Plugin plugin, BuildTracker tracker) {
        super(plugin);
        this.tracker = tracker;
    }

    /**
     * Handles the execution of the /game_reset command, resetting the game state if no arguments are provided.
     *
     * Sends a usage message if arguments are present. Otherwise, performs a full game reset and notifies the sender.
     *
     * @return true if incorrect usage; false if the game was reset successfully.
     */
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        if (strings.length != 0) {
            commandSender.sendMessage(Main.makeDisplay() + ChatColor.RED + "/game_reset");
            return true;
        }

        reset(plugin, tracker);
        commandSender.sendMessage(Main.makeDisplay() + "Game has been Reset");
        return false;
    }

    /**
     * Resets the game environment to its initial state.
     *
     * This method undoes all tracked build changes, removes spawners and teams, clears plugin items,
     * removes all non-player entities from the first loaded world, and sets specific game rules across all worlds.
     * It then reinitializes plugin items, spawner and team controllers, reloads configuration and data files,
     * and sends a confirmation message to the server console.
     */
    public static void reset(Plugin plugin, BuildTracker tracker) {
        tracker.undoChanges();
        SpawnerController.remove();
        TeamController.remove();
        pluginItems.remove();

        for (Entity entity : Bukkit.getWorlds().getFirst().getEntities()) {
            if (!(entity instanceof Player)) entity.remove();
        }

        for (World world : Bukkit.getWorlds()) {
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
            world.setGameRule(GameRule.KEEP_INVENTORY, true);
            world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
        }
        pluginItems = new PluginItems(plugin, tracker);
        SpawnerController.init(plugin);
        TeamController.init(plugin);
        JsonParser.loadFile();
        JsonParser.loadSpawners();
        JsonParser.loadTeams(plugin);

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Game was Reset!");
    }

    /**
     * Provides tab completion suggestions for the game reset command.
     *
     * Suggests item IDs when the first argument is being entered, numbers 1 to 64 for the second argument, and an empty suggestion otherwise.
     *
     * @return a list of suggested completions based on the current argument position
     */
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        List<String> suggest = new ArrayList<>();
        switch (strings.length) {
            case 1:
                for (BaseItem item : pluginItems.getItems()) {
                    suggest.add(item.getId());
                }
                break;
            case 2:
                for (int i = 1; i <= 64; i++) {
                    suggest.add(i+"");
                }
                break;
            default:
                suggest.add("");
        }
        return suggest;
    }
}
