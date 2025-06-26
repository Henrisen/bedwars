package cloud.thehsi.hsi_bedwars.Commands;

import cloud.thehsi.hsi_bedwars.Items.BaseItem;
import cloud.thehsi.hsi_bedwars.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cloud.thehsi.hsi_bedwars.Main.pluginItems;

public class ICommand extends AdvancedCommand {
    public ICommand(Plugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        BaseItem givenItem;
        ItemStack stack;
        switch (strings.length) {
            case 2:
                givenItem = null;
                for (BaseItem item : pluginItems.getItems()) {
                    if (Objects.equals(strings[0], item.getId())) {
                        givenItem = item;
                        break;
                    }
                }
                if (givenItem == null) {
                    commandSender.sendMessage(Main.makeDisplay() + ChatColor.RED + "Unknown Item: " + strings[0]);
                    return false;
                }
                stack = givenItem.getDefaultStack((Player)commandSender);
                int amount;
                try {
                    amount = Integer.parseInt(strings[1]);
                }catch (NumberFormatException ex) {
                    commandSender.sendMessage(Main.makeDisplay() + ChatColor.RED + "Invalid Number: " + strings[1]);
                    return false;
                }
                if (amount<1) {
                    commandSender.sendMessage(Main.makeDisplay() + ChatColor.RED + "Invalid Number: " + strings[1]);
                    return false;
                }
                stack.setAmount(amount);
                ((Player)commandSender).getInventory().addItem(stack);
                commandSender.sendMessage(Main.makeDisplay() + "Given " + ((Player)commandSender).getDisplayName() + " " + strings[1] + " " + givenItem.getName());
                break;
            case 1:
                givenItem = null;
                for (BaseItem item : pluginItems.getItems()) {
                    if (Objects.equals(strings[0], item.getId())) {
                        givenItem = item;
                        break;
                    }
                }
                if (givenItem == null) {
                    commandSender.sendMessage(Main.makeDisplay() + ChatColor.RED + "Unknown Item: " + strings[0]);
                    return false;
                }
                stack = givenItem.getDefaultStack();
                ((Player)commandSender).getInventory().addItem(stack);
                commandSender.sendMessage(Main.makeDisplay() + "Given " + ((Player)commandSender).getDisplayName() + " 1 " + givenItem.getName());
                break;
            default:
                commandSender.sendMessage(Main.makeDisplay() + ChatColor.RED + "/i <item> [<count>]");
        }
        return false;
    }

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
