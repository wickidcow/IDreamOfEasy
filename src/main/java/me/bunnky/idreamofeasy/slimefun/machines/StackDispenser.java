package me.bunnky.idreamofeasy.slimefun.machines;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockDispenseHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.handlers.VanillaInventoryDropHandler;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import me.bunnky.idreamofeasy.IDreamOfEasy;
import me.bunnky.idreamofeasy.utils.IDOEUtility;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;
/*
A dispenser that automatically releases items in stacks, providing a more efficient way to distribute items.
 */
public class StackDispenser extends SlimefunItem {

    public StackDispenser(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        IDOEUtility.setGlow(item);

        addItemHandler(onPlace(), onBlockDispense());
        addItemHandler(new VanillaInventoryDropHandler<>(Dispenser.class));
    }

    @Nonnull
    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@NotNull BlockPlaceEvent e) {
                Player p = e.getPlayer();
                Block b = e.getBlock();
                StorageCacheUtils.setData(b.getLocation(), "owner", p.getUniqueId().toString());
            }
        };
    }

    @Nonnull
    private BlockDispenseHandler onBlockDispense() {
        return (e, dispenser, facedBlock, machine) -> {
            // This machine owns the dispense operation. Letting vanilla dispense one
            // item first made it impossible to reliably identify the source stack.
            e.setCancelled(true);

            if (!hasPermission(dispenser, facedBlock) || !dispenser.getInventory().getViewers().isEmpty()) {
                return;
            }

            // Keep the historical safety rule: do not consume a stack when the
            // block directly in front of the dispenser is occupied.
            if (!facedBlock.isEmpty()) {
                return;
            }

            Location dispenserLocation = dispenser.getLocation();
            Location dropLocation = facedBlock.getLocation().add(0.5, 0.5, 0.5);

            Bukkit.getRegionScheduler().runDelayed(
                IDreamOfEasy.getInstance(),
                dispenserLocation,
                ignored -> dispenseOneStack(dispenserLocation, dropLocation),
                1L
            );
        };
    }

    private void dispenseOneStack(Location dispenserLocation, Location dropLocation) {
        Block block = dispenserLocation.getBlock();
        if (!(block.getState() instanceof Dispenser currentDispenser)) {
            return;
        }

        Inventory inventory = currentDispenser.getInventory();
        if (!inventory.getViewers().isEmpty()) {
            return;
        }

        for (int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack stored = inventory.getItem(slot);
            if (stored == null || stored.getType().isAir() || stored.getAmount() <= 0) {
                continue;
            }

            int amount = Math.min(stored.getAmount(), stored.getMaxStackSize());
            ItemStack output = stored.clone();
            output.setAmount(amount);
            currentDispenser.getWorld().dropItem(dropLocation, output);

            int remaining = stored.getAmount() - amount;
            if (remaining <= 0) {
                inventory.setItem(slot, null);
            } else {
                stored.setAmount(remaining);
                inventory.setItem(slot, stored);
            }
            return;
        }
    }

    @ParametersAreNonnullByDefault
    private boolean hasPermission(Dispenser dispenser, Block target) {
        String owner = StorageCacheUtils.getData(dispenser.getLocation(), "owner");
        if (owner == null) {
            return true;
        }

        try {
            OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(owner));
            return Slimefun.getProtectionManager().hasPermission(player, target, Interaction.PLACE_BLOCK);
        } catch (IllegalArgumentException ignored) {
            // Invalid legacy owner data should fail closed rather than crash the event.
            return false;
        }
    }
}
