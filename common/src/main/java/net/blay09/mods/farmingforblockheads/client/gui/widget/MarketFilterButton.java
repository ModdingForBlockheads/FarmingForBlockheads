package net.blay09.mods.farmingforblockheads.client.gui.widget;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.api.MarketCategory;
import net.blay09.mods.farmingforblockheads.menu.MarketMenu;
import net.blay09.mods.farmingforblockheads.registry.SimpleHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class MarketFilterButton extends Button {

    private static final ResourceLocation ICONS = ResourceLocation.fromNamespaceAndPath(FarmingForBlockheads.MOD_ID, "textures/gui/market.png");

    private final MarketMenu menu;
    private final SimpleHolder<MarketCategory> category;

    public MarketFilterButton(int x, int y, MarketMenu menu, SimpleHolder<MarketCategory> category, OnPress pressable) {
        super(x, y, 20, 20, Component.empty(), pressable, Button.DEFAULT_NARRATION);
        this.menu = menu;
        this.category = category;
        setTooltip(Tooltip.create(category.value().tooltip()));
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;

        int texY = 14;
        if (menu.getCurrentCategory().map(it -> !it.equals(category)).orElse(false)) {
            texY += 40;
        } else if (isHovered) {
            texY += 20;
        }
        guiGraphics.setColor(1f, 1f, 1f, 1f);
        guiGraphics.blit(ICONS, getX(), getY(), 176, texY, width, height);

        guiGraphics.renderItem(category.value().iconStack(), getX() + 2, getY() + 2);
    }

    public SimpleHolder<MarketCategory> getCategory() {
        return category;
    }

}
