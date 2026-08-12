package me.bunnky.idreamofeasy.slimefun.machines;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.ASlimefunDataContainer;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.armor.RadiationTask;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.utils.RadiationUtils;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.bunnky.idreamofeasy.IDreamOfEasy;
import me.bunnky.idreamofeasy.utils.IDOEUtility;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
/*
Protect yourself from harmful radiation. This device absorbs radiation in a radius, granting players temporary immunity while powered.
 */
public class RadiationAbsorber extends SlimefunItem implements EnergyNetComponent {

    private static final int GRACE_PERIOD = Math.max(1, Slimefun.getCfg().getInt("options.radiation-grace-period"));
    private static final Set<UUID> PROTECTED_PLAYERS = ConcurrentHashMap.newKeySet();

    private final Map<UUID, Long> cooldowns = new ConcurrentHashMap<>();
    private final Map<UUID, BossBar> bars = new ConcurrentHashMap<>();
    private final Map<UUID, ScheduledTask> protectionTasks = new ConcurrentHashMap<>();
    private final Map<UUID, Location> protectionOrigins = new ConcurrentHashMap<>();

    private final int cap;
    private final int ecost;

    public RadiationAbsorber(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int ecost, int cap, int range) {
        super(itemGroup, item, recipeType, recipe);

        this.cap = cap;
        this.ecost = ecost;

        addItemHandler(new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(@NotNull BlockBreakEvent e, @NotNull ItemStack stack, @NotNull List<ItemStack> drops) {
                Location brokenLocation = e.getBlock().getLocation();
                protectionOrigins.forEach((playerId, origin) -> {
                    if (sameBlock(origin, brokenLocation)) {
                        endProtection(playerId, true);
                    }
                });
            }
        });

        addItemHandler(new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return true;
            }

            @Override
            public void tick(Block b, SlimefunItem sfItem, ASlimefunDataContainer data) {
                Location loc = b.getLocation();

                for (Player player : loc.getNearbyPlayers(range)) {
                    if (!Slimefun.getProtectionManager().hasPermission(player, loc, Interaction.INTERACT_BLOCK)) {
                        continue;
                    }
                    if (!eligible(player)) {
                        continue;
                    }
                    startProtection(loc, player, data);
                }
            }
        });
    }

    private boolean eligible(Player player) {
        if (player.getGameMode() != GameMode.SURVIVAL || PROTECTED_PLAYERS.contains(player.getUniqueId())) {
            return false;
        }

        Long lastProtection = cooldowns.get(player.getUniqueId());
        return lastProtection == null || System.currentTimeMillis() - lastProtection >= GRACE_PERIOD * 1000L;
    }

    private void startProtection(Location loc, Player player, ASlimefunDataContainer data) {
        UUID playerId = player.getUniqueId();

        // Atomically claim this player so two absorbers cannot start duplicate sessions.
        if (!PROTECTED_PLAYERS.add(playerId)) {
            return;
        }

        if (getChargeLong(loc, data) < ecost) {
            PROTECTED_PLAYERS.remove(playerId);
            return;
        }

        removeCharge(loc, (long) ecost, data);

        RadiationTask.addGracePeriod(player);
        RadiationUtils.clearExposure(player);

        cooldowns.put(playerId, System.currentTimeMillis());
        protectionOrigins.put(playerId, loc.clone());

        BossBar bar = IDOEUtility.createBossBar(player);
        bars.put(playerId, bar);
        IDOEUtility.spawnBeam(player, loc);

        int[] remainingSeconds = {GRACE_PERIOD};
        ScheduledTask scheduled = Bukkit.getRegionScheduler().runAtFixedRate(
            IDreamOfEasy.getInstance(),
            loc,
            task -> {
                if (!PROTECTED_PLAYERS.contains(playerId) || !sameSlimefunBlock(loc)) {
                    task.cancel();
                    endProtection(playerId, false);
                    return;
                }

                remainingSeconds[0]--;
                double progress = Math.max(0.0, Math.min(1.0, (double) remainingSeconds[0] / GRACE_PERIOD));
                updateBar(playerId, progress);

                if (remainingSeconds[0] <= 0) {
                    task.cancel();
                    endProtection(playerId, false);
                    return;
                }

                ASlimefunDataContainer currentData = StorageCacheUtils.getDataContainer(loc);
                if (currentData != null) {
                    removeCharge(loc, (long) ecost, currentData);
                }

                loc.getWorld().spawnParticle(
                    Particle.ITEM_SLIME,
                    loc.clone().add(0.5, 0.5, 0.5),
                    5,
                    0.5,
                    0.5,
                    0.5,
                    0
                );
            },
            20L,
            20L
        );

        protectionTasks.put(playerId, scheduled);
    }

    private boolean sameSlimefunBlock(Location loc) {
        SlimefunItem item = StorageCacheUtils.getSlimefunItem(loc);
        return item == this;
    }

    private void updateBar(UUID playerId, double progress) {
        BossBar bar = bars.get(playerId);
        Player player = Bukkit.getPlayer(playerId);
        if (bar == null || player == null) {
            return;
        }

        player.getScheduler().execute(
            IDreamOfEasy.getInstance(),
            () -> bar.setProgress(progress),
            null,
            1L
        );
    }

    private void endProtection(UUID playerId, boolean cancelTask) {
        if (cancelTask) {
            ScheduledTask task = protectionTasks.remove(playerId);
            if (task != null) {
                task.cancel();
            }
        } else {
            protectionTasks.remove(playerId);
        }

        protectionOrigins.remove(playerId);
        PROTECTED_PLAYERS.remove(playerId);

        BossBar bar = bars.remove(playerId);
        if (bar == null) {
            return;
        }

        Player player = Bukkit.getPlayer(playerId);
        if (player != null) {
            player.getScheduler().execute(
                IDreamOfEasy.getInstance(),
                () -> bar.removePlayer(player),
                bar::removeAll,
                1L
            );
        } else {
            bar.removeAll();
        }
    }

    private boolean sameBlock(Location first, Location second) {
        return first.getWorld() == second.getWorld()
            && first.getBlockX() == second.getBlockX()
            && first.getBlockY() == second.getBlockY()
            && first.getBlockZ() == second.getBlockZ();
    }

    @Override
    public @NotNull EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public long getCapacityLong() {
        return cap;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getCapacity() {
        return cap;
    }
}
