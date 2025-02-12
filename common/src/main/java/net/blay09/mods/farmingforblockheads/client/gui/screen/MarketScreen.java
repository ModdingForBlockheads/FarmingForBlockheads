package net.blay09.mods.farmingforblockheads.client.gui.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.balm.mixin.ScreenAccessor;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.blay09.mods.farmingforblockheads.api.IMarketEntry;
import net.blay09.mods.farmingforblockheads.client.gui.widget.MarketFilterButton;
import net.blay09.mods.farmingforblockheads.menu.MarketClientMenu;
import net.blay09.mods.farmingforblockheads.menu.MarketMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.glfw.GLFW;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

// TODO @MouseTweaksIgnore
public class MarketScreen extends AbstractContainerScreen<MarketMenu> {

    private static final int SCROLLBAR_COLOR = 0xFFAAAAAA;
    private static final int SCROLLBAR_Y = 8;
    private static final int SCROLLBAR_WIDTH = 7;
    private static final int SCROLLBAR_HEIGHT = 77;
    private static final int VISIBLE_ROWS = 4;

    private static final ResourceLocation TEXTURE = new ResourceLocation(FarmingForBlockheads.MOD_ID, "textures/gui/market.png");

    private final List<MarketFilterButton> filterButtons = Lists.newArrayList();
    private final MarketClientMenu clientContainer;

    private int scrollBarScaledHeight;
    private int scrollBarXPos;
    private int scrollBarYPos;
    private int currentOffset;

    private int mouseClickY = -1;
    private int indexWhenClicked;
    private int lastNumberOfMoves;

    private EditBox searchBar;

    public MarketScreen(MarketMenu container, Inventory playerInventory, Component displayName) {
        super(container, playerInventory, displayName);
        this.clientContainer = (MarketClientMenu) container;
    }

    @Override
    public void init() {
        imageHeight = 174;
        super.init();

        Font font = Minecraft.getInstance().font;

        searchBar = new EditBox(font, leftPos + imageWidth - 78, topPos - 5, 70, 10, searchBar, Component.empty());
        setInitialFocus(searchBar);
        addRenderableWidget(searchBar);

        updateCategoryFilters();

        recalculateScrollBar();
    }

    private void updateCategoryFilters() {
        for (MarketFilterButton filterButton : filterButtons) {
            ((ScreenAccessor) this).balm_getChildren().remove(filterButton);
            ((ScreenAccessor) this).balm_getRenderables().remove(filterButton);
            ((ScreenAccessor) this).balm_getNarratables().remove(filterButton);
        }
        filterButtons.clear();

        int curY = -80;
        IMarketCategory[] categories = clientContainer.getCategories().stream().sorted().toArray(IMarketCategory[]::new);
        for (IMarketCategory category : categories) {
            MarketFilterButton filterButton = new MarketFilterButton(width / 2 + 87, height / 2 + curY, clientContainer, category, button -> {
                if (clientContainer.getCurrentCategory() == category) {
                    clientContainer.setFilterCategory(null);
                } else {
                    clientContainer.setFilterCategory(category);
                }
                clientContainer.populateMarketSlots();
                setCurrentOffset(currentOffset);
            });

            addRenderableWidget(filterButton);
            filterButtons.add(filterButton);

            curY += 20;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (Math.abs(delta) > 0f) {
            setCurrentOffset(delta > 0 ? currentOffset - 1 : currentOffset + 1);
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button != -1 && mouseClickY != -1) {
            mouseClickY = -1;
            indexWhenClicked = 0;
            lastNumberOfMoves = 0;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 1 && mouseX >= searchBar.getX() && mouseX < searchBar.getX() + searchBar.getWidth() && mouseY >= searchBar.getY() && mouseY < searchBar.getY() + searchBar.getHeight()) {
            searchBar.setValue("");
            clientContainer.search(null);
            clientContainer.populateMarketSlots();
            setCurrentOffset(currentOffset);
            return true;
        } else if (mouseX >= scrollBarXPos && mouseX <= scrollBarXPos + SCROLLBAR_WIDTH && mouseY >= scrollBarYPos && mouseY <= scrollBarYPos + scrollBarScaledHeight) {
            mouseClickY = (int) mouseY;
            indexWhenClicked = currentOffset;
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean charTyped(char c, int keyCode) {
        boolean result = super.charTyped(c, keyCode);

        clientContainer.search(searchBar.getValue());
        clientContainer.populateMarketSlots();
        setCurrentOffset(currentOffset);

        return result;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (searchBar.keyPressed(keyCode, scanCode, modifiers) || searchBar.isFocused()) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                minecraft.player.closeContainer();
            } else {
                clientContainer.search(searchBar.getValue());
                clientContainer.populateMarketSlots();
                setCurrentOffset(currentOffset);
            }

            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        for (MarketFilterButton sortButton : filterButtons) {
            if (sortButton.isMouseOver(mouseX, mouseY) && sortButton.active) {
                guiGraphics.renderTooltip(font, sortButton.getTooltipLines(), Optional.empty(), mouseX, mouseY);
            }
        }

        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        if (clientContainer.isDirty()) {
            updateCategoryFilters();
            recalculateScrollBar();
            clientContainer.setDirty(false);
        }

        Font font = minecraft.font;

        guiGraphics.setColor(1f, 1f, 1f, 1f);
        guiGraphics.blit(TEXTURE, leftPos, topPos - 10, 0, 0, imageWidth, imageHeight + 10);
        if (menu.getSelectedEntry() != null && !menu.isReadyToBuy()) {
            guiGraphics.blit(TEXTURE, leftPos + 43, topPos + 40, 176, 0, 14, 14);
        }

        if (mouseClickY != -1) {
            float pixelsPerFilter = (SCROLLBAR_HEIGHT - scrollBarScaledHeight) / (float) Math.max(1, (int) Math.ceil(clientContainer.getFilteredListCount() / 3f) - VISIBLE_ROWS);
            if (pixelsPerFilter != 0) {
                int numberOfFiltersMoved = (int) ((mouseY - mouseClickY) / pixelsPerFilter);
                if (numberOfFiltersMoved != lastNumberOfMoves) {
                    setCurrentOffset(indexWhenClicked + numberOfFiltersMoved);
                    lastNumberOfMoves = numberOfFiltersMoved;
                }
            }
        }

        guiGraphics.drawString(font, I18n.get("container.farmingforblockheads.market"), leftPos + 10, topPos + 10, 0xFFFFFF, true);

        if (menu.getSelectedEntry() == null) {
            guiGraphics.drawCenteredString(font, I18n.get("gui.farmingforblockheads:market.no_selection"), leftPos + 49, topPos + 65, 0xFFFFFF);
        } else {
            guiGraphics.drawCenteredString(font, getPriceText(menu.getSelectedEntry()), leftPos + 49, topPos + 65, 0xFFFFFF);
        }

        guiGraphics.fill(scrollBarXPos, scrollBarYPos, scrollBarXPos + SCROLLBAR_WIDTH, scrollBarYPos + scrollBarScaledHeight, SCROLLBAR_COLOR);

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int x, int y) {
    }

    public Collection<MarketFilterButton> getFilterButtons() {
        return filterButtons;
    }

    private void recalculateScrollBar() {
        int scrollBarTotalHeight = SCROLLBAR_HEIGHT - 1;
        this.scrollBarScaledHeight = (int) (scrollBarTotalHeight * Math.min(1f, ((float) VISIBLE_ROWS / (Math.ceil(clientContainer.getFilteredListCount() / 3f)))));
        this.scrollBarXPos = leftPos + imageWidth - SCROLLBAR_WIDTH - 9;
        this.scrollBarYPos = topPos + SCROLLBAR_Y + ((scrollBarTotalHeight - scrollBarScaledHeight) * currentOffset / Math.max(1, (int) Math.ceil((clientContainer.getFilteredListCount() / 3f)) - VISIBLE_ROWS));
    }

    private void setCurrentOffset(int currentOffset) {
        this.currentOffset = Math.max(0, Math.min(currentOffset, (int) Math.ceil(clientContainer.getFilteredListCount() / 3f) - VISIBLE_ROWS));

        clientContainer.setScrollOffset(this.currentOffset);

        recalculateScrollBar();
    }

    public static Component getPriceText(IMarketEntry entry) {
        MutableComponent textComponent = Component.translatable("gui.farmingforblockheads:market.cost", entry.getCostItem().getCount(), entry.getCostItem().getDisplayName());
        textComponent.withStyle(getPriceColor(entry));
        return textComponent;
    }

    public static ChatFormatting getPriceColor(IMarketEntry entry) {
        ChatFormatting color = ChatFormatting.GREEN;
        String unlocalizedName = entry.getCostItem().getDescriptionId().toLowerCase(Locale.ENGLISH);
        if (unlocalizedName.contains("diamond")) {
            color = ChatFormatting.AQUA;
        } else if (unlocalizedName.contains("gold")) {
            color = ChatFormatting.YELLOW;
        } else if (unlocalizedName.contains("iron")) {
            color = ChatFormatting.WHITE;
        }
        return color;
    }

}
