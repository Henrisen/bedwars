package cloud.thehsi.hsi_bedwars.BedwarsElements.Teams;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class Bed {
    Location part1;
    Location part2;
    Team team;
    public static final Material[] Beds = {Material.RED_BED,Material.ORANGE_BED,Material.YELLOW_BED,Material.LIME_BED,Material.LIGHT_BLUE_BED,Material.PURPLE_BED,Material.PINK_BED,Material.WHITE_BED};

    public Bed(Location part1, Location part2, Team team) {
        this.part1 = part1;
        this.part2 = part2;
        this.team = team;
    }

    public Location getBedLocation(boolean second) {
        return second?part2:part1;
    }

    public boolean isDestroyed() {
        if (part1.getBlock().getType().isAir() || part2.getBlock().getType().isAir()) {
            part1.getBlock().setType(Material.AIR);
            part2.getBlock().setType(Material.AIR);
            return true;
        }
        return false;
    }

    public void update(Location bed1, Location bed2) {
        this.part1 = bed1;
        this.part2 = bed2;
    }

    public void place() {
        BlockFace facing = getFacing();
        assert facing!=null;
        part1.getBlock().setType(getBedType(), false);
        org.bukkit.block.data.type.Bed bed1_bed = (org.bukkit.block.data.type.Bed) part1.getBlock().getBlockData();
        bed1_bed.setPart(org.bukkit.block.data.type.Bed.Part.FOOT);
        bed1_bed.setFacing(facing);
        part1.getBlock().setBlockData(bed1_bed, false);

        part2.getBlock().setType(getBedType(), false);
        org.bukkit.block.data.type.Bed bed2_bed = (org.bukkit.block.data.type.Bed) part2.getBlock().getBlockData();
        bed2_bed.setPart(org.bukkit.block.data.type.Bed.Part.HEAD);
        bed2_bed.setFacing(facing);
        part2.getBlock().setBlockData(bed2_bed, true);
    }

    public void remove() {
        part1.getBlock().setType(Material.AIR, false);
        part2.getBlock().setType(Material.AIR, false);
    }

    private BlockFace getFacing() {
        Vector difference = part1.clone().subtract(part2).toVector();
        if (difference.getX() == -1) return BlockFace.EAST;
        if (difference.getX() == 1) return BlockFace.WEST;
        if (difference.getZ() == -1) return BlockFace.SOUTH;
        if (difference.getZ() == 1) return BlockFace.NORTH;
        return null;
    }

    private Material getBedType() {
        return switch (team.getColor()) {
            case "red" -> Material.RED_BED;
            case "orange" -> Material.ORANGE_BED;
            case "yellow" -> Material.YELLOW_BED;
            case "green" -> Material.LIME_BED;
            case "blue" -> Material.LIGHT_BLUE_BED;
            case "purple" -> Material.PURPLE_BED;
            case "pink" -> Material.PINK_BED;
            case "white" -> Material.WHITE_BED;
            default -> throw new IllegalStateException("Unexpected value: " + team.getColor());
        };
    }
}
