package me.bunnky.idreamofeasy.slimefun.machines;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.ASlimefunDataContainer;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.handlers.VanillaInventoryDropHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import me.bunnky.idreamofeasy.utils.IDOEUtility;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
/*
A convenient pickup point for players to collect items easily.
 */
public class SupplyHopper extends SimpleSlimefunItem<BlockTicker> implements EnergyNetComponent {

    private final ItemSetting<Boolean> silent = new ItemSetting<>(this, "silent", false);
    private final ItemSetting<Boolean> toggleable = new ItemSetting<>(this, "toggleable-with-redstone", false);

    private final int cap;
    private final int ecost;

    public SupplyHopper(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int ecost, int cap) {
        super(itemGroup, item, recipeType, recipe);
        IDOEUtility.setGlow(item);

        this.cap = cap;
        this.ecost = ecost;

        addItemSetting(silent, toggleable);
        addItemHandler(new VanillaInventoryDropHandler<>(org.bukkit.block.Hopper.class));
    }

    @Override
    public @NotNull BlockTicker getItemHandler() {
        return new BlockTicker() {
            @Override
            public void tick(Block b, SlimefunItem sfItem, ASlimefunDataContainer data) {
                if (b.getType() != Material.HOPPER) {
                    Slimefun.getDatabaseManager().getBlockDataController().removeBlock(b.getLocation());
                    return;
                }

                if (toggleable.getValue()) {
                    Hopper hopper = (Hopper) b.getBlockData();
                    if (!hopper.isEnabled()) {
                        return;
                    }
                }

                org.bukkit.block.Hopper hopperState = (org.bukkit.block.Hopper) b.getState();
                Inventory hopperInventory = hopperState.getInventory();
                if (!hopperInventory.getViewers().isEmpty()) {
                    return;
                }

                Location loc = b.getLocation();
                Collection<Player> players = loc.getNearbyPlayers(2.0, 2.0, 2.0);
                boolean playSound = false;

                for (Player player : players) {
                    if (!Slimefun.getProtectionManager().hasPermission(player, b, Interaction.INTERACT_BLOCK)
                        || player.getGameMode() == GameMode.SPECTATOR) {
                        continue;
                    }

                    Location playerLocation = player.getLocation();
                    double blockCenterX = loc.getX() + 0.5;
                    double blockCenterY = loc.getY() - 1.7;
                    double blockCenterZ = loc.getZ() + 0.5;

                    if (playerLocation.getX() < blockCenterX - 1 || playerLocation.getX() > blockCenterX + 1
                        || playerLocation.getY() < blockCenterY - 1 || playerLocation.getY() > blockCenterY
                        || playerLocation.getZ() < blockCenterZ - 1 || playerLocation.getZ() > blockCenterZ + 1) {
                        continue;
                    }

                    for (ItemStack item : hopperInventory.getContents()) {
                        if (item == null || item.getType().isAir()) {
                            continue;
                        }
                        if (getChargeLong(loc, data) < ecost) {
                            return;
                        }

                        int originalAmount = item.getAmount();
                        Map<Integer, ItemStack> leftover = player.getInventory().addItem(item.clone());
                        int leftoverAmount = leftover.values().stream().mapToInt(ItemStack::getAmount).sum();
                        int moved = originalAmount - leftoverAmount;

                        if (moved <= 0) {
                            continue;
                        }

                        item.setAmount(originalAmount - moved);
                        removeCharge(loc, (long) ecost, data);
                        b.getWorld().spawnParticle(Particle.CRIT, b.getLocation().clone().add(0.5, 0, 0.5), 10, 0.3, 0.3, 0.3, 0.05);
                        playSound = true;
                    }
                }

                if (playSound && !silent.getValue()) {
                    SoundEffect.INFUSED_HOPPER_TELEPORT_SOUND.playAt(b);
                }
            }

            @Override
            public boolean isSynchronized() {
                return true;
            }
        };
    }

    @Override
    public @NotNull EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public long getCapacityLong() {
        return cap;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getCapacity() {
        return cap;
    }
}
