package me.bunnky.idreamofeasy.slimefun.machines.multiblock;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.miner.IndustrialMiner;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
/*
Description: The Advanced Terrabore is a variant of the Terrabore. It works in an 11x11 radius and mines everything but ores. Requires lava, oil, or fuel.
 */
public class AdvancedTerrabore extends IndustrialMiner {
    public AdvancedTerrabore(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, Material.LAPIS_BLOCK, true, 5);
    }

    @Override
    protected void registerDefaultFuelTypes() {
        fuelTypes.add(new MachineFuel(640, new ItemStack(Material.LAVA_BUCKET)));
        fuelTypes.add(new MachineFuel(1280, SlimefunItems.OIL_BUCKET));
        fuelTypes.add(new MachineFuel(1920, SlimefunItems.FUEL_BUCKET));
    }

    @Override
    public @NotNull ItemStack getOutcome(@NotNull Material material) {
        return new ItemStack(material);
    }

    @Override
    public boolean canMine(@NotNull Block b) {
        return !SlimefunTag.INDUSTRIAL_MINER_ORES.isTagged(b.getType()) &&
            b.getType().getHardness() >= 0 &&
            b.getType().isSolid() &&
            !StorageCacheUtils.hasBlock(b.getLocation());
    }
}
