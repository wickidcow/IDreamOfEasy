package me.bunnky.idreamofeasy.slimefun.machines;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.ASlimefunDataContainer;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class ElectricShearer extends SlimefunItem implements EnergyNetComponent {
    private final int cap;
    private final int ecost;

    public ElectricShearer(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int ecost, int cap, int range) {
        super(itemGroup, item, recipeType, recipe);
        this.cap = cap;
        this.ecost = ecost;

        addItemHandler(onRightClick());

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

                if (shearSheep(loc, range)) {
                    removeCharge(loc, (long) ecost, data);
                }
            }
        });
    }

    private boolean shearSheep(Location loc, int range) {
        boolean shearedAny = false;

        for (Entity entity : loc.getNearbyEntities(range, range, range)) {
            if (!(entity instanceof Sheep sheep) || sheep.isSheared() || sheep.getLocation().distanceSquared(loc) > (double) range * range) {
                continue;
            }

            sheep.setSheared(true);
            Material woolMaterial = Material.valueOf(sheep.getColor().name() + "_WOOL");
            sheep.getWorld().dropItemNaturally(sheep.getLocation(), new ItemStack(woolMaterial));

            Location sheepLocation = sheep.getLocation();
            sheep.getWorld().spawnParticle(Particle.CLOUD, sheepLocation, 20, 0.5, 0.5, 0.5, 0.1);
            sheep.getWorld().playSound(sheepLocation, Sound.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
            shearedAny = true;
        }

        return shearedAny;
    }

    public @Nonnull BlockUseHandler onRightClick() {
        return PlayerRightClickEvent::cancel;
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
