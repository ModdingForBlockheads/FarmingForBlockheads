package net.blay09.mods.farmingforblockheads.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public interface IMarketCategory extends Comparable<IMarketCategory> {
	ResourceLocation getRegistryName();

	/**
	 * @return the language key for the tooltip of the category button
	 */
	String getTooltipLangKey();

	/**
	 * @return the item to use for the category icon
	 */
	ItemStack getIconStack();

	int getSortIndex();

	default boolean passes(IMarketEntry entry) {
		return entry.getCategory() == this;
	}

	@Override
	default int compareTo(IMarketCategory o) {
		return Integer.compare(getSortIndex(), o.getSortIndex());
	}
}
