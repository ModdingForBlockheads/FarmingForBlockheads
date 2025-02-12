package net.blay09.mods.farmingforblockheads.menu;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

public class MarketPaymentSlot extends Slot {

    private int maxStackSizeOverride = 0;

    public MarketPaymentSlot(Container container, int slotId, int x, int y) {
        super(container, slotId, x, y);
    }

    @Override
    public int getMaxStackSize() {
        if (maxStackSizeOverride > 0) {
            return maxStackSizeOverride;
        }

        return super.getMaxStackSize();
    }

    public void setMaxStackSizeOverride(int maxStackSizeOverride) {
        this.maxStackSizeOverride = maxStackSizeOverride;
    }

    public void resetMaxStackSizeOverride() {
        this.maxStackSizeOverride = 0;
    }
}
