package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.blay09.mods.farmingforblockheads.api.IMarketEntry;
import net.blay09.mods.farmingforblockheads.api.InternalMethods;
import net.blay09.mods.farmingforblockheads.api.IMarketRegistryDefaultHandler;
import net.blay09.mods.farmingforblockheads.registry.MarketCategory;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class InternalMethodsImpl implements InternalMethods {
	@Override
	public IMarketCategory registerMarketCategory(ResourceLocation registryName, String tooltipLangKey, ItemStack icon, int sortIndex) {
		MarketCategory category = new MarketCategory(registryName, tooltipLangKey, icon, sortIndex);
		MarketRegistry.INSTANCE.registerCategory(category);
		return category;
	}

	@Override
	public void registerMarketEntry(ItemStack outputItem, ItemStack costItem, IMarketCategory category) {
		MarketRegistry.INSTANCE.registerEntry(outputItem, costItem, category);
	}

	@Override
	public void registerMarketDefaultHandler(String key, IMarketRegistryDefaultHandler defaultHandler) {
		MarketRegistry.registerDefaultHandler(key, defaultHandler);
	}

	@Override
	@Nullable
	public IMarketCategory getMarketCategory(ResourceLocation registryName) {
		return MarketRegistry.getCategory(registryName);
	}

	@Override
	@Nullable
	public IMarketEntry getMarketEntry(ItemStack outputItem) {
		return MarketRegistry.getEntryFor(outputItem);
	}

	@Override
	public Collection<IMarketEntry> getMarketEntries() {
		return MarketRegistry.getEntries();
	}
}
