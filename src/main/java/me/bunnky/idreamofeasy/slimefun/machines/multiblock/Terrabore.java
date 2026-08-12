package me.bunnky.idreamofeasy.slimefun.machines.multiblock;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.miner.IndustrialMiner;
import me.bunnky.idreamofeasy.utils.MaterialUtility;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
/*
Description: The Terrabore is a variant of the Industrial Miner. It works in a 7x7 radius and mines everything. Requires basic combustibles like coal or wood as fuel.
 */

public class Terrabore extends IndustrialMiner {
    public Terrabore(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, Material.DIRT, false, 3);
    }

    @Override
    public @NotNull ItemStack getOutcome(@NotNull Material material) {
        MaterialUtility.DropInfo dropInfo = MaterialUtility.getDropInfo(material);
        int amount = dropInfo.getRandomAmount();
        return new ItemStack(dropInfo.getMaterial(), amount);
    }

    @Override
    public boolean canMine(@NotNull Block b) {
        return b.getType().getHardness() >= 0 &&
            b.getType().isSolid() &&
            !StorageCacheUtils.hasBlock(b.getLocation());
    }
}
