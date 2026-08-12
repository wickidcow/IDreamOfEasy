package me.bunnky.idreamofeasy.slimefun.items;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import me.bunnky.idreamofeasy.utils.IDOEUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/*
This specialized tool allows you to break heads with a simple right-click, clearing them from your path with ease.
 */

public class Jawn extends SimpleSlimefunItem<ItemUseHandler> {

    public Jawn(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        IDOEUtility.setGlow(item);
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return this::onRightClick;
    }

    private void onRightClick(@NotNull PlayerRightClickEvent e) {
        SlimefunItem hand = SlimefunItem.getByItem(e.getItem());

        if (!(hand instanceof Jawn)) return;

        e.cancel();

        Player p = e.getPlayer();
        Block b = e.getInteractEvent().getClickedBlock();

        if (b == null) return;

        if (IDOEUtility.isHead(b)) breakBlock(b, p);
    }

    public static void breakBlock(Block b, Player p) {
        if (!(Slimefun.getProtectionManager().hasPermission(p, b, Interaction.INTERACT_BLOCK))){
            return;
        }
        BlockBreakEvent breakEvent = new BlockBreakEvent(b, p);
        Bukkit.getPluginManager().callEvent(breakEvent);
        if (!breakEvent.isCancelled()) {
            if (StorageCacheUtils.hasBlock(b.getLocation())) {
                Slimefun.getDatabaseManager().getBlockDataController().removeBlock(b.getLocation());
                b.setType(Material.AIR);
            } else {
                b.breakNaturally();
            }
        }
    }

}
