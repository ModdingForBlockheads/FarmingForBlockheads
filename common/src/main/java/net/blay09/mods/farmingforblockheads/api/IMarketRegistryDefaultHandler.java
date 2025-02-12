package net.blay09.mods.farmingforblockheads.api;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public interface IMarketRegistryDefaultHandler {
    @Deprecated
    default void register(@Nullable ItemStack overridePayment) {
        register(overridePayment, null);
    }

    default void register(@Nullable ItemStack overridePayment, @Nullable Integer overrideCount) {
    }

    boolean isEnabledByDefault();

    /**
     * @deprecated Not actually called anymore, instead register will only receive a payment ItemStack if payment has been overridden for this group.
     */
    @Deprecated
    default ItemStack getDefaultPayment() {
        return new ItemStack(Items.EMERALD);
    }
}
