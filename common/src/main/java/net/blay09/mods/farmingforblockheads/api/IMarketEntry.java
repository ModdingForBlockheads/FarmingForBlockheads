package net.blay09.mods.farmingforblockheads.api;

import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public interface IMarketEntry {
	UUID getEntryId();
	ItemStack getOutputItem();
	ItemStack getCostItem();
	IMarketCategory getCategory();
}
