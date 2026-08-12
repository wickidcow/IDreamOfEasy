package me.bunnky.idreamofeasy.slimefun.items;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import me.bunnky.idreamofeasy.IDreamOfEasy;
import me.bunnky.idreamofeasy.utils.IDOEUtility;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AlarmClock extends SlimefunItem implements Listener {

    private final HashMap<UUID, Long> playerTimers = new HashMap<>();
    private final HashMap<UUID, Boolean> alarmMode = new HashMap<>();
    private final HashMap<UUID, BukkitRunnable> alarmTasks = new HashMap<>();
    private final Set<UUID> awaitingTimerInput = new HashSet<>();

    public AlarmClock(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        IDOEUtility.setGlow(item);
        Bukkit.getPluginManager().registerEvents(this, IDreamOfEasy.getInstance());
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

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.LOWEST)
    public void onTimerInput(AsyncPlayerChatEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        if (!awaitingTimerInput.remove(playerId)) {
            return;
        }

        event.setCancelled(true);
        String message = event.getMessage().trim();
        Bukkit.getScheduler().runTask(IDreamOfEasy.getInstance(), () -> handleTimerInput(event.getPlayer(), message));
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
            playerTimers.put(player.getUniqueId(), endTime);
            player.sendMessage("§eTimer set for §f" + seconds + "§es.");
            startTimer(player);
        } catch (NumberFormatException | ArithmeticException ex) {
            player.sendMessage("§cInvalid input. Please enter a valid number in seconds.");
        }
    }

    private void startTimer(Player player) {
        UUID playerId = player.getUniqueId();

        new BukkitRunnable() {
            @Override
            public void run() {
                Long endTime = playerTimers.get(playerId);
                if (endTime == null) {
                    cancel();
                    return;
                }

                long remainingTime = endTime - System.currentTimeMillis();
                if (remainingTime <= 0) {
                    ring(player);
                    playerTimers.remove(playerId);
                    cancel();
                    player.sendMessage("§eTime's up!");

                    if (alarmMode.getOrDefault(playerId, false)) {
                        startAlarmTask(playerId);
                    }
                }
            }
        }.runTaskTimer(IDreamOfEasy.getInstance(), 0L, 1L);
    }

    private void startAlarmTask(UUID playerId) {
        BukkitRunnable previous = alarmTasks.remove(playerId);
        if (previous != null) {
            previous.cancel();
        }

        BukkitRunnable alarmTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!alarmMode.getOrDefault(playerId, false)) {
                    cancel();
                    alarmTasks.remove(playerId);
                    return;
                }

                Player player = Bukkit.getPlayer(playerId);
                if (player != null && player.isOnline()) {
                    ring(player);
                }
            }
        };
        alarmTask.runTaskTimer(IDreamOfEasy.getInstance(), 0L, 10L);
        alarmTasks.put(playerId, alarmTask);
    }

    private void ring(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 2.0f);
        Bukkit.getScheduler().runTaskLater(IDreamOfEasy.getInstance(),
            () -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.5f), 2L);
        Bukkit.getScheduler().runTaskLater(IDreamOfEasy.getInstance(),
            () -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f), 4L);
    }

    private void cancelAlarm(UUID playerId) {
        BukkitRunnable task = alarmTasks.remove(playerId);
        if (task != null) {
            task.cancel();
        }
    }
}
