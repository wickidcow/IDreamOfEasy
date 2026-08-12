package me.bunnky.idreamofeasy.slimefun.items;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import me.bunnky.idreamofeasy.IDreamOfEasy;
import me.bunnky.idreamofeasy.utils.IDOEUtility;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.boat.DarkOakBoat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

/*
A unique vessel designed for navigating lava lakes, allowing you to traverse molten environments efficiently.
 */
public class LavaBoat extends SlimefunItem implements Listener {

    private final NamespacedKey lavaBoatKey;

    public LavaBoat(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        this.lavaBoatKey = new NamespacedKey(IDreamOfEasy.getInstance(), "lava_boat");

        IDreamOfEasy.getInstance().getJavaPlugin().getServer().getPluginManager().registerEvents(this, IDreamOfEasy.getInstance());
        IDOEUtility.setGlow(item);

        addItemHandler((ItemUseHandler) e -> {
            SlimefunItem hand = SlimefunItem.getByItem(e.getItem());
            if (!(hand instanceof LavaBoat)) {
                return;
            }
            e.cancel();

            Player p = e.getPlayer();
            spawnLavaBoat(p);

            if (p.getGameMode() == GameMode.SURVIVAL) {
                e.getItem().subtract(1);
            }
        });
    }

    public void spawnLavaBoat(Player p) {
        DarkOakBoat boat = p.getWorld().spawn(p.getLocation(), DarkOakBoat.class);
        boat.setGlowing(true);
        boat.getPersistentDataContainer().set(lavaBoatKey, PersistentDataType.BYTE, (byte) 1);

        Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = sb.getTeam("LavaBoats");

        if (team == null) {
            team = sb.registerNewTeam("LavaBoats");
            team.color(NamedTextColor.DARK_RED);
        }

        team.addEntry(boat.getUniqueId().toString());
        boat.setFireTicks(0);
    }

    private boolean isLavaBoat(Boat boat) {
        return boat.getPersistentDataContainer().has(lavaBoatKey, PersistentDataType.BYTE);
    }

    private void clearLavaBoatMarker(Boat boat) {
        boat.getPersistentDataContainer().remove(lavaBoatKey);
    }

    @EventHandler
    public void onBoatDestroy(@NotNull VehicleDestroyEvent e) {
        if (e.getVehicle() instanceof Boat boat && isLavaBoat(boat)) {
            if (!(e.getAttacker() instanceof Player)) {
                e.setCancelled(true);
                return;
            }
            e.setCancelled(true);
            clearLavaBoatMarker(boat);
            boat.remove();

            ItemStack sfItem = SlimefunItem.getById("IDOE_LAVABOAT").getItem();
            boat.getWorld().dropItemNaturally(boat.getLocation(), sfItem);
        }
    }

    @EventHandler
    public void onMove(@NotNull VehicleMoveEvent e) {
        if (e.getVehicle() instanceof Boat boat && isLavaBoat(boat)) {
            World w = boat.getWorld();
            Location loc = boat.getLocation();

            if (e.getTo().clone().subtract(0, 0.1, 0).getBlock().getType() == Material.WATER ||
                e.getTo().clone().add(0, 0.1, 0).getBlock().getType() == Material.WATER) {
                clearLavaBoatMarker(boat);
                boat.remove();
                ItemStack sfItem = SlimefunItem.getById("IDOE_LAVABOAT").getItem();
                boat.getWorld().dropItem(boat.getLocation(), sfItem);
                return;
            }

            if (e.getTo().clone().subtract(0, 0.1, 0).getBlock().getType() == Material.LAVA) {
                e.getVehicle().setGravity(false);
                boat.setFireTicks(0);
                Material blockAbove = w.getBlockAt(loc.clone().add(0, 0.1, 0)).getType();
                if (blockAbove == Material.LAVA) {
                    boat.setVelocity(new Vector(boat.getVelocity().getX(), 0.1, boat.getVelocity().getZ()));
                }
            } else {
                e.getVehicle().setGravity(true);
            }
        }
    }

    @EventHandler
    public void onCombust(EntityCombustEvent e) {
        // Only protect an actual dropped Lava Boat item. The old implementation
        // cancelled every combustion event because the registered Slimefun item
        // itself always exists.
        if (e.getEntity() instanceof Item item) {
            SlimefunItem sfItem = SlimefunItem.getByItem(item.getItemStack());
            if (sfItem instanceof LavaBoat) {
                e.setCancelled(true);
                return;
            }
        }

        // Protect players only while they are actually riding a Lava Boat.
        if (e.getEntity() instanceof Player p
            && p.isInsideVehicle()
            && p.getVehicle() instanceof Boat boat
            && isLavaBoat(boat)) {
            e.setCancelled(true);
            p.setFireTicks(0);
            return;
        }

        // Protect the Lava Boat entity and any player passengers.
        if (e.getEntity() instanceof Boat boat && isLavaBoat(boat)) {
            e.setCancelled(true);
            boat.setFireTicks(0);

            for (Entity passenger : boat.getPassengers()) {
                if (passenger instanceof Player) {
                    passenger.setFireTicks(0);
                }
            }
        }
    }

    @EventHandler
    public void onVehicleDamage(@NotNull VehicleDamageEvent e) {
        if (e.getVehicle() instanceof Boat boat && isLavaBoat(boat)) {
            if (!(e.getAttacker() instanceof Player)) {
                e.setCancelled(true);
                e.getVehicle().setFireTicks(0);
            }
        }
    }

    @EventHandler
    public void onDamage(@NotNull EntityDamageEvent e) {
        if (e.getEntity() instanceof Item item) {
            SlimefunItem sfItem = SlimefunItem.getByItem(item.getItemStack());
            if (sfItem instanceof LavaBoat) {
                Vector velocity = e.getEntity().getVelocity();
                e.getEntity().setVelocity(new Vector(velocity.getX(), 0.1, velocity.getZ()));
                e.setCancelled(true);
            }
        }

        if (e.getEntity() instanceof Player p) {
            if (p.getVehicle() instanceof Boat boat && isLavaBoat(boat)) {
                if (e.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                    e.setCancelled(true);
                    p.setFireTicks(0);
                }
            }

            if (p.isInsideVehicle() && p.getVehicle() instanceof Boat boat && isLavaBoat(boat)) {
                if (e.getCause() == EntityDamageEvent.DamageCause.LAVA ||
                    e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
                    e.setCancelled(true);
                    p.setFireTicks(0);
                }
            }
        }
    }

    @EventHandler
    public void onExit(@NotNull VehicleExitEvent e) {
        if (e.getVehicle() instanceof Boat boat && isLavaBoat(boat)) {
            Location boatLocation = e.getVehicle().getLocation();
            Location teleportLocation = boatLocation.clone().add(0, 1, 0);
            e.getExited().teleport(teleportLocation);
            new BukkitRunnable() {
                @Override
                public void run() {
                    e.getExited().setFireTicks(0);
                }
            }.runTaskLater(IDreamOfEasy.getInstance().getJavaPlugin(), 1);
        }
    }
}
