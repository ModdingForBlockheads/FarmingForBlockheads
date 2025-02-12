package net.blay09.mods.farmingforblockheads.menu;

import net.blay09.mods.farmingforblockheads.api.IMarketEntry;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MarketBuySlot extends Slot {

    private final MarketMenu menu;

    public MarketBuySlot(MarketMenu menu, Container container, int index, int xPosition, int yPosition) {
        super(container, index, xPosition, yPosition);
        this.menu = menu;
    }

    @Override
    public boolean mayPickup(Player player) {
        return menu.isReadyToBuy();
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        IMarketEntry entry = menu.getSelectedEntry();
        if (entry != null) {
            menu.onItemBought();
        }
    }

}
