package me.bunnky.idreamofeasy.slimefun.items;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.settings.IntRangeSetting;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.bunnky.idreamofeasy.utils.IDOEUtility;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
A useful tool that points players toward the nearest biome of their choice, aiding exploration.
*/

public class BiomeCompass extends SimpleSlimefunItem<ItemUseHandler> {

    private final ItemSetting<Integer> r = new IntRangeSetting(this, "range", 1, 100, Integer.MAX_VALUE);

    private static final int COOLDOWN_TICKS = 200; // 10 Seconds
    private final Biome[] biomes;

    private Map<Player, Integer> playerBiomeSelection = new HashMap<>();
    private Map<Player, Map<Biome, List<Location>>> playerBiomeCache = new HashMap<>();
    private Map<Player, Map<Biome, Location>> playerDiscoveredBiomes = new HashMap<>();

    public BiomeCompass(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        addItemSetting(r);
        IDOEUtility.setGlow(item);

        this.biomes = Biome.values();
    }

    @Override
    public @NotNull ItemUseHandler getItemHandler() {
        return this::onRightClick;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onRightClick(@NotNull PlayerRightClickEvent e) {
        Player p = e.getPlayer();

        int selectedBiomeIndex = playerBiomeSelection.getOrDefault(p, 0);

        if (p.hasCooldown(Material.COMPASS)) {
            p.sendMessage("§cYou must wait before using this again!");
            return;
        }

        if (p.isSneaking()) {
            if (e.getInteractEvent().getClickedBlock() == null) {
                selectedBiomeIndex = (selectedBiomeIndex + 1) % biomes.length;
                playerBiomeSelection.put(p, selectedBiomeIndex);
            } else {
                selectedBiomeIndex = (selectedBiomeIndex - 1 + biomes.length) % biomes.length;
                playerBiomeSelection.put(p, selectedBiomeIndex);
            }
            p.sendMessage("§eSelected biome: " + ChatColor.GOLD + biomes[selectedBiomeIndex].name());
            return;
        }

        Location closestBiomeLocation = findClosestBiome(p, p.getLocation(), biomes[selectedBiomeIndex]);
        if (closestBiomeLocation != null) {
            p.setCompassTarget(closestBiomeLocation);
            double distance = p.getLocation().distance(closestBiomeLocation);
            p.sendMessage("§aFound " + ChatColor.GREEN + biomes[selectedBiomeIndex].name() + " §abiome, " + ChatColor.GREEN + (int) distance + " §ablocks away!");
            Map<Biome, Location> playerDiscovered = playerDiscoveredBiomes.computeIfAbsent(p, k -> new HashMap<>());
            playerDiscovered.put(biomes[selectedBiomeIndex], closestBiomeLocation);
            p.setCooldown(Material.COMPASS, COOLDOWN_TICKS);
            p.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.5F, 2F);
        } else {
            p.sendMessage("§cNo " + ChatColor.DARK_RED + biomes[selectedBiomeIndex].name() + " §cbiome nearby.");
            p.setCooldown(Material.COMPASS, COOLDOWN_TICKS);
            p.playSound(p, Sound.BLOCK_ANVIL_LAND, SoundCategory.PLAYERS, 0.4F, 1F);
        }

        p.spawnParticle(Particle.EFFECT, p.getLocation().add(0.5, 0.5, 0.5), 30, 0.3, 0.3, 0.3, 0.05);
    }

    private Location findClosestBiome(Player p, Location playerLocation, Biome targetBiome) {
        List<Location> foundLocations = new ArrayList<>();
        int radius = r.getValue();

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                int biomeX = playerLocation.getBlockX() + x;
                int biomeZ = playerLocation.getBlockZ() + z;
                Biome biome = playerLocation.getWorld().getBiome(biomeX, biomeZ);
                if (biome == targetBiome) {
                    Location foundLocation = new Location(playerLocation.getWorld(), biomeX, playerLocation.getWorld().getHighestBlockYAt(biomeX, biomeZ), biomeZ);
                    foundLocations.add(foundLocation);
                }
            }
        }

        if (!foundLocations.isEmpty()) {
            Location closestNewBiome = foundLocations.get(0);
            Map<Biome, List<Location>> playerCache = playerBiomeCache.computeIfAbsent(p, k -> new HashMap<>());
            playerCache.computeIfAbsent(targetBiome, k -> new ArrayList<>()).add(closestNewBiome);

            return closestNewBiome;
        }

        Map<Biome, List<Location>> playerCache = playerBiomeCache.computeIfAbsent(p, k -> new HashMap<>());
        List<Location> cachedLocations = playerCache.get(targetBiome);

        if (cachedLocations != null) {
            Location closestCached = null;
            double closestCachedDistance = Double.MAX_VALUE;

            for (Location loc : cachedLocations) {
                double distance = loc.distance(playerLocation);
                if (distance < closestCachedDistance) {
                    closestCachedDistance = distance;
                    closestCached = loc;
                }
            }

            return closestCached;
        }
        return null;
    }

    public static int getRange() {
        return Slimefun.getItemCfg().getOrSetDefault("IDOE_BIOMECOMPASS.range", 100);
    }
}