package cloud.thehsi.hsi_bedwars;

import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.Bed;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.Team;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.TeamController;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BuildTracker {
    private final Map<CoordinateEntry, Material> changes = new HashMap<>();
    private boolean paused = false;
    private static class CoordinateEntry {
        public int x;
        public int y;
        public int z;
        public int entry;
        public World world;
        public boolean natural;

        public CoordinateEntry(int x, int y, int z, int entry, World world, boolean natural) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.entry = entry;
            this.world = world;
            this.natural = natural;
        }

        public static int getNextEntryId(Map <CoordinateEntry, Material> changes) {
            int i = 0;
            for (CoordinateEntry coordinateEntry : changes.keySet()) {
                if (i<coordinateEntry.entry) {
                    i = coordinateEntry.entry;
                }
            }
            return i+1;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof CoordinateEntry other)
                return this.x == other.x && this.y == other.y && this.z == other.z && this.world.getName().equals(other.world.getName());
            return false;
        }

        @Override
        public int hashCode() {
            return natural?0:1 + (this.y*2) + (this.x * 400 * 2) + (this.z * 60_000_000 * 2) + (this.world.getName().hashCode() * 60_000_000 * 60_000_000 * 2);
        }

        @Override
        public String toString() {
            return "X: " + x + " Y: " + y + " Z: " + z + " World: " + world;
        }

        public Block getBlock() {
            return world.getBlockAt(x,y,z);
        }
    }

    public void registerChange(Block block, Material original, boolean natural) {
        CoordinateEntry coordinate = new CoordinateEntry(block.getX(), block.getY(), block.getZ(), CoordinateEntry.getNextEntryId(changes), block.getWorld(), natural);
        if (this.changes.containsKey(coordinate))
            return;
        if (paused)
            return;

        this.changes.put(coordinate, original);
    }

    public boolean canBreak(Block block) {
        if (Arrays.stream(Bed.Beds).toList().contains(block.getType()))
            if (TeamController.getBedOwner(block.getLocation()) != null) {
                return true;
            }
        CoordinateEntry coordinate = new CoordinateEntry(block.getX(), block.getY(), block.getZ(), CoordinateEntry.getNextEntryId(changes), block.getWorld(), false);
        return this.changes.containsKey(coordinate) || paused;
    }

    public boolean canBreakWithEvent(Block block, BlockBreakEvent event) {
        if (Arrays.stream(Bed.Beds).toList().contains(block.getType())) {
            Team team = TeamController.getBedOwner(block.getLocation());
            Team playerTeam = TeamController.getPlayerTeam(event.getPlayer());
            if (team != null && !team.equals(playerTeam)) {
                event.setDropItems(false);
                return true;
            }
        }
        CoordinateEntry coordinate = new CoordinateEntry(block.getX(), block.getY(), block.getZ(), CoordinateEntry.getNextEntryId(changes), block.getWorld(), false);
        return this.changes.containsKey(coordinate) || paused;
    }

    public void undoChanges() {
        for (CoordinateEntry cord : this.changes.keySet()) {
            cord.getBlock().setType(this.changes.get(cord));
        }
    }

    public void undoChangesLimited(int toRemove) {
        int i = CoordinateEntry.getNextEntryId(changes);
        Map<CoordinateEntry, Material> changesCopy = new HashMap<>(changes);
        for (CoordinateEntry cord : this.changes.keySet()) {
            if (cord.entry >= i-toRemove) {
                cord.getBlock().setType(this.changes.get(cord));
                changesCopy.remove(cord);
            }
        }
        changes.clear();
        changes.putAll(changesCopy);
    }

    public void clearChanges() {
        changes.clear();
    }

    public void clearChangesLimited(int toRemove) {
        int i = CoordinateEntry.getNextEntryId(changes);
        Map<CoordinateEntry, Material> changesCopy = new HashMap<>(changes);
        for (CoordinateEntry cord : this.changes.keySet()) {
            if (cord.entry >= i-toRemove) {
                changesCopy.remove(cord);
            }
        }
        changes.clear();
        changes.putAll(changesCopy);
    }

    public void resume() {paused = false;}
    public void pause() {paused = true;}
}
