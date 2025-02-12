package net.blay09.mods.farmingforblockheads.menu;

import net.blay09.mods.farmingforblockheads.recipe.MarketRecipe;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

public class MarketListingSlot extends FakeSlot {

    private RecipeHolder<MarketRecipe> recipe;

    public MarketListingSlot(Container container, int slotId, int x, int y) {
        super(container, slotId, x, y);
    }

    @Override
    public ItemStack getItem() {
        return recipe != null ? recipe.value().getIcon() : ItemStack.EMPTY;
    }

    @Override
    public boolean hasItem() {
        return recipe != null;
    }

    @Override
    public boolean isActive() {
        return recipe != null;
    }

    public void setRecipe(@Nullable RecipeHolder<MarketRecipe> recipe) {
        this.recipe = recipe;
    }

    @Nullable
    public RecipeHolder<MarketRecipe> getRecipe() {
        return recipe;
    }

}
