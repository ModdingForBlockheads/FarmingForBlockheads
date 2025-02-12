package net.blay09.mods.farmingforblockheads.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FarmBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FarmBlock.class)
public interface FarmBlockAccessor {
    @Invoker
    static boolean callIsNearWater(LevelReader reader, BlockPos pos) {
        throw new AssertionError();
    }

    @Invoker
    static boolean callShouldMaintainFarmland(BlockGetter blockGetter, BlockPos pos) {
        throw new AssertionError();
    }
}
