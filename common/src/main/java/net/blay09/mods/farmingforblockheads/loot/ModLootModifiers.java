package net.blay09.mods.farmingforblockheads.loot;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.FarmlandHandler;
import net.blay09.mods.farmingforblockheads.block.FertilizedFarmlandBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class ModLootModifiers {
    public static void initialize(BalmLootTables lootTables) {
        lootTables.registerLootModifier(new ResourceLocation(FarmingForBlockheads.MOD_ID, "rich_farmland"), (context, loot) -> {
            Level level = context.getLevel();
            Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
            BlockState plant = context.getParamOrNull(LootContextParams.BLOCK_STATE);
            if (origin == null || plant == null || !(plant.getBlock() instanceof BonemealableBlock)) {
                return;
            }

            BlockPos pos = BlockPos.containing(origin);
            // Other mods might trigger loot tables during world gen, which results in a deadlock when reading the
            // block state below as the world in the context is the server world, not the world gen region
            ChunkPos chunkPos = new ChunkPos(pos);
            if (!level.getChunkSource().hasChunk(chunkPos.x, chunkPos.z)) {
                return;
            }

            if (plant.getBlock() instanceof CropBlock crop && !crop.isMaxAge(plant)) {
                return;
            }

            BlockPos posBelow = pos.below();
            BlockState farmland = level.getBlockState(posBelow);
            if (farmland.getBlock() instanceof FertilizedFarmlandBlock) {
                if (Math.random() <= ((FertilizedFarmlandBlock) farmland.getBlock()).getBonusCropChance()) {
                    loot.stream().filter(p -> !isProbablySeed(p)).findAny().ifPresent(c -> {
                        loot.add(c.copy());
                        level.levelEvent(2005, pos, 0);
                        FarmlandHandler.rollRegression(level, posBelow, farmland);
                    });
                }
            }
        });
    }

    private static boolean isProbablySeed(ItemStack itemStack) {
        ResourceLocation registryName = Balm.getRegistries().getKey(itemStack.getItem());
        return registryName != null && registryName.getPath().contains("seed");
    }
}
