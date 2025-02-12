package net.blay09.mods.farmingforblockheads.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.blay09.mods.farmingforblockheads.api.IMarketEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

import static net.blay09.mods.farmingforblockheads.FarmingForBlockheads.MOD_ID;
import static net.blay09.mods.farmingforblockheads.compat.emi.EmiIntegration.BACKGROUND;
import static net.blay09.mods.farmingforblockheads.compat.emi.EmiIntegration.TRADE_ICON;

public class MarketEmiRecipe implements EmiRecipe {
    private final UUID uuid;
    private final ResourceLocation id;
    private final List<EmiStack> input;
    private final List<EmiStack> output;
    public MarketEmiRecipe(IMarketEntry marketEntry) {
        this.uuid = marketEntry.getEntryId();
        this.id = new ResourceLocation(MOD_ID, "/market/%s".formatted(marketEntry.getEntryId().toString()));
        this.input = List.of(EmiStack.of(marketEntry.getCostItem()));
        this.output = List.of(EmiStack.of(marketEntry.getOutputItem()));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EmiIntegration.MARKET_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(EmiIngredient.of(input));
    }

    @Override
    public List<EmiStack> getOutputs() {
        return output;
    }

    @Override
    public int getDisplayWidth() {
        return 86;
    }

    @Override
    public int getDisplayHeight() {
        return 48;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BACKGROUND, 0, 0);
        widgets.addSlot(this.input.get(0), 15, 12);
        widgets.addSlot(this.output.get(0), 53, 12).recipeContext(this);
        widgets.addTexture(TRADE_ICON, 35, 13);
        Component costText = getFormattedCostString();
        Font fontRenderer = Minecraft.getInstance().font;
        int stringWidth = fontRenderer.width(costText);
        widgets.addText(costText, (int) (42 - stringWidth / 2f), 35, 0xFFFFFF, true);
    }

    private Component getFormattedCostString() {
        final MutableComponent result = Component.translatable("gui.farmingforblockheads:market.cost",
                this.input.get(0).getAmount(),
                this.input.get(0).getItemStack().getDisplayName());
        ChatFormatting color = ChatFormatting.GREEN;
        if (this.input.get(0).getItemStack().getItem() == Items.DIAMOND) {
            color = ChatFormatting.AQUA;
        }
        result.withStyle(color);
        return result;
    }
}
