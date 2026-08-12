package me.bunnky.idreamofeasy.slimefun.items.idols;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import me.bunnky.idreamofeasy.utils.IDOEUtility;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

public abstract class Idol extends SlimefunItem {

    private static final List<Idol> idols = new ArrayList<>();
    private boolean messagingEnabled = true;

    public Idol(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        IDOEUtility.setGlow(item);
        idols.add(this);

        addItemHandler((ItemUseHandler) e -> {
            Player p = e.getPlayer();
            messagingEnabled = !messagingEnabled;

            String idolName = this.getItemName();

            if (messagingEnabled) {
                p.sendMessage(IDOEUtility.PREFIX + idolName + " §amessages are now enabled");
            } else {
                p.sendMessage(IDOEUtility.PREFIX + idolName + " §cmessages are now disabled");
            }
        });
    }

    public static List<Idol> getIdols() {
        return idols;
    }

    public boolean trigger(Player p, Event e) {
        if (checkIdol(p)) {
            handleEvent(e);
            return true;
        }
        return false;
    }

    protected abstract void handleEvent(Event e);

    private boolean checkIdol(Player p) {
        PlayerInventory inv = p.getInventory();

        for (ItemStack item : inv.getContents()) {
            if (item != null) {
                SlimefunItem sfItem = SlimefunItem.getByItem(item);
                if (sfItem instanceof Idol idol && idol == this) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void sendMessage(Player p, String message) {
        if (messagingEnabled) {
            p.sendActionBar(Component.text(IDOEUtility.PREFIX + message));
        }
    }
}