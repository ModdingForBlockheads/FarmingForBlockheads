package net.blay09.mods.farmingforblockheads.network;

import net.blay09.mods.farmingforblockheads.menu.MarketMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.UUID;

public class MarketSelectMessage {

    private final UUID entryId;
    private final boolean stack;

    public MarketSelectMessage(UUID entryId, boolean stack) {
        this.entryId = entryId;
        this.stack = stack;
    }

    public static void encode(MarketSelectMessage message, FriendlyByteBuf buf) {
        buf.writeUUID(message.entryId);
        buf.writeBoolean(message.stack);
    }

    public static MarketSelectMessage decode(FriendlyByteBuf buf) {
        UUID entryId = buf.readUUID();
        boolean stack = buf.readBoolean();
        return new MarketSelectMessage(entryId, stack);
    }

    public static void handle(ServerPlayer player, MarketSelectMessage message) {
        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof MarketMenu marketMenu) {
            marketMenu.selectMarketEntry(message.entryId, message.stack);
        }
    }

}
