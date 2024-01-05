package net.blay09.mods.farmingforblockheads.api;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public interface MarketCategory extends Comparable<MarketCategory> {
	/**
	 * @return the language key for the tooltip of the category button
	 */
	Component tooltip();

	/**
	 * @return the item to use for the category icon
	 */
	ItemStack iconStack();

	int sortIndex();

	@Override
	default int compareTo(MarketCategory o) {
		return Integer.compare(sortIndex(), o.sortIndex());
	}
}
