package net.blay09.mods.farmingforblockheads.menu;

import net.blay09.mods.farmingforblockheads.api.IMarketEntry;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class MarketFakeSlot extends FakeSlot {

	private IMarketEntry entry;

	public MarketFakeSlot(Container container, int slotId, int x, int y) {
		super(container, slotId, x, y);
	}

	@Override
	public ItemStack getItem() {
		return entry != null ? entry.getOutputItem() : ItemStack.EMPTY;
	}

	@Override
	public boolean hasItem() {
		return entry != null;
	}

	@Override
	public boolean isActive() {
		return entry != null;
	}

	public void setEntry(@Nullable IMarketEntry entry) {
		this.entry = entry;
	}

	@Nullable
	public IMarketEntry getEntry() {
		return entry;
	}

}
