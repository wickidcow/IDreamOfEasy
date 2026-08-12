package me.bunnky.idreamofeasy.slimefun.tasks;

import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.bunnky.idreamofeasy.IDreamOfEasy;
import me.bunnky.idreamofeasy.listeners.MagnetoidListener;
import me.bunnky.idreamofeasy.slimefun.items.Magnetoid;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class MagnetoidTask extends BukkitRunnable {

    private static final float COST = 0.01F;

    private final double r;
    private final Player p;
    private final Magnetoid magnetoid;
    private final NamespacedKey teleportMarker;

    public MagnetoidTask(@NotNull Player p, double r, @NotNull Magnetoid magnetoid) {
        this.r = r;
        this.p = p;
        this.magnetoid = magnetoid;
        this.teleportMarker = new NamespacedKey(IDreamOfEasy.getInstance(), "magnetoid_tp");
    }

    @Override
    public void run() {
        if (!isValid()) {
            MagnetoidListener.activeTasks.remove(p.getUniqueId());
            cancel();
            return;
        }

        ItemStack off = p.getInventory().getItemInOffHand();
        if (!SlimefunUtils.isItemSimilar(off, magnetoid.getItem(), true)) {
            MagnetoidListener.activeTasks.remove(p.getUniqueId());
            cancel();
            return;
        }

        if (!p.isSneaking()) {
            for (Entity ent : p.getNearbyEntities(r, r, r)) {
                if (ent instanceof Item item && isValidItem(item) && !isTeleportMarked(item)) {
                    if (magnetoid.removeItemCharge(off, COST)) {
                        item.teleport(p.getLocation());
                        p.playSound(p, Sound.BLOCK_AMETHYST_BLOCK_STEP, SoundCategory.PLAYERS, 0.2F, 0.2F);
                        markTeleported(item);
                        Bukkit.getScheduler().runTaskLater(IDreamOfEasy.getInstance(), () -> clearTeleportMarker(item), 10L);
                    }
                }
            }
        }
    }

    protected boolean isValid() {
        return p.isOnline() && p.getGameMode() != GameMode.SPECTATOR;
    }

    private boolean isValidItem(Item item) {
        return !item.isDead()
            && !SlimefunUtils.hasNoPickupFlag(item)
            && item.getPickupDelay() <= 0
            && p.getLocation().distanceSquared(item.getLocation()) > 0.3;
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
