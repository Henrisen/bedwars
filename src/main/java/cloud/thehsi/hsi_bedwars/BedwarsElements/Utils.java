package cloud.thehsi.hsi_bedwars.BedwarsElements;

import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.Team;
import cloud.thehsi.hsi_bedwars.BedwarsElements.Teams.TeamController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.enchantment.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.server.*;
import org.bukkit.event.vehicle.*;
import org.bukkit.event.weather.*;
import org.bukkit.event.world.*;

public class Utils {
    @SuppressWarnings("unused")
    public static void message(Team team, String message) {
        for (Player player : team.players()) {
            player.sendMessage("[" + team.getColor() + "System" + ChatColor.RESET + "]: " + message);
        }
    }

    @SuppressWarnings("unused")
    public static void message(Player player, String message) {
        ChatColor color = ChatColor.WHITE;
        Team team = TeamController.getPlayerTeam(player);
        if (team != null) {
            color = team.getChatColor();
        }
        player.sendMessage("[" + color + "System" + ChatColor.RESET + "]: " + message);
    }

    public static void broadcastBedDestruct(Team team, Player destroyer) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player, Sound.ENTITY_ENDER_DRAGON_AMBIENT, 1, 1);
            Team destroyerTeam = TeamController.getPlayerTeam(destroyer);
            ChatColor destroyerColor = destroyerTeam==null?ChatColor.GRAY:destroyerTeam.getChatColor();
            if (team.players().contains(player)) {
                player.sendTitle(ChatColor.RED + "BED DESTROYED!", "You will no longer respawn!", 5, 80, 15);
                player.sendMessage(ChatColor.BOLD + "BED DESTRUCTION > " + ChatColor.RESET + ChatColor.GRAY + "Your Bed was destroyed by\n " + destroyerColor + destroyer.getDisplayName() + ChatColor.RESET + ChatColor.GRAY + "!");
            }else {
                String color = team.getColor().toLowerCase();
                color = color.substring(0,1).toUpperCase() + color.substring(1);
                player.sendMessage(ChatColor.BOLD + "BED DESTRUCTION > " + ChatColor.RESET + team.getChatColor() + color + " Bed" + ChatColor.RESET + ChatColor.GRAY + " was destroyed by\n " + destroyerColor + destroyer.getDisplayName() + ChatColor.RESET + ChatColor.GRAY + "!");
            }
        }
    }

    public static void broadcastBedReplacement(Team team, Player restorer) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player, Sound.BLOCK_END_PORTAL_FRAME_FILL, 1, 1);
            if (team.players().contains(player)) {
                player.sendTitle(ChatColor.GREEN + "BED RESTORED!", "You will respawn once again!", 5, 40, 15);
                player.sendMessage(ChatColor.BOLD + "BED DESTRUCTION > " + ChatColor.RESET + ChatColor.GRAY + "Your Bed was restored by\n " + team.getChatColor() + restorer.getDisplayName() + ChatColor.RESET + ChatColor.GRAY + "!");
            }else {
                String color = team.getColor().toLowerCase();
                color = color.substring(0,1).toUpperCase() + color.substring(1);
                player.sendMessage(ChatColor.BOLD + "BED RESTORATION > " + ChatColor.RESET + team.getChatColor() + color + " Bed" + ChatColor.RESET + ChatColor.GRAY + " was restored by\n " + team.getChatColor() + restorer.getDisplayName() + ChatColor.RESET + ChatColor.GRAY + "!");
            }
        }
    }

    public static void unregisterEvents(Listener l) {
        //by maycrafter
        BlockBreakEvent.getHandlerList().unregister(l);
        BlockBurnEvent.getHandlerList().unregister(l);
        BlockCanBuildEvent.getHandlerList().unregister(l);
        BlockDamageEvent.getHandlerList().unregister(l);
        BlockDispenseEvent.getHandlerList().unregister(l);
        BlockExpEvent.getHandlerList().unregister(l);
        BlockFadeEvent.getHandlerList().unregister(l);
        BlockFormEvent.getHandlerList().unregister(l);
        BlockFromToEvent.getHandlerList().unregister(l);
        BlockGrowEvent.getHandlerList().unregister(l);
        BlockIgniteEvent.getHandlerList().unregister(l);
        BlockMultiPlaceEvent.getHandlerList().unregister(l);
        BlockPhysicsEvent.getHandlerList().unregister(l);
        BlockPistonExtendEvent.getHandlerList().unregister(l);
        BlockPistonRetractEvent.getHandlerList().unregister(l);
        BlockPlaceEvent.getHandlerList().unregister(l);
        BlockRedstoneEvent.getHandlerList().unregister(l);
        BlockSpreadEvent.getHandlerList().unregister(l);
        EntityBlockFormEvent.getHandlerList().unregister(l);
        LeavesDecayEvent.getHandlerList().unregister(l);
        NotePlayEvent.getHandlerList().unregister(l);
        SignChangeEvent.getHandlerList().unregister(l);

        EnchantItemEvent.getHandlerList().unregister(l);
        PrepareItemEnchantEvent.getHandlerList().unregister(l);

        CreatureSpawnEvent.getHandlerList().unregister(l);
        CreeperPowerEvent.getHandlerList().unregister(l);
        EntityBreakDoorEvent.getHandlerList().unregister(l);
        EntityChangeBlockEvent.getHandlerList().unregister(l);
        EntityCombustByBlockEvent.getHandlerList().unregister(l);
        EntityCombustByEntityEvent.getHandlerList().unregister(l);
        EntityDamageByBlockEvent.getHandlerList().unregister(l);
        EntityDamageByEntityEvent.getHandlerList().unregister(l);
        EntityDeathEvent.getHandlerList().unregister(l);
        EntityExplodeEvent.getHandlerList().unregister(l);
        EntityInteractEvent.getHandlerList().unregister(l);
        EntityPortalEnterEvent.getHandlerList().unregister(l);
        EntityPortalExitEvent.getHandlerList().unregister(l);
        EntityRegainHealthEvent.getHandlerList().unregister(l);
        EntityShootBowEvent.getHandlerList().unregister(l);
        EntityTameEvent.getHandlerList().unregister(l);
        EntityTargetEvent.getHandlerList().unregister(l);
        EntityTargetLivingEntityEvent.getHandlerList().unregister(l);
        EntityTeleportEvent.getHandlerList().unregister(l);
        EntityUnleashEvent.getHandlerList().unregister(l);
        ExpBottleEvent.getHandlerList().unregister(l);
        ExplosionPrimeEvent.getHandlerList().unregister(l);
        FoodLevelChangeEvent.getHandlerList().unregister(l);
        HorseJumpEvent.getHandlerList().unregister(l);
        ItemDespawnEvent.getHandlerList().unregister(l);
        ItemSpawnEvent.getHandlerList().unregister(l);
        PigZapEvent.getHandlerList().unregister(l);
        PlayerDeathEvent.getHandlerList().unregister(l);
        PlayerLeashEntityEvent.getHandlerList().unregister(l);
        PotionSplashEvent.getHandlerList().unregister(l);
        ProjectileHitEvent.getHandlerList().unregister(l);
        ProjectileLaunchEvent.getHandlerList().unregister(l);
        SheepDyeWoolEvent.getHandlerList().unregister(l);
        SheepRegrowWoolEvent.getHandlerList().unregister(l);
        SlimeSplitEvent.getHandlerList().unregister(l);

        HangingBreakByEntityEvent.getHandlerList().unregister(l);
        HangingBreakEvent.getHandlerList().unregister(l);
        HangingPlaceEvent.getHandlerList().unregister(l);

        BrewEvent.getHandlerList().unregister(l);
        CraftItemEvent.getHandlerList().unregister(l);
        FurnaceBurnEvent.getHandlerList().unregister(l);
        FurnaceExtractEvent.getHandlerList().unregister(l);
        FurnaceSmeltEvent.getHandlerList().unregister(l);
        InventoryClickEvent.getHandlerList().unregister(l);
        InventoryCloseEvent.getHandlerList().unregister(l);
        InventoryCreativeEvent.getHandlerList().unregister(l);
        InventoryDragEvent.getHandlerList().unregister(l);
        InventoryInteractEvent.getHandlerList().unregister(l);
        InventoryMoveItemEvent.getHandlerList().unregister(l);
        InventoryOpenEvent.getHandlerList().unregister(l);
        InventoryPickupItemEvent.getHandlerList().unregister(l);
        PrepareItemCraftEvent.getHandlerList().unregister(l);

        AsyncPlayerChatEvent.getHandlerList().unregister(l);
        AsyncPlayerPreLoginEvent.getHandlerList().unregister(l);
        PlayerAnimationEvent.getHandlerList().unregister(l);
        PlayerBedEnterEvent.getHandlerList().unregister(l);
        PlayerBedLeaveEvent.getHandlerList().unregister(l);
        PlayerBucketEmptyEvent.getHandlerList().unregister(l);
        PlayerBucketFillEvent.getHandlerList().unregister(l);
        PlayerChangedWorldEvent.getHandlerList().unregister(l);
        PlayerChannelEvent.getHandlerList().unregister(l);
        PlayerCommandPreprocessEvent.getHandlerList().unregister(l);
        PlayerDropItemEvent.getHandlerList().unregister(l);
        PlayerEditBookEvent.getHandlerList().unregister(l);
        PlayerEggThrowEvent.getHandlerList().unregister(l);
        PlayerExpChangeEvent.getHandlerList().unregister(l);
        PlayerFishEvent.getHandlerList().unregister(l);
        PlayerGameModeChangeEvent.getHandlerList().unregister(l);
        PlayerInteractAtEntityEvent.getHandlerList().unregister(l);
        PlayerInteractEntityEvent.getHandlerList().unregister(l);
        PlayerInteractEvent.getHandlerList().unregister(l);
        PlayerItemBreakEvent.getHandlerList().unregister(l);
        PlayerItemConsumeEvent.getHandlerList().unregister(l);
        PlayerItemHeldEvent.getHandlerList().unregister(l);
        PlayerJoinEvent.getHandlerList().unregister(l);
        PlayerKickEvent.getHandlerList().unregister(l);
        PlayerLevelChangeEvent.getHandlerList().unregister(l);
        PlayerLoginEvent.getHandlerList().unregister(l);
        PlayerMoveEvent.getHandlerList().unregister(l);
        PlayerPortalEvent.getHandlerList().unregister(l);
        PlayerQuitEvent.getHandlerList().unregister(l);
        PlayerRegisterChannelEvent.getHandlerList().unregister(l);
        PlayerRespawnEvent.getHandlerList().unregister(l);
        PlayerShearEntityEvent.getHandlerList().unregister(l);
        PlayerStatisticIncrementEvent.getHandlerList().unregister(l);
        PlayerTeleportEvent.getHandlerList().unregister(l);
        PlayerToggleFlightEvent.getHandlerList().unregister(l);
        PlayerToggleSneakEvent.getHandlerList().unregister(l);
        PlayerToggleSprintEvent.getHandlerList().unregister(l);
        PlayerUnleashEntityEvent.getHandlerList().unregister(l);
        PlayerUnregisterChannelEvent.getHandlerList().unregister(l);
        PlayerVelocityEvent.getHandlerList().unregister(l);

        MapInitializeEvent.getHandlerList().unregister(l);
        PluginDisableEvent.getHandlerList().unregister(l);
        PluginEnableEvent.getHandlerList().unregister(l);
        RemoteServerCommandEvent.getHandlerList().unregister(l);
        ServerCommandEvent.getHandlerList().unregister(l);
        ServerListPingEvent.getHandlerList().unregister(l);
        ServiceRegisterEvent.getHandlerList().unregister(l);
        ServiceUnregisterEvent.getHandlerList().unregister(l);

        VehicleBlockCollisionEvent.getHandlerList().unregister(l);
        VehicleCreateEvent.getHandlerList().unregister(l);
        VehicleDamageEvent.getHandlerList().unregister(l);
        VehicleDestroyEvent.getHandlerList().unregister(l);
        VehicleEnterEvent.getHandlerList().unregister(l);
        VehicleEntityCollisionEvent.getHandlerList().unregister(l);
        VehicleExitEvent.getHandlerList().unregister(l);
        VehicleMoveEvent.getHandlerList().unregister(l);
        VehicleUpdateEvent.getHandlerList().unregister(l);

        LightningStrikeEvent.getHandlerList().unregister(l);
        ThunderChangeEvent.getHandlerList().unregister(l);
        WeatherChangeEvent.getHandlerList().unregister(l);

        ChunkLoadEvent.getHandlerList().unregister(l);
        ChunkPopulateEvent.getHandlerList().unregister(l);
        ChunkUnloadEvent.getHandlerList().unregister(l);
        PortalCreateEvent.getHandlerList().unregister(l);
        SpawnChangeEvent.getHandlerList().unregister(l);
        StructureGrowEvent.getHandlerList().unregister(l);
        WorldInitEvent.getHandlerList().unregister(l);
        WorldLoadEvent.getHandlerList().unregister(l);
        WorldUnloadEvent.getHandlerList().unregister(l);
    }
}
