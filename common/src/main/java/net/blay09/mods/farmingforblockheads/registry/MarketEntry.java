package net.blay09.mods.farmingforblockheads.registry;

import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.blay09.mods.farmingforblockheads.api.IMarketEntry;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class MarketEntry implements IMarketEntry {

	private final UUID entryId;
	private final ItemStack outputItem;
	private final ItemStack costItem;
	private final IMarketCategory category;

	public MarketEntry(UUID entryId, ItemStack outputItem, ItemStack costItem, IMarketCategory category) {
		this.entryId = entryId;
		this.outputItem = outputItem;
		this.costItem = costItem;
		this.category = category;
	}

	@Override
	public UUID getEntryId() {
		return entryId;
	}

	@Override
	public ItemStack getCostItem() {
		return costItem;
	}

	@Override
	public ItemStack getOutputItem() {
		return outputItem;
	}

	@Override
	public IMarketCategory getCategory() {
		return category;
	}

}
