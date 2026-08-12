package me.bunnky.idreamofeasy.slimefun.machines;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotHopperable;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmokingRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
/*
A fast cooking machine that uses electricity to smoke food items.
 */
public class ElectricSmoker extends AContainer implements NotHopperable, RecipeDisplayItem {

    public ElectricSmoker(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    protected void registerDefaultRecipes() {
        for (@NotNull Iterator<Recipe> it = Bukkit.recipeIterator(); it.hasNext(); ) {
            Recipe recipe = it.next();
            if (recipe instanceof SmokingRecipe smokingRecipe) {
                ItemStack input = representativeInput(smokingRecipe.getInputChoice());
                if (input != null) {
                    registerRecipe(10, new ItemStack[]{input}, new ItemStack[]{smokingRecipe.getResult()});
                }
            }
        }
    }

    private ItemStack representativeInput(RecipeChoice choice) {
        if (choice instanceof RecipeChoice.ExactChoice exactChoice && !exactChoice.getChoices().isEmpty()) {
            return exactChoice.getChoices().getFirst().clone();
        }

        for (Material material : Material.values()) {
            if (material.isItem() && material != Material.AIR) {
                ItemStack candidate = new ItemStack(material);
                if (choice.test(candidate)) {
                    return candidate;
                }
            }
        }
        return null;
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.CAMPFIRE);
    }

    @Override
    public @NotNull String getMachineIdentifier() {
        return "IDOE_ELECTRIC_SMOKER";
    }

}
