package me.bunnky.idreamofeasy.listeners;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.bunnky.idreamofeasy.IDreamOfEasy;
import me.bunnky.idreamofeasy.slimefun.items.Magnetoid;
import me.bunnky.idreamofeasy.slimefun.tasks.MagnetoidTask;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MagnetoidListener implements Listener {

    public static final Map<UUID, ScheduledTask> activeTasks = new ConcurrentHashMap<>();

    private final IDreamOfEasy plugin;

    public MagnetoidListener(@Nonnull IDreamOfEasy plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        Bukkit.getGlobalRegionScheduler().runAtFixedRate(
            plugin,
            ignored -> checkAllPlayersForMagnetoid(),
            1L,
            10L
        );
    }

    private void checkAllPlayersForMagnetoid() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getScheduler().execute(
                plugin,
                () -> {
                    if (player.getGameMode() != GameMode.SPECTATOR) {
                        handleMagnetoid(player);
                    } else {
                        cancelTask(player);
                    }
                },
                () -> activeTasks.remove(player.getUniqueId()),
                1L
            );
        }
    }

    private void handleMagnetoid(Player player) {
        UUID playerId = player.getUniqueId();
        SlimefunItem sfItem = SlimefunItem.getByItem(player.getInventory().getItemInOffHand());

        if (sfItem instanceof Magnetoid magnetoid) {
            if (!activeTasks.containsKey(playerId) && magnetoid.canUse(player, true)) {
                MagnetoidTask worker = new MagnetoidTask(player, magnetoid.getR(), magnetoid);
                ScheduledTask task = player.getScheduler().runAtFixedRate(
                    plugin,
                    worker::run,
                    () -> activeTasks.remove(playerId),
                    1L,
                    10L
                );
                if (task != null) {
                    activeTasks.put(playerId, task);
                }
            }
        } else {
            cancelTask(player);
        }
    }

    public void cancelTask(Player player) {
        ScheduledTask task = activeTasks.remove(player.getUniqueId());
        if (task != null) {
            task.cancel();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        cancelTask(event.getPlayer());
    }
}
