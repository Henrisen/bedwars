package cloud.thehsi.hsi_bedwars.BedwarsElements.Spawners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;

public abstract class TextSpawner extends BaseSpawner{
    TextDisplay textDisplay;
    ItemDisplay itemDisplay;

    public TextSpawner(Material item, Location spawnLocation, int max, String name, int ticksPerSpawn) {
        super(item, spawnLocation, max, name, ticksPerSpawn);
        World world = spawnLocation.getWorld();
        assert world != null;
        textDisplay = world.spawn(spawnLocation.clone().add(0,1,0), TextDisplay.class);
        textDisplay.setText(ChatColor.GOLD + "Next " + name + " in:\n" + (ticksPerSpawn/20) + " Seconds");
        textDisplay.setBillboard(Display.Billboard.CENTER);

        itemDisplay = world.spawn(spawnLocation.clone().add(0,2,0), ItemDisplay.class);
        itemDisplay.setItemStack(new ItemStack(item));
        itemDisplay.setBillboard(Display.Billboard.CENTER);
    }

    @Override
    abstract public String getId();

    public void tick(long time) {
        textDisplay.setText(ChatColor.GOLD + "Next " + itemName + " in:\n" + ((interval/20)-(time%interval)/20) + " Seconds");
        if (time % interval == 0)
            if (countItems(itemLocation, item) < max)
                spawnItem(itemLocation, new ItemStack(item));
    }

    public void remove() {
        textDisplay.remove();
        itemDisplay.remove();
    }
}
