package net.blay09.mods.farmingforblockheads.registry.market;

import net.minecraft.world.item.ItemStack;

public class MarketCategoryData {
    private String name;
    private ItemStack icon;
    private int sortIndex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }
}
