package me.bunnky.idreamofeasy.slimefun.items.idols;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/*
Terran Idol: The Terran Idol improves mining, farming, and hunting rewards while periodically granting Haste.
*/
public class TerranIdol extends Idol {

    private final Random random = new Random();

    public TerranIdol(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    protected void handleEvent(Event e) {
        if (e instanceof BlockBreakEvent) {
            onBlockBreak((BlockBreakEvent) e);
        } else if (e instanceof EntityDeathEvent) {
            onEntityDeath((EntityDeathEvent) e);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Material type = e.getBlock().getType();
        ItemStack tool = p.getInventory().getItemInMainHand();
        int fortuneLevel = tool.getEnchantmentLevel(Enchantment.FORTUNE);

        if (!p.hasPotionEffect(PotionEffectType.HASTE) && random.nextDouble() < 0.5) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 100, 1));
            sendMessage(p, this.getItemName() + ": §r§a+ Haste II");
        }

        switch (type) {
            case DIAMOND_ORE, GOLD_ORE, IRON_ORE, EMERALD_ORE, COAL_ORE, COPPER_ORE, REDSTONE_ORE, LAPIS_ORE,
                 DEEPSLATE_DIAMOND_ORE, DEEPSLATE_GOLD_ORE, DEEPSLATE_IRON_ORE,
                 DEEPSLATE_EMERALD_ORE, DEEPSLATE_COAL_ORE, DEEPSLATE_COPPER_ORE,
                 DEEPSLATE_REDSTONE_ORE, DEEPSLATE_LAPIS_ORE, NETHER_GOLD_ORE, NETHER_QUARTZ_ORE -> {
                if (random.nextDouble() < 0.2) {
                    e.setDropItems(false);
                    Collection<ItemStack> normalDrops = e.getBlock().getDrops(p.getInventory().getItemInMainHand(), p);
                    for (ItemStack drop : normalDrops) {
                        int extra = fortuneLevel > 0 ? random.nextInt(fortuneLevel + 1) : 0;
                        ItemStack doubledDrop = new ItemStack(drop.getType(), drop.getAmount() * (2 + extra));
                        e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), doubledDrop);
                    }
                    sendMessage(p, this.getItemName() + ": §r§aDouble ore drops!");
                }
            }

            case CARROTS, POTATOES, BEETROOTS, WHEAT, NETHER_WART -> {
                if (random.nextDouble() < 0.2) {
                    e.setDropItems(false);
                    Collection<ItemStack> normalDrops = e.getBlock().getDrops(p.getInventory().getItemInMainHand(), p);
                    for (ItemStack drop : normalDrops) {
                        int extra = fortuneLevel > 0 ? random.nextInt(fortuneLevel + 1) : 0;
                        int newAmount = drop.getAmount() * (2 + extra);
                        e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(drop.getType(), newAmount));
                    }
                    sendMessage(p, this.getItemName() + ": §r§aDouble crops!");
                }
            }

            case COCOA, SUGAR_CANE, PUMPKIN, MELON -> {
                if (random.nextDouble() < 0.2) {
                    e.setDropItems(false);
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(type, 2));
                    sendMessage(p, this.getItemName() + ": §r§aDouble crops!");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent e) {
        if (random.nextDouble() < 0.2) {
            LivingEntity entity = e.getEntity();
            Player killer = entity.getKiller();
            if (killer != null) {
                ItemStack weapon = killer.getInventory().getItemInMainHand();
                int lootingLevel = weapon.getEnchantmentLevel(Enchantment.LOOTING);

                List<ItemStack> drops = e.getDrops();
                List<ItemStack> doubledDrops = new ArrayList<>();

                for (ItemStack drop : drops) {
                    int baseAmount = drop.getAmount();
                    int extra = lootingLevel > 0 ? random.nextInt(lootingLevel + 1) : 0;
                    doubledDrops.add(new ItemStack(drop.getType(), baseAmount * 2 + extra));
                }

                drops.clear();
                drops.addAll(doubledDrops);
                sendMessage(killer, this.getItemName() + ": §r§aDouble drops!");
            }
        }
    }
}
