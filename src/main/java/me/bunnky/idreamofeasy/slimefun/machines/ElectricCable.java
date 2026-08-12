package me.bunnky.idreamofeasy.slimefun.machines;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.ASlimefunDataContainer;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ElectricCable extends SlimefunItem implements EnergyNetComponent {
    private final int cap;
    private final int ecost;
    private final double damageAmount;
    private final double knockbackStrength;
    private final double range = 1.5;

    public ElectricCable(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, @Nullable ItemStack recipeOutput, int ecost, int cap, double damageAmount, double knockbackStrength) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
        this.cap = cap;
        this.ecost = ecost;
        this.damageAmount = damageAmount;
        this.knockbackStrength = knockbackStrength;

        addItemHandler(onPlace());

        addItemHandler(new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return true;
            }

            @Override
            public void tick(Block b, SlimefunItem sfItem, ASlimefunDataContainer data) {
                Location loc = b.getLocation();

                if (getChargeLong(loc, data) < ecost) {
                    return;
                }

                removeCharge(loc, (long) ecost, data);
                nearbyEntities(loc, data.getData("owner"));
            }
        });
    }

    @Nonnull
    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@NotNull BlockPlaceEvent e) {
                StorageCacheUtils.setData(e.getBlock().getLocation(), "owner", e.getPlayer().getUniqueId().toString());
            }
        };
    }

    private void nearbyEntities(Location loc, String ownerUUID) {
        double minX = loc.getBlockX() - range;
        double maxX = loc.getBlockX() + range;
        double minY = loc.getBlockY() - 1;
        double maxY = loc.getBlockY() + 1;
        double minZ = loc.getBlockZ() - range;
        double maxZ = loc.getBlockZ() + range;
        BoundingBox cableBox = new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);

        for (Entity entity : loc.getNearbyEntities(range, range, range)) {
            if (!entity.getBoundingBox().overlaps(cableBox) || entity.getType() == EntityType.ITEM) {
                continue;
            }

            if (entity instanceof Player player) {
                if (player.getGameMode() != GameMode.SURVIVAL) {
                    continue;
                }
                if (ownerUUID != null && player.getUniqueId().toString().equals(ownerUUID)) {
                    continue;
                }
            }

            damage(loc, entity);
        }
    }

    private void damage(Location loc, Entity entity) {
        if (entity instanceof Damageable damageable) {
            damageable.damage(damageAmount);
        }

        Vector dir = entity.getLocation().toVector().subtract(loc.toVector());
        if (dir.lengthSquared() > 0) {
            dir.normalize().multiply(knockbackStrength);
        }
        dir.setY(0.3);
        entity.setVelocity(dir);

        loc.getWorld().playSound(loc, Sound.ENTITY_ARROW_HIT, 0.5F, 5.0F);
        loc.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, loc.clone().add(0.5, 0.5, 0.5), 10, 0.3, 0.3, 0.3, 0.1);
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
