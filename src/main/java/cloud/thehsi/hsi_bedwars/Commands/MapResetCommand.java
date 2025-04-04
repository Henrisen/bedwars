package cloud.thehsi.hsi_bedwars.Commands;

import cloud.thehsi.hsi_bedwars.BuildTracker;
import cloud.thehsi.hsi_bedwars.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapResetCommand extends AdvancedCommand {
    BuildTracker tracker;
    public MapResetCommand(Plugin plugin, BuildTracker tracker) {
        super(plugin);
        this.tracker = tracker;
    }

    private void clear_history(String[] args, CommandSender commandSender) {
        if (Objects.equals(args[1], "*")) {
            commandSender.sendMessage(Main.makeDisplay() + "Removing All Player Activity History...");
            tracker.clearChanges();
            return;
        }
        int toRemove;
        try {
            toRemove = Integer.parseInt(args[1]);
        }catch (NumberFormatException ignored) {
            commandSender.sendMessage(ChatColor.RED + "/map_reset clear_history " + args[1] + " << Must be Integer");
            return;
        }
        if (toRemove < 1) {
            commandSender.sendMessage(ChatColor.RED + "/map_reset clear_history " + args[1] + " << Must be Positive and not 0");
        }
        if (toRemove == 1)
            commandSender.sendMessage(Main.makeDisplay() + "Removing " + args[1] + " Latest Entries From Player Activity History...");
        else
            commandSender.sendMessage(Main.makeDisplay() + "Removing " + args[1] + " Latest Entries From Player Activity History...");
        tracker.clearChangesLimited(toRemove);
    }

    private void undo(String[] args, CommandSender commandSender) {
        if (Objects.equals(args[1], "*")) {
            commandSender.sendMessage(Main.makeDisplay() + "Undoing All Changes...");
            tracker.undoChanges();
            return;
        }
        int toRemove;
        try {
            toRemove = Integer.parseInt(args[1]);
        }catch (NumberFormatException ignored) {
            commandSender.sendMessage(ChatColor.RED + "/map_reset clear_history " + args[1] + " << Must be Integer");
            return;
        }
        if (toRemove < 1) {
            commandSender.sendMessage(ChatColor.RED + "/map_reset clear_history " + args[1] + " << Must be Positive and not 0");
        }
        if (toRemove == 1)
            commandSender.sendMessage(Main.makeDisplay() + "Undoing " + args[1] + " Change...");
        else
            commandSender.sendMessage(Main.makeDisplay() + "Undoing " + args[1] + " Changes...");
        tracker.undoChangesLimited(toRemove);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        switch (strings.length) {
            case 0:
                commandSender.sendMessage(ChatColor.RED + "Usage: /map_reset [pause|resume|clear_history|undo]");
                break;
            case 1:
                switch (strings[0]) {
                    case "pause":
                        commandSender.sendMessage(Main.makeDisplay() + "Paused Map Protection and Undo / Reset Logging");
                        tracker.pause();
                        break;
                    case "resume":
                        commandSender.sendMessage(Main.makeDisplay() + "Resumed Map Protection and Undo / Reset Logging");
                        tracker.resume();
                        break;
                    case "clear_history":
                        commandSender.sendMessage(ChatColor.RED + "Usage: /map_reset clear_history [1..n|*]");
                        break;
                    case "undo":
                        commandSender.sendMessage(ChatColor.RED + "Usage: /map_reset undo [1..n|*]");
                        break;
                    default:
                        commandSender.sendMessage(ChatColor.RED + "Usage: /map_reset [pause|resume|clear_history]");
                        break;
                }
                break;
            case 2:
                switch (strings[0]) {
                    case "clear_history":
                        clear_history(strings, commandSender);
                        break;
                    case "undo":
                        undo(strings, commandSender);
                        break;
                    default:
                        commandSender.sendMessage(ChatColor.RED + "Usage: /map_reset [pause|resume|clear_history|undo]");
                        break;
                }
                break;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        List<String> suggestions = new ArrayList<>();
        switch (strings.length) {
            case 1:
                suggestions.add("clear_history");
                suggestions.add("undo");
                suggestions.add("pause");
                suggestions.add("resume");
                break;
            case 2:
                switch (strings[0]) {
                    case "clear_history":
                        for (int i = 1;i < 100; i++) {
                            suggestions.add(""+i);
                        }
                        suggestions.add("*");
                    case "undo":
                        for (int i = 1;i < 100; i++) {
                            suggestions.add(""+i);
                        }
                        suggestions.add("*");
                }
        }
        return suggestions;
    }
}
