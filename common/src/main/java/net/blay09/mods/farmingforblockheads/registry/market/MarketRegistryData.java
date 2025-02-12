package net.blay09.mods.farmingforblockheads.registry.market;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class MarketRegistryData {
    private String modId;

    private boolean silent;
    private MarketGroupData group;
    private Map<String, MarketOverrideData> groupOverrides;
    private Map<String, MarketOverrideData> entryOverrides;
    private Map<String, MarketCategoryData> customCategories;
    private List<MarketEntryData> customEntries;

    @Nullable
    public String getModId() {
        return modId;
    }

    public void setModId(String modId) {
        this.modId = modId;
    }

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    @Nullable
    public MarketGroupData getGroup() {
        return group;
    }

    public void setGroup(MarketGroupData group) {
        this.group = group;
    }

    @Nullable
    public Map<String, MarketOverrideData> getGroupOverrides() {
        return groupOverrides;
    }

    public void setGroupOverrides(Map<String, MarketOverrideData> groupOverrides) {
        this.groupOverrides = groupOverrides;
    }

    @Nullable
    public Map<String, MarketOverrideData> getEntryOverrides() {
        return entryOverrides;
    }

    public void setEntryOverrides(Map<String, MarketOverrideData> entryOverrides) {
        this.entryOverrides = entryOverrides;
    }

    @Nullable
    public List<MarketEntryData> getCustomEntries() {
        return customEntries;
    }

    public void setCustomEntries(List<MarketEntryData> customEntries) {
        this.customEntries = customEntries;
    }

    @Nullable
    public Map<String, MarketCategoryData> getCustomCategories() {
        return customCategories;
    }

    public void setCustomCategories(Map<String, MarketCategoryData> customCategories) {
        this.customCategories = customCategories;
    }
}
