package net.blay09.mods.farmingforblockheads.api;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface IMarketOverrideData {

    boolean isEnabled();

    @Nullable
    ItemStack getPayment();

    int getCount();
}
