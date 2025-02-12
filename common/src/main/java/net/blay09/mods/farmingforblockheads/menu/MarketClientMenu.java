package net.blay09.mods.farmingforblockheads.menu;

import com.google.common.collect.Lists;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.blay09.mods.farmingforblockheads.api.IMarketEntry;
import net.blay09.mods.farmingforblockheads.network.MarketSelectMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MarketClientMenu extends MarketMenu {

    private final List<IMarketCategory> categoryList = Lists.newArrayList();
    private final List<IMarketEntry> itemList = Lists.newArrayList();
    private final List<IMarketEntry> filteredItems = Lists.newArrayList();

    private final Comparator<IMarketEntry> comparator = Comparator.comparingInt(o -> o.getCategory().getSortIndex());

    private String currentSearch;
    private IMarketCategory currentCategory;
    private boolean isDirty;
    private int scrollOffset;

    public MarketClientMenu(int windowId, Inventory playerInventory, BlockPos pos) {
        super(windowId, playerInventory, pos);
    }

    @Override
    public void clicked(int slotNumber, int dragType, ClickType clickType, Player player) {
        if (slotNumber >= 0 && slotNumber < slots.size()) {
            Slot slot = slots.get(slotNumber);
            if (player.level().isClientSide) {
                if (slot instanceof MarketFakeSlot marketSlot) {
                    IMarketEntry entry = marketSlot.getEntry();
                    if (entry != null) {
                        selectedEntry = entry;
                        Balm.getNetworking().sendToServer(new MarketSelectMessage(entry.getEntryId(), clickType == ClickType.QUICK_MOVE));
                    }
                }
            }
        }
    }

    public void search(@Nullable String term) {
        this.currentSearch = term;
        applyFilters();
    }

    public void setFilterCategory(@Nullable IMarketCategory category) {
        this.currentCategory = category;
        applyFilters();
    }

    private void applyFilters() {
        this.scrollOffset = 0;
        filteredItems.clear();
        boolean hasSearchFilter = currentSearch != null && !currentSearch.trim().isEmpty();
        if (currentCategory == null && !hasSearchFilter) {
            filteredItems.addAll(itemList);
        } else {
            for (IMarketEntry entry : itemList) {
                String itemName = entry.getOutputItem().getDisplayName().getString();
                if (hasSearchFilter && !itemName.toLowerCase(Locale.ENGLISH).contains(currentSearch.toLowerCase())) {
                    continue;
                }

                if (currentCategory != null && !currentCategory.passes(entry)) {
                    continue;
                }

                filteredItems.add(entry);
            }
        }

        filteredItems.sort(comparator);
    }

    public int getFilteredListCount() {
        return filteredItems.size();
    }

    public void setScrollOffset(int scrollOffset) {
        this.scrollOffset = scrollOffset;
        populateMarketSlots();
    }

    public void populateMarketSlots() {
        int i = scrollOffset * 3;
        for (MarketFakeSlot slot : marketSlots) {
            if (i < filteredItems.size()) {
                slot.setEntry(filteredItems.get(i));
                i++;
            } else {
                slot.setEntry(null);
            }
        }
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }

    public void setCategoryList(Collection<IMarketCategory> categoryList) {
        this.categoryList.clear();
        this.categoryList.addAll(categoryList);

        setDirty(true);
    }

    public void setEntryList(Collection<IMarketEntry> entryList) {
        this.itemList.clear();
        this.itemList.addAll(entryList);

        // Re-apply the filters to populate filteredItems
        applyFilters();

        // Updates the items inside the entry slots
        populateMarketSlots();

        setDirty(true);
    }

    @Override
    public boolean isReadyToBuy() {
        return canBuy.get() == 1;
    }

    @Nullable
    public IMarketCategory getCurrentCategory() {
        return currentCategory;
    }

    public Collection<IMarketCategory> getCategories() {
        return categoryList;
    }
}
