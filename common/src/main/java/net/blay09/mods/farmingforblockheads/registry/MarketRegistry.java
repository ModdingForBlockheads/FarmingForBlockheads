package net.blay09.mods.farmingforblockheads.registry;

import com.google.common.collect.ArrayListMultimap;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.ContainerUtils;
import net.blay09.mods.farmingforblockheads.api.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MarketRegistry {

    public static final MarketRegistry INSTANCE = new MarketRegistry();

    private final Map<ResourceLocation, IMarketCategory> indexedCategories = new HashMap<>();
    private final ArrayListMultimap<IMarketCategory, IMarketEntry> entries = ArrayListMultimap.create();
    private final Map<UUID, IMarketEntry> entriesById = new HashMap<>();

    private final Map<String, IMarketOverrideData> groupOverrides = new HashMap<>();
    private final Map<String, IMarketOverrideData> entryOverrides = new HashMap<>();
    private final Map<String, IMarketRegistryDefaultHandler> defaultHandlers = new HashMap<>();

    public static void resetCategories() {
        INSTANCE.indexedCategories.clear();
    }

    public void registerCategory(IMarketCategory category) {
        if (indexedCategories.containsKey(category.getRegistryName())) {
            throw new RuntimeException("Attempted to register duplicate market category " + category.getRegistryName());
        }

        indexedCategories.put(category.getRegistryName(), category);
    }

    public void registerGroupOverride(String key, IMarketOverrideData override) {
        groupOverrides.put(key, override);
    }

    public void registerEntryOverride(String key, IMarketOverrideData override) {
        entryOverrides.put(key, override);
    }

    public void registerEntry(ItemStack outputItem, ItemStack costItem, @Nullable IMarketCategory category) {
        String registryName = Objects.toString(Balm.getRegistries().getKey(outputItem.getItem()));
        IMarketOverrideData override = entryOverrides.get(registryName);
        if (category == null) {
            category = determineCategory(outputItem);
        }

        if (override == null || override.isEnabled()) {
            ItemStack payment = override != null && override.getPayment() != null ? override.getPayment() : costItem;
            ItemStack alteredOutputItem = ContainerUtils.copyStackWithSize(outputItem, override != null ? override.getCount() : Math.min(outputItem.getCount(), outputItem.getMaxStackSize()));
            final MarketEntry entry = new MarketEntry(UUID.randomUUID(), alteredOutputItem, payment, category);
            entries.put(category, entry);
            entriesById.put(entry.getEntryId(), entry);
        }
    }

    public void registerDefaults() {
        for (Map.Entry<String, IMarketRegistryDefaultHandler> entry : defaultHandlers.entrySet()) {
            IMarketOverrideData override = groupOverrides.get(entry.getKey());
            IMarketRegistryDefaultHandler defaultHandler = entry.getValue();
            boolean enabled = defaultHandler.isEnabledByDefault();
            if (override != null) {
                enabled = override.isEnabled();
            }

            if (enabled) {
                defaultHandler.register(override != null ? override.getPayment() : null, override != null ? override.getCount() : null);
            }
        }
    }

    @Nullable
    public static IMarketEntry getEntryById(UUID entryId) {
        return INSTANCE.entriesById.get(entryId);
    }

    @Nullable
    public static IMarketEntry getEntryFor(ItemStack outputItem) {
        for (IMarketEntry entry : INSTANCE.entries.values()) {
            if (ItemStack.isSameItemSameTags(entry.getOutputItem(), outputItem) && outputItem.getCount() == entry.getOutputItem().getCount()) {
                return entry;
            }
        }

        return null;
    }

    public static ArrayListMultimap<IMarketCategory, IMarketEntry> getGroupedEntries() {
        return INSTANCE.entries;
    }

    public static Collection<IMarketEntry> getEntries() {
        return INSTANCE.entries.values();
    }

    private static IMarketCategory determineCategory(ItemStack outputStack) {
        IMarketCategory category = FarmingForBlockheadsAPI.getMarketCategoryOther();
        ResourceLocation registryName = Balm.getRegistries().getKey(outputStack.getItem());
        if (registryName != null) {
            if (registryName.getPath().contains("sapling")) {
                category = FarmingForBlockheadsAPI.getMarketCategorySaplings();
            } else if (registryName.getPath().contains("seed")) {
                category = FarmingForBlockheadsAPI.getMarketCategorySeeds();
            } else if (registryName.getPath().contains("flower")) {
                category = FarmingForBlockheadsAPI.getMarketCategoryFlowers();
            }
        }

        return category;
    }

    public static void registerDefaultHandler(String defaultKey, IMarketRegistryDefaultHandler handler) {
        if (INSTANCE.defaultHandlers.containsKey(defaultKey)) {
            throw new RuntimeException("Attempted to register duplicate default handler " + defaultKey);
        }

        INSTANCE.defaultHandlers.put(defaultKey, handler);
    }

    public static Collection<IMarketCategory> getCategories() {
        return INSTANCE.indexedCategories.values();
    }

    @Nullable
    public static IMarketCategory getCategory(ResourceLocation id) {
        return INSTANCE.indexedCategories.get(id);
    }

    public void reset() {
        entryOverrides.clear();
        groupOverrides.clear();
        defaultHandlers.clear();
        indexedCategories.clear();
        entries.clear();
        entriesById.clear();
    }
}
