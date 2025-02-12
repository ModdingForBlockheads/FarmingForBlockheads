package net.blay09.mods.farmingforblockheads.network;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class ChickenNestEffectMessage {
    private final BlockPos pos;

    public ChickenNestEffectMessage(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(ChickenNestEffectMessage message, FriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
    }

    public static ChickenNestEffectMessage decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        return new ChickenNestEffectMessage(pos);
    }

    public static void handle(Player player, ChickenNestEffectMessage message) {
        player.level().addParticle(ParticleTypes.EXPLOSION, message.pos.getX() + 0.5f, message.pos.getY() + 0.5f, message.pos.getZ() + 0.5f, 0, 0, 0);
    }

}
