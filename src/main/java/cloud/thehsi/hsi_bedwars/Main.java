package cloud.thehsi.hsi_bedwars;

import cloud.thehsi.hsi_bedwars.BedwarsElements.JsonParser;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Spawners.SpawnerController;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.TeamController;
import cloud.thehsi.hsi_bedwars.Commands.PluginCommands;
import cloud.thehsi.hsi_bedwars.Items.PluginItems;
import cloud.thehsi.hsi_bedwars.Listeners.PluginListeners;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    BuildTracker tracker = new BuildTracker();
    public PluginItems pluginItems;
    @Override
    public void onEnable() {
        for (World world : Bukkit.getWorlds()) {
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
            world.setGameRule(GameRule.KEEP_INVENTORY, true);
            world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
        }
        pluginItems = new PluginItems(this);
        new PluginListeners(this, tracker);
        new PluginCommands(this, this, tracker, pluginItems);
        SpawnerController.init(this);
        TeamController.init(this);
        JsonParser.loadFile();
        JsonParser.loadSpawners();
        JsonParser.loadTeams(this);
    }

    @Override
    public void onDisable() {
        tracker.undoChanges();
        SpawnerController.remove();
        TeamController.remove();
        for (Entity entity : Bukkit.getWorlds().getFirst().getEntities()) {
            if (!(entity instanceof Player)) entity.remove();
        }
    }

    static ChatColor[] colors = {ChatColor.RED, ChatColor.GOLD, ChatColor.YELLOW, ChatColor.GREEN, ChatColor.AQUA, ChatColor.DARK_PURPLE, ChatColor.LIGHT_PURPLE, ChatColor.WHITE};

    public static String makeDisplay() {
        String r = ChatColor.DARK_GRAY + "[" + colors[0] + "B" + colors[1] + "E" + colors[2] + "D" + colors[3] + "W" + colors[4] + "A" + colors[5] + "R" + colors[6] + "S" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + " ";
        ChatColor zero = colors[0];
        colors[0] = colors[1];
        colors[1] = colors[2];
        colors[2] = colors[3];
        colors[3] = colors[4];
        colors[4] = colors[5];
        colors[5] = colors[6];
        colors[6] = colors[7];
        colors[7] = zero;
        return r;
    }
}
