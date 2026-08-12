package me.bunnky.idreamofeasy.slimefun.items;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.bunnky.idreamofeasy.IDreamOfEasy;
import me.bunnky.idreamofeasy.utils.IDOEUtility;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AlarmClock extends SlimefunItem implements Listener {

    private final Map<UUID, Long> playerTimers = new ConcurrentHashMap<>();
    private final Map<UUID, Boolean> alarmMode = new ConcurrentHashMap<>();
    private final Map<UUID, ScheduledTask> timerTasks = new ConcurrentHashMap<>();
    private final Map<UUID, ScheduledTask> alarmTasks = new ConcurrentHashMap<>();
    private final Set<UUID> awaitingTimerInput = ConcurrentHashMap.newKeySet();

    public AlarmClock(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        IDOEUtility.setGlow(item);
        IDreamOfEasy.getInstance().getServer().getPluginManager().registerEvents(this, IDreamOfEasy.getInstance());
        addItemHandler(onUse());
    }

    private ItemUseHandler onUse() {
        return e -> {
            Player p = e.getPlayer();
            UUID playerId = p.getUniqueId();
            cancelAlarm(playerId);

            if (p.isSneaking()) {
                awaitingTimerInput.remove(playerId);
                boolean isAlarmMode = alarmMode.getOrDefault(playerId, false);
                alarmMode.put(playerId, !isAlarmMode);

                if (!alarmMode.get(playerId)) {
                    cancelAlarm(playerId);
                    p.sendMessage("§cAlarm mode is now disabled.");
                } else {
                    p.sendMessage("§aAlarm mode enabled.");
                }
            } else {
                awaitingTimerInput.add(playerId);
                p.sendMessage("§aEnter timer length in seconds in chat, or type 'cancel'.");
            }
        };
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTimerInput(AsyncChatEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (!awaitingTimerInput.remove(playerId)) {
            return;
        }

        event.setCancelled(true);
        String message = PlainTextComponentSerializer.plainText().serialize(event.message()).trim();
        player.getScheduler().execute(
            IDreamOfEasy.getInstance(),
            () -> handleTimerInput(player, message),
            null,
            1L
        );
    }

    private void handleTimerInput(Player player, String message) {
        if (message.equalsIgnoreCase("cancel")) {
            player.sendMessage("§cTimer setup cancelled.");
            return;
        }

        try {
            long seconds = Long.parseLong(message);
            if (seconds <= 0) {
                player.sendMessage("§cTimer length must be greater than zero seconds.");
                return;
            }

            long duration = Math.multiplyExact(seconds, 1000L);
            long endTime = Math.addExact(System.currentTimeMillis(), duration);
            UUID playerId = player.getUniqueId();
            cancelTimerTask(playerId);
            playerTimers.put(playerId, endTime);
            player.sendMessage("§eTimer set for §f" + seconds + "§es.");
            startTimer(player);
        } catch (NumberFormatException | ArithmeticException ex) {
            player.sendMessage("§cInvalid input. Please enter a valid number in seconds.");
        }
    }

    private void startTimer(Player player) {
        UUID playerId = player.getUniqueId();
        cancelTimerTask(playerId);

        ScheduledTask task = player.getScheduler().runAtFixedRate(
            IDreamOfEasy.getInstance(),
            scheduledTask -> {
                Long endTime = playerTimers.get(playerId);
                if (endTime == null) {
                    scheduledTask.cancel();
                    timerTasks.remove(playerId, scheduledTask);
                    return;
                }

                if (System.currentTimeMillis() >= endTime) {
                    playerTimers.remove(playerId);
                    scheduledTask.cancel();
                    timerTasks.remove(playerId, scheduledTask);
                    ring(player);
                    player.sendMessage("§eTime's up!");

                    if (alarmMode.getOrDefault(playerId, false)) {
                        startAlarmTask(player);
                    }
                }
            },
            null,
            1L,
            20L
        );

        if (task != null) {
            timerTasks.put(playerId, task);
        }
    }

    private void startAlarmTask(Player player) {
        UUID playerId = player.getUniqueId();
        cancelAlarm(playerId);

        ScheduledTask task = player.getScheduler().runAtFixedRate(
            IDreamOfEasy.getInstance(),
            scheduledTask -> {
                if (!alarmMode.getOrDefault(playerId, false)) {
                    scheduledTask.cancel();
                    alarmTasks.remove(playerId, scheduledTask);
                    return;
                }
                ring(player);
            },
            null,
            1L,
            10L
        );

        if (task != null) {
            alarmTasks.put(playerId, task);
        }
    }

    private void ring(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 2.0f);
        player.getScheduler().runDelayed(
            IDreamOfEasy.getInstance(),
            ignored -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.5f),
            null,
            2L
        );
        player.getScheduler().runDelayed(
            IDreamOfEasy.getInstance(),
            ignored -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f),
            null,
            4L
        );
    }

    private void cancelTimerTask(UUID playerId) {
        ScheduledTask task = timerTasks.remove(playerId);
        if (task != null) {
            task.cancel();
        }
    }

    private void cancelAlarm(UUID playerId) {
        ScheduledTask task = alarmTasks.remove(playerId);
        if (task != null) {
            task.cancel();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        awaitingTimerInput.remove(playerId);
        cancelTimerTask(playerId);
        cancelAlarm(playerId);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Long endTime = playerTimers.get(player.getUniqueId());
        if (endTime == null) {
            return;
        }

        if (System.currentTimeMillis() >= endTime) {
            playerTimers.remove(player.getUniqueId());
            ring(player);
            player.sendMessage("§eTime's up!");
            if (alarmMode.getOrDefault(player.getUniqueId(), false)) {
                startAlarmTask(player);
            }
        } else {
            startTimer(player);
        }
    }
}
