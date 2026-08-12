package me.bunnky.idreamofeasy.slimefun.items.idols;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

/*
Flameheart Idol: The Flameheart Idol provides a range of powerful abilities, especially in combat and fire-related situations. When the player is in contact with lava or fire, there is a 20% chance to gain Fire Resistance for 10 seconds, protecting the wearer from fire damage. Additionally, when attacked by non-player entities, the Flameheart Idol offers a 20% chance to grant Strength III for 10 seconds, significantly increasing melee damage, and a 30% chance to bestow Regeneration I for 5 seconds, aiding recovery after taking damage. This idol is essential for players who frequently engage in combat and face fire-based hazards.
*/

public class FlameheartIdol extends Idol {

    private final Random random = new Random();

    public FlameheartIdol(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    protected void handleEvent(Event event) {
        if (event instanceof EntityDamageEvent e) {
            if (e.getEntity() instanceof Player p) {
                switch (e.getCause()) {
                    case LAVA:
                    case FIRE:
                        // Talisman of Lava + Fire: Grants fire resistance when in contact with lava/fire.
                        if (random.nextDouble() < 0.2) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 10, 0));
                            sendMessage(p, this.getItemName() + ": §r§a+Fire Resistance");
                        }
                        break;

                    case ENTITY_ATTACK:
                        if (e instanceof EntityDamageByEntityEvent damage) {
                            Entity damager = damage.getDamager();
                            if (!(damager instanceof Player)) {

                                // Talisman of the Warrior: Grants Strength III when attacked by an entity
                                if (random.nextDouble() < 0.2) {
                                    p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 20 * 10, 2));
                                    sendMessage(p, this.getItemName() + ": §r§a+Stength III");
                                }

                                // Talisman of the Knight: 30% chance to get regeneration when attacked by an entity.
                                if (random.nextDouble() < 0.3) {
                                    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 5, 1));
                                    sendMessage(p, this.getItemName() + ": §r§a+Regeneration");
                                }
                                break;
                            }
                        }
                }
            }
        }
    }
}