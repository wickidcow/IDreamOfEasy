package me.bunnky.idreamofeasy.slimefun.items.idols;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Trident;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.Random;
/*
Torrent Idol: The Torrent Idol is a mystical talisman that grants its wielder an array of protective and enhancing abilities. With its innate power, players gain Water Breathing when they start to drown, allowing them to explore the depths of oceans and rivers without fear. In addition, this idol boasts a remarkable 75% chance to absorb fall damage, ensuring that adventurers can traverse high cliffs and ledges without injury. When facing projectiles, the Torrent Idol channels the Talisman of the Whirlwind, reflecting incoming projectiles back at their source with a 60% success rate, turning the tide of battle in your favor. Furthermore, when the bearer sprints, they can receive a Speed boost for a short duration, enhancing their agility and allowing for swift escapes or rapid advances. The Torrent Idol embodies the fluidity and adaptability of water itself, providing both safety and speed in every adventure!
*/
public class TorrentIdol extends Idol {

    private final Random random = new Random();

    public TorrentIdol(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    protected void handleEvent(Event event) {
        if (event instanceof EntityDamageEvent e) {
            if (e.getEntity() instanceof Player p) {
                switch (e.getCause()) {
                    case DROWNING:
                        // Talisman of Water: Grants water breathing when starting to drown.
                        if (random.nextDouble() < 0.2) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 200, 1));
                            sendMessage(p, this.getItemName() + ": §r§a+Water Breathing");
                        }
                        break;

                    case FALL:
                        // Talisman of the Angel: 75% chance to prevent fall damage.
                        if (random.nextDouble() < 0.75) {
                            e.setCancelled(true);
                            sendMessage(p, this.getItemName() + ": §r§aFall damage absorbed!");
                        }
                        break;

                    case PROJECTILE:
                        // Talisman of the Whirlwind: Reflects projectiles back at the attacker.
                        if (e instanceof EntityDamageByEntityEvent entity &&
                            entity.getDamager() instanceof Projectile projectile && !(entity.getDamager() instanceof Trident)) {
                            if (random.nextDouble() < 0.6) {
                                returnProjectile(p, projectile);
                                sendMessage(p, this.getItemName() + ": §r§aProjectile reflected!");
                            }
                        }
                        break;
                }
            }
        } else if (event instanceof PlayerToggleSprintEvent e) {
            // Talisman of the Traveller: Triggers an effect when the player starts sprinting.
            Player p = e.getPlayer();
            if (e.isSprinting()) {
                if (!p.hasPotionEffect(PotionEffectType.SPEED)) {
                    if (random.nextDouble() < 0.6) {

                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
                        sendMessage(p, this.getItemName() + ": §r§a+Speed II");
                    }
                }
            }
        }
    }

    private void returnProjectile(@Nonnull Player p, @Nonnull Projectile projectile) {
        Vector direction = p.getEyeLocation().getDirection().multiply(2.0);
        Location loc = p.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ());

        Projectile returnedProjectile = (Projectile) p.getWorld().spawnEntity(loc, projectile.getType());
        returnedProjectile.setShooter(projectile.getShooter());
        returnedProjectile.setVelocity(direction);

        if (projectile instanceof AbstractArrow firedArrow) {
            AbstractArrow returnedArrow = (AbstractArrow) returnedProjectile;

            returnedArrow.setDamage(firedArrow.getDamage());
            returnedArrow.setPickupStatus(firedArrow.getPickupStatus());
            returnedArrow.setPierceLevel(firedArrow.getPierceLevel());
        }

        projectile.remove();
    }
}