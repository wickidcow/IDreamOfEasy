package me.bunnky.idreamofeasy.slimefun.items.idols;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.Random;

/*
Divine Idol: The Divine Idol is a powerful artifact that enhances enchanting, amplifies experience gain, and prolongs the durability of tools and armor.
*/
public class DivineIdol extends Idol {

    private final Random random = new Random();

    public DivineIdol(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    protected void handleEvent(Event e) {
        if (e instanceof EnchantItemEvent) {
            onEnchant((EnchantItemEvent) e);
        } else if (e instanceof PlayerExpChangeEvent) {
            onPlayerExperience((PlayerExpChangeEvent) e);
        } else if (e instanceof PlayerItemDamageEvent) {
            onItemDamage((PlayerItemDamageEvent) e);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEnchant(EnchantItemEvent e) {
        Player p = e.getEnchanter();
        ItemStack item = e.getItem();
        Material type = item.getType();

        if (random.nextDouble() < 0.8) {
            Map<Enchantment, Integer> enchantments = e.getEnchantsToAdd();
            enchantments.replaceAll((enchantment, level) -> Math.min(level + 1, enchantment.getMaxLevel()));

            sendMessage(p, this.getItemName() + ": §r§aEnchantment boosted!");
            if ((Tag.ITEMS_PICKAXES.isTagged(type)
                || Tag.ITEMS_SHOVELS.isTagged(type)
                || Tag.ITEMS_AXES.isTagged(type)
                || Tag.ITEMS_HOES.isTagged(type))
                && random.nextDouble() < 0.2) {
                enchantments.put(Enchantment.FORTUNE, random.nextInt(3) + 1);
                sendMessage(p, this.getItemName() + ": §r§aFortune added!");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerExperience(PlayerExpChangeEvent e) {
        Player p = e.getPlayer();
        if (random.nextDouble() < 0.2) {
            e.setAmount(e.getAmount() * 2);
            sendMessage(p, this.getItemName() + ": §r§aDouble experience!");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemDamage(PlayerItemDamageEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        ItemMeta itemMeta = item.getItemMeta();

        if (!(itemMeta instanceof Damageable damageable)) {
            return;
        }

        if (random.nextDouble() < 0.20 && damageable.getDamage() >= item.getType().getMaxDurability() - 1) {
            e.setCancelled(true);
            damageable.setDamage(0);
            item.setItemMeta(itemMeta);

            Component displayName = itemMeta.displayName();
            String name = displayName == null
                ? humanizeMaterial(item.getType())
                : PlainTextComponentSerializer.plainText().serialize(displayName);
            sendMessage(p, this.getItemName() + ": §r§aSaved " + name + "§r§a!");
        }
    }

    private String humanizeMaterial(Material material) {
        String[] parts = material.name().toLowerCase().split("_");
        StringBuilder result = new StringBuilder();
        for (String part : parts) {
            if (!result.isEmpty()) {
                result.append(' ');
            }
            result.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return result.toString();
    }
}
