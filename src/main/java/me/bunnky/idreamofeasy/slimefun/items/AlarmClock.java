package me.bunnky.idreamofeasy.slimefun.items;

import io.github.bakedlibs.dough.chat.ChatInput;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class AlarmClock extends SlimefunItem {

    private final HashMap<UUID, Long> playerTimers = new HashMap<>();
    private final HashMap<UUID, Boolean> alarmMode = new HashMap<>();
    private final HashMap<UUID, BukkitRunnable> alarmTasks = new HashMap<>();

    public AlarmClock(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        IDOEUtility.setGlow(item);

        addItemHandler(onUse());
    }

    private ItemUseHandler onUse() {
        return e -> {
            Player p = e.getPlayer();
            UUID playerId = p.getUniqueId();
            cancelAlarm(playerId);

            if (p.isSneaking()) {
                boolean isAlarmMode = alarmMode.getOrDefault(playerId, false);
                alarmMode.put(playerId, !isAlarmMode);

                if (!alarmMode.get(playerId)) {
                    cancelAlarm(playerId);
                    p.sendMessage("§cAlarm mode is now disabled.");
                } else {
                    p.sendMessage("§aAlarm mode enabled.");
                }
            } else {
                p.sendMessage("§aEnter timer length in seconds");

                ChatInput.waitForPlayer(IDreamOfEasy.getInstance(), p, msg -> {
                    try {
                        long duration = Long.parseLong(msg) * 1000;
                        long endTime = System.currentTimeMillis() + duration;
                        playerTimers.put(playerId, endTime);
                        p.sendMessage("§eTimer set for §f" + duration / 1000 + "§es.");
                        startTimer(p);
                    } catch (NumberFormatException ex) {
                        p.sendMessage("§cInvalid input. Please enter a valid number in seconds.");
                    }
                });
            }
        };
    }

    private void startTimer(Player player) {
        UUID playerId = player.getUniqueId();

        new BukkitRunnable() {
            @Override
            public void run() {
                Long endTime = playerTimers.get(playerId);
                if (endTime == null) {
                    return;
                }

                long remainingTime = endTime - System.currentTimeMillis();

                if (remainingTime <= 0) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 2.0f);

                    Bukkit.getScheduler().runTaskLater(IDreamOfEasy.getInstance(), () -> {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.5f);
                    }, 2L);

                    Bukkit.getScheduler().runTaskLater(IDreamOfEasy.getInstance(), () -> {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
                    }, 4L);

                    playerTimers.remove(playerId);
                    this.cancel();
                    player.sendMessage("§eTime's up!");

                    if (alarmMode.getOrDefault(playerId, false)) {
                        startAlarmTask(player.getUniqueId());
                    }
                }
            }
        }.runTaskTimer(IDreamOfEasy.getInstance(), 0, 0);
    }

    private void startAlarmTask(UUID playerId) {
        BukkitRunnable alarmTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (alarmMode.getOrDefault(playerId, false)) {
                    Player player = Bukkit.getPlayer(playerId);
                    if (player != null) {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 2.0f);

                        Bukkit.getScheduler().runTaskLater(IDreamOfEasy.getInstance(), () -> {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.5f);
                        }, 2L);

                        Bukkit.getScheduler().runTaskLater(IDreamOfEasy.getInstance(), () -> {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
                        }, 4L);

                    }
                } else {
                    this.cancel();
                }
            }
        };
        alarmTask.runTaskTimer(IDreamOfEasy.getInstance(), 0, 10);
        alarmTasks.put(playerId, alarmTask);
    }

    private void cancelAlarm(UUID playerId) {
        BukkitRunnable task = alarmTasks.get(playerId);
        if (task != null) {
            task.cancel();
            alarmTasks.remove(playerId);
        }
    }
}