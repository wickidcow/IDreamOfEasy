package me.bunnky.idreamofeasy.slimefun.tasks;

import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.bunnky.idreamofeasy.IDreamOfEasy;
import me.bunnky.idreamofeasy.listeners.MagnetoidListener;
import me.bunnky.idreamofeasy.slimefun.items.Magnetoid;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public final class MagnetoidTask {

    private static final float COST = 0.01F;

    private final double r;
    private final Player player;
    private final Magnetoid magnetoid;
    private final NamespacedKey teleportMarker;

    public MagnetoidTask(@NotNull Player player, double r, @NotNull Magnetoid magnetoid) {
        this.r = r;
        this.player = player;
        this.magnetoid = magnetoid;
        this.teleportMarker = new NamespacedKey(IDreamOfEasy.getInstance(), "magnetoid_tp");
    }

    public void run(@NotNull ScheduledTask task) {
        if (!isValid()) {
            stop(task);
            return;
        }

        ItemStack offhand = player.getInventory().getItemInOffHand();
        if (!SlimefunUtils.isItemSimilar(offhand, magnetoid.getItem(), true)) {
            stop(task);
            return;
        }

        if (player.isSneaking()) {
            return;
        }

        for (Entity entity : player.getNearbyEntities(r, r, r)) {
            if (!(entity instanceof Item item) || !isValidItem(item) || isTeleportMarked(item)) {
                continue;
            }

            if (!magnetoid.removeItemCharge(offhand, COST)) {
                return;
            }

            Location destination = player.getLocation().clone();
            player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_STEP, SoundCategory.PLAYERS, 0.2F, 0.2F);

            item.getScheduler().execute(
                IDreamOfEasy.getInstance(),
                () -> teleportItem(item, destination),
                null,
                1L
            );
        }
    }

    private void teleportItem(Item item, Location destination) {
        if (!isValidItem(item) || isTeleportMarked(item)) {
            return;
        }

        item.teleport(destination);
        markTeleported(item);
        item.getScheduler().runDelayed(
            IDreamOfEasy.getInstance(),
            ignored -> clearTeleportMarker(item),
            null,
            10L
        );
    }

    private void stop(ScheduledTask task) {
        MagnetoidListener.activeTasks.remove(player.getUniqueId(), task);
        task.cancel();
    }

    private boolean isValid() {
        return player.isOnline() && player.getGameMode() != GameMode.SPECTATOR;
    }

    private boolean isValidItem(Item item) {
        return item.isValid()
            && !item.isDead()
            && !SlimefunUtils.hasNoPickupFlag(item)
            && item.getPickupDelay() <= 0
            && player.getWorld() == item.getWorld()
            && player.getLocation().distanceSquared(item.getLocation()) > 0.3;
    }

    private boolean isTeleportMarked(Item item) {
        return item.getPersistentDataContainer().has(teleportMarker, PersistentDataType.BYTE);
    }

    private void markTeleported(Item item) {
        item.getPersistentDataContainer().set(teleportMarker, PersistentDataType.BYTE, (byte) 1);
    }

    private void clearTeleportMarker(Item item) {
        if (item.isValid()) {
            item.getPersistentDataContainer().remove(teleportMarker);
        }
    }
}
