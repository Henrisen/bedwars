package cloud.thehsi.hsi_bedwars.Commands;

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class AdvancedCommand implements TabExecutor {
    Plugin plugin;
    public AdvancedCommand(Plugin plugin) {this.plugin = plugin;}

    @Override
    public abstract boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings);

    @Override
    public abstract List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings);
}
