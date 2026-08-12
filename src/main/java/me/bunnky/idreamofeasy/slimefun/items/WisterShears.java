package me.bunnky.idreamofeasy.slimefun.items;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.EntityInteractHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import me.bunnky.idreamofeasy.utils.IDOEUtility;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Snowman;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
/*
The Wister Shears can shear certain mobs and efficiently clear leaves from trees, grass, and other foliage. When used on a mob, it provides unique drops based on the creature.
 */
public class WisterShears extends SlimefunItem {

    private static final int COOLDOWN_TICKS = 100; // 5 Seconds
    private static final int LEAF_RADIUS = 5;

    public WisterShears(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        IDOEUtility.setGlow(item);

        addItemHandler(onBlockBreak());

        addItemHandler((EntityInteractHandler) (e, stack, b) -> {
            Player p = e.getPlayer();
            Entity entity = e.getRightClicked();

            if (entity != null) {
                if (!(entity instanceof Sheep || entity instanceof MushroomCow || entity instanceof Snowman)) {
                    e.setCancelled(true);

                    if (p.hasCooldown(Material.SHEARS)) {
                        p.sendMessage("§cYou must wait before using this again!");
                        return;
                    }
                    handleShearing(entity);
                    p.setCooldown(Material.SHEARS, COOLDOWN_TICKS);
                }
            }
        });
    }

    @Nonnull
    private ToolUseHandler onBlockBreak() {
        return (e, tool, fortune, drops) -> {
            Player p = e.getPlayer();
            Block b = e.getBlock();
            Material type = b.getType();

            if (Tag.LEAVES.isTagged(type) || Tag.REPLACEABLE_BY_TREES.isTagged(type)) {
                if (p.hasCooldown(Material.SHEARS)) {
                    return;
                }

                handleLeaves(b);
                p.setCooldown(Material.SHEARS, COOLDOWN_TICKS);
            }
        };
    }

    private void handleShearing(Entity entity) {
        ItemStack drop = IDOEUtility.getEntityDrop(entity.getType());
        if (drop != null) {
            entity.getWorld().dropItemNaturally(entity.getLocation(), drop);
            entity.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, entity.getLocation().add(0.5, 0.5, 0.5), 30, 0.3, 0.3, 0.3, 0.05);
        }
    }

    private void handleLeaves(Block startBlock) {
        World w = startBlock.getWorld();
        Location start = startBlock.getLocation();

        for (int x = -LEAF_RADIUS; x <= LEAF_RADIUS; x++) {
            for (int y = -LEAF_RADIUS; y <= LEAF_RADIUS; y++) {
                for (int z = -LEAF_RADIUS; z <= LEAF_RADIUS; z++) {
                    Location loc = start.clone().add(x, y, z);
                    Block b = w.getBlockAt(loc);

                    if (Tag.LEAVES.isTagged(b.getType()) || Tag.REPLACEABLE_BY_TREES.isTagged(b.getType())) {
                        if (BlockStorage.hasBlockInfo(b)){
                            BlockStorage.clearBlockInfo(b);
                        }
                        b.breakNaturally(new ItemStack(Material.SHEARS));
                        w.spawnParticle(Particle.INSTANT_EFFECT, b.getLocation(), 2, 0.2, 0.2, 0.2, 0.1);
                    }
                }
            }
        }
        w.playSound(start, Sound.ENTITY_SHEEP_SHEAR, 1.0f, 1.0f);
    }

}