package net.blay09.mods.farmingforblockheads.network;

import com.google.common.collect.ArrayListMultimap;
import net.blay09.mods.farmingforblockheads.api.FarmingForBlockheadsAPI;
import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.blay09.mods.farmingforblockheads.api.IMarketEntry;
import net.blay09.mods.farmingforblockheads.menu.MarketClientMenu;
import net.blay09.mods.farmingforblockheads.registry.MarketEntry;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.UUID;

public class MarketListMessage {

    private final ArrayListMultimap<IMarketCategory, IMarketEntry> entryMap;

    public MarketListMessage(ArrayListMultimap<IMarketCategory, IMarketEntry> entryMap) {
        this.entryMap = entryMap;
    }

    public static MarketListMessage decode(FriendlyByteBuf buf) {
        ArrayListMultimap<IMarketCategory, IMarketEntry> entryMap = ArrayListMultimap.create();
        int categoryCount = buf.readByte();
        MarketRegistry.resetCategories();
        for (int i = 0; i < categoryCount; i++) {
            ResourceLocation categoryId = buf.readResourceLocation();
            String tooltipLangKey = buf.readUtf();
            ItemStack icon = buf.readItem();
            int sortIndex = buf.readByte();
            IMarketCategory category = FarmingForBlockheadsAPI.registerMarketCategoryAndReturn(categoryId, tooltipLangKey, icon, sortIndex);
            int entryCount = buf.readShort();
            for (int j = 0; j < entryCount; j++) {
                entryMap.put(category, readEntry(buf, category));
            }
        }

        return new MarketListMessage(entryMap);
    }

    public static void encode(MarketListMessage message, FriendlyByteBuf buf) {
        buf.writeByte(message.entryMap.keySet().size());
        for (IMarketCategory category : message.entryMap.keySet()) {
            buf.writeResourceLocation(category.getRegistryName());
            buf.writeUtf(category.getTooltipLangKey());
            buf.writeItem(category.getIconStack());
            buf.writeByte(category.getSortIndex());
            List<IMarketEntry> entries = message.entryMap.get(category);
            buf.writeShort(entries.size());
            for (IMarketEntry entry : entries) {
                writeEntry(entry, buf);
            }
        }
    }

    public static void handle(Player player, MarketListMessage message) {
        if (player.containerMenu instanceof MarketClientMenu marketClientMenu) {
            marketClientMenu.setCategoryList(message.entryMap.keySet());
            marketClientMenu.setEntryList(message.entryMap.values());
        }
    }

    private static MarketEntry readEntry(FriendlyByteBuf buf, IMarketCategory category) {
        UUID entryId = buf.readUUID();
        ItemStack outputItem = buf.readItem();
        ItemStack costItem = buf.readItem();
        return new MarketEntry(entryId, outputItem, costItem, category);
    }

    private static void writeEntry(IMarketEntry entry, FriendlyByteBuf buf) {
        buf.writeUUID(entry.getEntryId());
        buf.writeItem(entry.getOutputItem());
        buf.writeItem(entry.getCostItem());
    }

}
