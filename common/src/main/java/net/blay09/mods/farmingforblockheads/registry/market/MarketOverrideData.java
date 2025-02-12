package net.blay09.mods.farmingforblockheads.registry.market;

import net.blay09.mods.farmingforblockheads.api.IMarketOverrideData;
import net.minecraft.world.item.ItemStack;

public class MarketOverrideData implements IMarketOverrideData {
    private boolean enabled = true;
    private ItemStack payment;
    private Integer count = null;

    @Deprecated
    private Integer amount = 1;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public ItemStack getPayment() {
        return payment;
    }

    public void setPayment(ItemStack payment) {
        this.payment = payment;
    }

    @Override
    public int getCount() {
        return count != null ? count : amount;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmount() {
        return amount;
    }
}
