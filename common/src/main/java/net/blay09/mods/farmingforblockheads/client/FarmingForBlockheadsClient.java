package net.blay09.mods.farmingforblockheads.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.event.client.ItemTooltipEvent;
import net.blay09.mods.balm.mixin.AbstractContainerScreenAccessor;
import net.blay09.mods.farmingforblockheads.api.IMarketEntry;
import net.blay09.mods.farmingforblockheads.client.gui.screen.MarketScreen;
import net.blay09.mods.farmingforblockheads.menu.MarketBuySlot;
import net.blay09.mods.farmingforblockheads.menu.MarketFakeSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.inventory.Slot;

public class FarmingForBlockheadsClient {

    public static void initialize() {
        ModScreens.initialize(BalmClient.getScreens());
        ModRenderers.initialize(BalmClient.getRenderers());

        Balm.getEvents().onEvent(ItemTooltipEvent.class, event -> {
            if (Minecraft.getInstance().screen instanceof MarketScreen screen) {
                Slot hoverSlot = ((AbstractContainerScreenAccessor) screen).getHoveredSlot();
                if (hoverSlot != null && event.getItemStack() == hoverSlot.getItem()) {
                    IMarketEntry hoverEntry = null;

                    if (hoverSlot instanceof MarketFakeSlot) {
                        hoverEntry = ((MarketFakeSlot) hoverSlot).getEntry();
                    } else if (hoverSlot instanceof MarketBuySlot) {
                        hoverEntry = screen.getMenu().getSelectedEntry();
                    }

                    if (hoverEntry != null) {
                        event.getToolTip().add(getPriceTooltipText(hoverEntry));
                    }
                }
            }
        });
    }

    private static Component getPriceTooltipText(IMarketEntry entry) {
        MutableComponent result = Component.translatable("gui.farmingforblockheads:market.tooltip_cost", MarketScreen.getPriceText(entry));
        result.withStyle(MarketScreen.getPriceColor(entry));
        return result;
    }
}
