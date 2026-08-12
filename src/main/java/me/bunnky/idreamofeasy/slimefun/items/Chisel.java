package me.bunnky.idreamofeasy.slimefun.items;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.Rechargeable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import me.bunnky.idreamofeasy.utils.ChiselConverter;
import me.bunnky.idreamofeasy.utils.IDOEUtility;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class Chisel extends SlimefunItem implements Rechargeable {

    private final float cap;
    private static final float COST = 2.5F;

    public Chisel(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, float cap) {
        super(itemGroup, item, recipeType, recipe);
        this.cap = cap;
        IDOEUtility.setGlow(item);

        addItemHandler((ItemUseHandler) e -> {
            e.cancel();

            final Optional<Block> optional = e.getClickedBlock();
            if (optional.isPresent()) {
                final Block b = optional.get();

                if (!(Slimefun.getProtectionManager().hasPermission(e.getPlayer(), b.getLocation(), Interaction.INTERACT_BLOCK))) {
                    return;
                }

                Material stoneCutterResult = ChiselConverter.convert(b.getType());

                if (stoneCutterResult != b.getType() && removeItemCharge(e.getItem(), COST) && !StorageCacheUtils.hasBlock(b.getLocation())) {
                    b.setType(stoneCutterResult);

                    b.getWorld().playSound(b.getLocation(), Sound.BLOCK_SNIFFER_EGG_PLOP, 0.5F, 5.0F);
                    b.getWorld().spawnParticle(Particle.SCRAPE, b.getLocation().add(0.5, 0.5, 0.5), 5, 0.5, 0.5, 0.5, 0.1);
                }
            }
        });
    }

    @Override
    public float getMaxItemCharge(ItemStack itemStack) {
        return cap;
    }
}
