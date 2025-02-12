package net.blay09.mods.farmingforblockheads.menu;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.DefaultContainer;
import net.blay09.mods.farmingforblockheads.api.IMarketEntry;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.blay09.mods.farmingforblockheads.network.MarketListMessage;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MarketMenu extends AbstractContainerMenu {

    private final Player player;
    private final BlockPos pos;
    private final DefaultContainer marketInputBuffer = new DefaultContainer(1);
    private final DefaultContainer marketOutputBuffer = new DefaultContainer(1);
    protected final List<MarketFakeSlot> marketSlots = new ArrayList<>();
    private final int playerInventoryStart;
    private final MarketPaymentSlot paymentSlot;
    protected final DataSlot canBuy = DataSlot.standalone();

    private boolean sentItemList;
    protected IMarketEntry selectedEntry;

    public MarketMenu(int windowId, Inventory playerInventory, BlockPos pos) {
        super(ModMenus.market.get(), windowId);
        this.player = playerInventory.player;
        this.pos = pos;

        addSlot(paymentSlot = new MarketPaymentSlot(marketInputBuffer, 0, 23, 39));
        addSlot(new MarketBuySlot(this, marketOutputBuffer, 0, 61, 39));

        Container fakeInventory = new DefaultContainer(4 * 3);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                MarketFakeSlot slot = new MarketFakeSlot(fakeInventory, j + i * 3, 102 + j * 18, 11 + i * 18);
                marketSlots.add(slot);
                addSlot(slot);
            }
        }

        playerInventoryStart = slots.size();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 92 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 150));
        }

        canBuy.set(1);
        addDataSlot(canBuy);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();
            if (index == 1) {
                if (!isReadyToBuy()) {
                    return ItemStack.EMPTY;
                }
                if (!moveItemStackTo(slotStack, 14, 50, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, itemStack);
            } else if (index == 0) {
                if (!moveItemStackTo(slotStack, 14, 50, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (isPaymentItem(slotStack)) {
                if (!moveItemStackTo(slotStack, 0, 1, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 41 && index < 50) {
                if (!moveItemStackTo(slotStack, 14, 41, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 14 && index < 41) {
                if (!moveItemStackTo(slotStack, 41, 50, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }
        return itemStack;
    }

    private boolean isPaymentItem(ItemStack itemStack) {
        return (selectedEntry == null && itemStack.getItem() == Items.EMERALD)
                || (selectedEntry != null && ItemStack.isSameItemSameTags(selectedEntry.getCostItem(), itemStack));
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        if (!player.level().isClientSide && !sentItemList) {
            Balm.getNetworking().sendTo(player, new MarketListMessage(MarketRegistry.getGroupedEntries()));
            sentItemList = true;
        }
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide) {
            ItemStack itemStack = this.marketInputBuffer.removeItemNoUpdate(0);
            if (!itemStack.isEmpty() && !player.addItem(itemStack)) {
                player.drop(itemStack, false);
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return player.level().getBlockState(pos).getBlock() == ModBlocks.market && player.distanceToSqr(pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5) <= 64;
    }

    @Override
    public void slotsChanged(Container container) {
        if (selectedEntry != null) {
            marketOutputBuffer.setItem(0, selectedEntry.getOutputItem().copy());
        } else {
            marketOutputBuffer.setItem(0, ItemStack.EMPTY);
        }
        canBuy.set(isValidPaymentProvided() ? 1 : 0);
    }

    public void selectMarketEntry(UUID entryId, boolean stack) {
        selectedEntry = MarketRegistry.getEntryById(entryId);
        if (selectedEntry != null) {
            ItemStack costItem = selectedEntry.getCostItem();
            ItemStack currentInput = marketInputBuffer.getItem(0);
            if (!ItemStack.isSameItem(costItem, currentInput)) {
                quickMoveStack(player, 0);
                quickMoveCost(costItem, stack ? 64 : 1);
            } else if (stack && currentInput.getCount() < 64) {
                quickMoveCost(costItem, 64);
            } else if(!stack && currentInput.getCount() > 1) {
                quickMoveStack(player, 0);
                quickMoveCost(costItem, 1);
            }
        }
        slotsChanged(marketInputBuffer);
    }

    private void quickMoveCost(ItemStack costItem, int desiredCount) {
        paymentSlot.setMaxStackSizeOverride(desiredCount);
        for (int i = playerInventoryStart; i < slots.size(); i++) {
            ItemStack slotStack = slots.get(i).getItem();
            if (ItemStack.isSameItem(slotStack, costItem)) {
                quickMoveStack(player, i);
            }

            if (paymentSlot.getItem().getCount() >= desiredCount) {
                break;
            }
        }
        paymentSlot.resetMaxStackSizeOverride();
    }

    @Nullable
    public IMarketEntry getSelectedEntry() {
        return selectedEntry;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack itemStack, Slot slot) {
        return slot.container != this.marketOutputBuffer && super.canTakeItemForPickAll(itemStack, slot);
    }

    public boolean isReadyToBuy() {
        return isValidPaymentProvided();
    }

    public boolean isValidPaymentProvided() {
        ItemStack payment = marketInputBuffer.getItem(0);
        return selectedEntry != null && !payment.isEmpty() && isPaymentItem(payment) && payment.getCount() >= selectedEntry.getCostItem().getCount();
    }

    public void onItemBought() {
        if (selectedEntry != null) {
            marketInputBuffer.removeItem(0, selectedEntry.getCostItem().getCount());
            slotsChanged(marketInputBuffer);
        }
    }

}
