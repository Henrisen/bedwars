package cloud.thehsi.hsi_bedwars.Commands;

import cloud.thehsi.hsi_bedwars.BedwarsElements.JsonParser;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Spawners.*;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Spawners.Custom.DiamondSpawner;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Spawners.Custom.EmeraldSpawner;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Spawners.Custom.GoldSpawner;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Spawners.Custom.IronSpawner;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.Bed;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.Team;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.TeamController;
import cloud.thehsi.hsi_bedwars.BuildTracker;
import cloud.thehsi.hsi_bedwars.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BedwarsAdminCommand extends AdvancedCommand {
    BuildTracker tracker;
    String[] colors = {"blue", "green", "orange", "pink", "purple", "red", "white", "yellow"};
    public BedwarsAdminCommand(Plugin plugin, BuildTracker tracker) {
        super(plugin);
        this.tracker = tracker;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        if (strings.length != 5) {
            commandSender.sendMessage(ChatColor.RED + "/bedwars_admin <add_bed|add_spawner|add_spawnpoint|add_trader> <color|material|world> <x> <y> <z>");
            return false;
        }
        String x = strings[2];
        String y = strings[3];
        String z = strings[4];
        Location location;
        try {
            location = new Location(((Player)commandSender).getWorld(), Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(z));
        }catch (NumberFormatException err) {
            commandSender.sendMessage(ChatColor.RED + err.getMessage());
            return false;
        }
        Team team;
        switch (strings[0]) {
            case "add_spawner":
                location.add(.5,.5,.5);
                switch (strings[1].toLowerCase()) {
                    case "iron" -> {
                        SpawnerController.addAndRegisterSpawner(new IronSpawner(location));
                        commandSender.sendMessage(Main.makeDisplay() + "Created new " + strings[1] + " Spawner");
                    }
                    case "gold" -> {
                        SpawnerController.addAndRegisterSpawner(new GoldSpawner(location));
                        commandSender.sendMessage(Main.makeDisplay() + "Created new " + strings[1] + " Spawner");
                    }
                    case "diamond" -> {
                        SpawnerController.addAndRegisterSpawner(new DiamondSpawner(location));
                        commandSender.sendMessage(Main.makeDisplay() + "Created new " + strings[1] + " Spawner");
                    }
                    case "emerald" -> {
                        SpawnerController.addAndRegisterSpawner(new EmeraldSpawner(location));
                        commandSender.sendMessage(Main.makeDisplay() + "Created new " + strings[1] + " Spawner");
                    }
                    default ->
                        commandSender.sendMessage(ChatColor.RED + "Unknown Spawner Type: '" + strings[1].toLowerCase() + "'");
                }
                break;
            case "add_bed":
                if (!Arrays.stream(Bed.Beds).toList().contains(location.getBlock().getType())) {
                    commandSender.sendMessage(ChatColor.RED + "That Position is not a Bed");
                    return false;
                }
                Location bed1 = null;
                Location bed2 = null;
                for (BlockFace face : new BlockFace[]{BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST}) {
                    Block block = location.getBlock().getRelative(face);
                    if (Arrays.stream(Bed.Beds).toList().contains(block.getType())) {
                        if (block.getBlockData() instanceof org.bukkit.block.data.type.Bed bed) {
                            if (bed.getPart() == org.bukkit.block.data.type.Bed.Part.FOOT) {
                                bed1 = block.getLocation();
                                bed2 = location;
                            }else {
                                bed1 = location;
                                bed2 = block.getLocation();
                            }
                        }
                        break;
                    }
                }
                if (bed1 == null) {
                    commandSender.sendMessage(ChatColor.RED + "Could not find second half of Bed");
                    return false;
                }
                team = TeamController.getTeamByColor(strings[1].toLowerCase());
                if (team == null) {
                    commandSender.sendMessage(ChatColor.YELLOW + "[WARNING] There was no " + strings[1] + " team.");
                    commandSender.sendMessage(ChatColor.YELLOW + "[WARNING] It was automatically created.");
                    commandSender.sendMessage(ChatColor.YELLOW + "[WARNING] You may want to set a Spawnpoint and Trader.");
                    team = new Team(strings[1], bed1, bed2, new Location(bed1.getWorld(), 0,0,0), new Location(location.getWorld(), 0,1,0), plugin);
                    TeamController.addAndRegisterTeam(team);
                }else {
                    team.getBed().getBedLocation(false).getBlock().setType(Material.AIR, false);
                    team.getBed().getBedLocation(true).getBlock().setType(Material.AIR, false);
                    team.getBed().update(bed1, bed2);
                    TeamController.updateTeam(team);
                }
                commandSender.sendMessage(Main.makeDisplay() + "Set Bed for Team " + strings[1]);
                break;
            case "add_spawnpoint":
                location.add(.5,.5,.5);
                team = TeamController.getTeamByColor(strings[1].toLowerCase());
                if (team == null) {
                    commandSender.sendMessage(ChatColor.YELLOW + "[WARNING] There was no " + strings[1] + " team.");
                    commandSender.sendMessage(ChatColor.YELLOW + "[WARNING] It was automatically created.");
                    commandSender.sendMessage(ChatColor.YELLOW + "[WARNING] You may want to set a Bed and Trader.");
                    team = new Team(
                            strings[1],
                            new Location(location.getWorld(),0,0,0),
                            new Location(location.getWorld(), 1,0,0),
                            location,
                            new Location(location.getWorld(), 0,1,0),
                            plugin
                    );
                    TeamController.addAndRegisterTeam(team);
                }else {
                    team.getSpawnpoint().update(location);
                    TeamController.updateTeam(team);
                }
                commandSender.sendMessage(Main.makeDisplay() + "Set Spawnpoint for Team " + strings[1]);
                break;
            case "add_trader":
                team = TeamController.getTeamByColor(strings[1].toLowerCase());
                if (team == null) {
                    commandSender.sendMessage(ChatColor.YELLOW + "[WARNING] There was no " + strings[1] + " team.");
                    commandSender.sendMessage(ChatColor.YELLOW + "[WARNING] It was automatically created.");
                    commandSender.sendMessage(ChatColor.YELLOW + "[WARNING] You may want to set a Bed and Spawnpoint.");
                    team = new Team(
                            strings[1],
                            new Location(location.getWorld(),0,0,0),
                            new Location(location.getWorld(), 1,0,0),
                            new Location(location.getWorld(), 0,1,0),
                            location,
                            plugin
                    );
                    TeamController.addAndRegisterTeam(team);
                }else {
                    team.getTrader().setSpawnLocation(location);
                    TeamController.updateTeam(team);
                }
                commandSender.sendMessage(Main.makeDisplay() + "Set Trader for Team " + strings[1]);
                break;
            case "set_center":
                location.add(.5,.5,.5);
                JsonParser.setCenter(location);
                commandSender.sendMessage(Main.makeDisplay() + "Set World Center");
                break;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        List<String> suggestions = new ArrayList<>();
        switch (strings.length) {
            case 1:
                suggestions.add("add_bed");
                suggestions.add("add_spawner");
                suggestions.add("add_spawnpoint");
                suggestions.add("set_center");
                suggestions.add("set_trader");
                break;
            case 2:
                switch (strings[0]) {
                    case "add_bed","add_spawnpoint","add_trader":
                        suggestions.addAll(List.of(colors));
                        break;
                    case "add_spawner":
                        suggestions.add("iron");
                        suggestions.add("gold");
                        suggestions.add("diamond");
                        suggestions.add("emerald");
                        break;
                    case "set_center":
                        suggestions.add("world");
                        break;
                }
                break;
            case 3,4,5:
                switch (strings[0]) {
                    case "add_spawnpoint","add_spawner","add_bed","set_center","add_trader":
                        Player p = (Player)commandSender;
                        RayTraceResult result = p.rayTraceBlocks(5);
                        if (strings.length == 3) {
                            if (result == null) suggestions.add(p.getLocation().getBlockX()+" "+p.getLocation().getBlockY()+" "+p.getLocation().getBlockZ());
                            else {Block b=result.getHitBlock();assert b!=null;suggestions.add(b.getX()+" "+b.getY()+" "+b.getZ());}
                        }else if (strings.length == 4) {
                            if (result == null) suggestions.add(p.getLocation().getBlockY()+" "+p.getLocation().getBlockZ());
                            else {Block b=result.getHitBlock();assert b!=null;suggestions.add(b.getY()+" "+b.getZ());}
                        }else {
                            if (result == null) suggestions.add(p.getLocation().getBlockZ()+"");
                            else {Block b=result.getHitBlock();assert b!=null;suggestions.add(b.getZ()+"");}
                        }
                }
                break;
            default:
                suggestions.add("");
                break;
        }
        return suggestions;
    }
}
