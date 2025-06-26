package cloud.thehsi.hsi_bedwars.Commands;

import cloud.thehsi.hsi_bedwars.BuildTracker;
import cloud.thehsi.hsi_bedwars.Items.PluginItems;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Function;

public class PluginCommands {
    Plugin plugin;
    PluginManager manager;
    BuildTracker tracker;
    JavaPlugin javaPlugin;
    PluginItems pluginItems;

    private void register() {
        registerCommand("reset", plugin -> new ResetCommand(plugin, tracker));
        registerCommand("map_reset", plugin -> new MapResetCommand(plugin, tracker));
        registerCommand("bedwars_admin", plugin -> new BedwarsAdminCommand(plugin, tracker));
        registerCommand("join", JoinCommand::new);
        registerCommand("revive", ReviveCommand::new);
        registerCommand("i", ICommand::new);
        registerCommand("game_reset", plugin -> new GameResetCommand(plugin, tracker));
    }

    private void registerCommand(String command, Function<Plugin, ? extends AdvancedCommand> commandExecutor) {
        AdvancedCommand cmd = commandExecutor.apply(plugin);
        PluginCommand pluginCommand = javaPlugin.getCommand(command);
        assert pluginCommand != null;
        pluginCommand.setExecutor(cmd);
        pluginCommand.setTabCompleter(cmd);
    }

    public PluginCommands(Plugin plugin, JavaPlugin javaPlugin, BuildTracker tracker, PluginItems pluginItems) {
        this.manager = Bukkit.getPluginManager();
        this.plugin = plugin;
        this.tracker = tracker;
        this.javaPlugin = javaPlugin;
        this.pluginItems = pluginItems;
        register();
    }
}
