package me.bunnky.idreamofeasy.slimefun.machines.repellers;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import me.bunnky.idreamofeasy.IDreamOfEasy;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
/*
Generalized devices that prevent specific mobs from spawning in the surrounding area.
 */

public abstract class MobRepeller extends SlimefunItem implements Listener, EnergyNetComponent {

    private final Set<String> repellerChunks = new HashSet<>();
    private final int cap;
    private final int ecost;

    public MobRepeller(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int ecost, int cap) {
        super(itemGroup, item, recipeType, recipe);
        IDreamOfEasy.getInstance().getJavaPlugin().getServer().getPluginManager().registerEvents(this, IDreamOfEasy.getInstance());

        this.cap = cap;
        this.ecost = ecost;

        addItemHandler(new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return false;
            }

            @Override
            public void tick(Block b, SlimefunItem sfItem, Config config) {
                Location loc = b.getLocation();
                World w = loc.getWorld();

                if (w == null) {
                    return;
                }

                if (getCharge(loc) >= ecost) {
                    removeCharge(loc, ecost);

                    Chunk chunk = b.getChunk();
                    String chunkKey = getChunkKey(chunk);
                    if (!repellerChunks.contains(chunkKey)) {
                        repellerChunks.add(chunkKey);
                    }
                } else {
                    Chunk chunk = b.getChunk();
                    String chunkKey = getChunkKey(chunk);
                    if (repellerChunks.contains(chunkKey)) {
                        repellerChunks.remove(chunkKey);
                    }
                }
            }
        });

        addItemHandler(new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@NotNull BlockPlaceEvent blockPlaceEvent) {
                Block b = blockPlaceEvent.getBlock();
                Chunk chunk = b.getChunk();

                String chunkKey = getChunkKey(chunk);
                repellerChunks.add(chunkKey);

                blockPlaceEvent.getPlayer().sendMessage(ChatColor.YELLOW + getRepelledEntityName() + "§es will no longer spawn in this chunk.");
            }
        });

        addItemHandler(new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(@NotNull BlockBreakEvent blockBreakEvent, @NotNull ItemStack itemStack, @NotNull List<ItemStack> list) {
                Block b = blockBreakEvent.getBlock();
                Chunk chunk = b.getChunk();

                String chunkKey = getChunkKey(chunk);
                repellerChunks.remove(chunkKey);

                blockBreakEvent.getPlayer().sendMessage(ChatColor.YELLOW + getRepelledEntityName() + "§es may now spawn in this chunk.");
            }
        });

    }

    protected abstract EntityType getRepelledEntityType();

    protected abstract String getRepelledEntityName();

    private String getChunkKey(Chunk chunk) {
        return chunk.getWorld().getName() + ":" + chunk.getX() + ":" + chunk.getZ();
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        if (e.getEntityType() == getRepelledEntityType()) {
            Chunk chunk = e.getLocation().getChunk();
            String chunkKey = getChunkKey(chunk);

            if (repellerChunks.contains(chunkKey)) {
                e.setCancelled(true);
            }
        }
    }

    @Override
    public @NotNull EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public int getCapacity() {
        return this.cap;
    }
}
