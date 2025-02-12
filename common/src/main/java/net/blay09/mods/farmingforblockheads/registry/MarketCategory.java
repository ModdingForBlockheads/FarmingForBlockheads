package net.blay09.mods.farmingforblockheads.registry;

import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class MarketCategory implements IMarketCategory {
	private final ResourceLocation registryName;
	private final String tooltipLangKey;
	private final ItemStack iconStack;
	private int sortIndex;

	public MarketCategory(ResourceLocation registryName, String tooltipLangKey, ItemStack iconStack, int sortIndex) {
		this.registryName = registryName;
		this.tooltipLangKey = tooltipLangKey;
		this.iconStack = iconStack;
		this.sortIndex = sortIndex;
	}

	@Override
	public ResourceLocation getRegistryName() {
		return registryName;
	}

	@Override
	public String getTooltipLangKey() {
		return tooltipLangKey;
	}

	@Override
	public ItemStack getIconStack() {
		return iconStack;
	}

	@Override
	public int getSortIndex() {
		return sortIndex;
	}
}
