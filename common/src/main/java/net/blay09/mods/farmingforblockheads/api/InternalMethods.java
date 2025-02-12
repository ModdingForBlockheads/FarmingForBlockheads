package net.blay09.mods.farmingforblockheads.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface InternalMethods {
	IMarketCategory registerMarketCategory(ResourceLocation id, String tooltipLangKey, ItemStack icon, int sortIndex);
	void registerMarketEntry(ItemStack outputItem, ItemStack costItem, IMarketCategory category);
	void registerMarketDefaultHandler(String key, IMarketRegistryDefaultHandler defaultHandler);
	@Nullable
	IMarketCategory getMarketCategory(ResourceLocation id);
	@Nullable
	IMarketEntry getMarketEntry(ItemStack outputItem);
	Collection<IMarketEntry> getMarketEntries();
}
