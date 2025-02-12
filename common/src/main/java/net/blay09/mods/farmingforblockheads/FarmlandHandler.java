package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.balm.api.event.CropGrowEvent;
import net.blay09.mods.farmingforblockheads.block.FertilizedFarmlandBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;

public class FarmlandHandler {

    public static void onGrowEvent(CropGrowEvent.Post event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState plant = level.getBlockState(event.getPos());
        if (plant.getBlock() instanceof BonemealableBlock growable) {
            BlockState farmland = level.getBlockState(event.getPos().below());
            if (farmland.getBlock() instanceof FertilizedFarmlandBlock farmlandBlock) {
                if (Math.random() <= farmlandBlock.getDoubleGrowthChance()) {
                    if (growable.isValidBonemealTarget(level, pos, plant, level.isClientSide)) {
                        growable.performBonemeal(((ServerLevel) level), level.getRandom(), pos, plant);
                        level.levelEvent(2005, pos, 0);
                        rollRegression(level, pos.below(), farmland);
                    }
                }
            }
        }
    }

    public static void rollRegression(Level level, BlockPos pos, BlockState farmland) {
        if (farmland.getBlock() instanceof FertilizedFarmlandBlock) {
            if (Math.random() <= ((FertilizedFarmlandBlock) farmland.getBlock()).getRegressionChance()) {
                level.setBlockAndUpdate(pos, Blocks.FARMLAND.defaultBlockState().setValue(FarmBlock.MOISTURE, farmland.getValue(FarmBlock.MOISTURE)));
            }
        }
    }
}
