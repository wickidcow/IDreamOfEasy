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
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.bunnky.idreamofeasy.utils.IDOEUtility;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BiomeSearchResult;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/*
A useful tool that points players toward the nearest biome of their choice, aiding exploration.
*/

public class BiomeCompass extends SimpleSlimefunItem<ItemUseHandler> {

    private final ItemSetting<Integer> r = new IntRangeSetting(this, "range", 1, 100, Integer.MAX_VALUE);

    private static final int COOLDOWN_TICKS = 200; // 10 seconds

    private final Registry<Biome> biomeRegistry;
    private final List<Biome> biomes;
    private final Map<UUID, Integer> playerBiomeSelection = new HashMap<>();

    public BiomeCompass(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        addItemSetting(r);
        IDOEUtility.setGlow(item);

        this.biomeRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME);
        this.biomes = biomeRegistry.stream().toList();
    }

    @Override
    public @NotNull ItemUseHandler getItemHandler() {
        return this::onRightClick;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onRightClick(@NotNull PlayerRightClickEvent e) {
        Player p = e.getPlayer();
        UUID playerId = p.getUniqueId();

        int selectedBiomeIndex = playerBiomeSelection.getOrDefault(playerId, 0);

        if (p.hasCooldown(Material.COMPASS)) {
            p.sendMessage("§cYou must wait before using this again!");
            return;
        }

        if (p.isSneaking()) {
            if (e.getInteractEvent().getClickedBlock() == null) {
                selectedBiomeIndex = (selectedBiomeIndex + 1) % biomes.size();
            } else {
                selectedBiomeIndex = (selectedBiomeIndex - 1 + biomes.size()) % biomes.size();
            }

            playerBiomeSelection.put(playerId, selectedBiomeIndex);
            p.sendMessage("§eSelected biome: §6" + getBiomeName(biomes.get(selectedBiomeIndex)));
            return;
        }

        Biome selectedBiome = biomes.get(selectedBiomeIndex);
        Location closestBiomeLocation = findClosestBiome(p.getLocation(), selectedBiome);

        if (closestBiomeLocation != null) {
            p.setCompassTarget(closestBiomeLocation);
            double distance = p.getLocation().distance(closestBiomeLocation);
            p.sendMessage("§aFound §a" + getBiomeName(selectedBiome) + " §abiome, §a"
                + (int) distance + " §ablocks away!");
            p.setCooldown(Material.COMPASS, COOLDOWN_TICKS);
            p.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.5F, 2F);
        } else {
            p.sendMessage("§cNo §4" + getBiomeName(selectedBiome) + " §cbiome nearby.");
            p.setCooldown(Material.COMPASS, COOLDOWN_TICKS);
            p.playSound(p, Sound.BLOCK_ANVIL_LAND, SoundCategory.PLAYERS, 0.4F, 1F);
        }

        p.spawnParticle(Particle.EFFECT, p.getLocation().add(0.5, 0.5, 0.5), 30, 0.3, 0.3, 0.3, 0.05);
    }

    private Location findClosestBiome(Location playerLocation, Biome targetBiome) {
        BiomeSearchResult result = playerLocation.getWorld().locateNearestBiome(playerLocation, r.getValue(), targetBiome);
        return result == null ? null : result.getLocation();
    }

    private String getBiomeName(Biome biome) {
        NamespacedKey key = biomeRegistry.getKey(biome);
        if (key == null) {
            return "Unknown";
        }

        StringBuilder name = new StringBuilder();
        for (String part : key.getKey().split("_")) {
            if (!name.isEmpty()) {
                name.append(' ');
            }
            name.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return name.toString();
    }

    public static int getRange() {
        return Slimefun.getItemCfg().getOrSetDefault("IDOE_BIOMECOMPASS.range", 100);
    }
}
