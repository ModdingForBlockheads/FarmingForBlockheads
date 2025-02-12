package net.blay09.mods.farmingforblockheads.item;

import net.blay09.mods.farmingforblockheads.block.FertilizedFarmlandBlock;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FertilizerItem extends Item {

    public enum FertilizerType {
        HEALTHY,
        RICH,
        STABLE;

        public boolean canFertilize(BlockState state) {
            Block sourceBlock = state.getBlock();
            return sourceBlock == Blocks.FARMLAND || sourceBlock instanceof FertilizedFarmlandBlock;
        }

        public BlockState applyFertilizer(BlockState state) {
            if (!canFertilize(state)) {
                return state;
            }

            int moisture = state.getValue(BlockStateProperties.MOISTURE);
            List<FertilizedFarmlandBlock.FarmlandTrait> traits = new ArrayList<>();
            traits.add(getFarmlandTrait());
            Block sourceBlock = state.getBlock();
            if (sourceBlock instanceof FertilizedFarmlandBlock) {
                traits.addAll(((FertilizedFarmlandBlock) sourceBlock).getTraits());
            }

            Block targetBlock = getBlockForTraits(traits);
            if (targetBlock == null) {
                return state;
            }

            BlockState newState = targetBlock.defaultBlockState();
            return newState.setValue(FertilizedFarmlandBlock.MOISTURE, moisture);
        }

        private FertilizedFarmlandBlock.FarmlandTrait getFarmlandTrait() {
            return switch (this) {
                case HEALTHY -> new FertilizedFarmlandBlock.FarmlandHealthyTrait();
                case RICH -> new FertilizedFarmlandBlock.FarmlandRichTrait();
                case STABLE -> new FertilizedFarmlandBlock.FarmlandStableTrait();
            };

        }

        @Nullable
        private static Block getBlockForTraits(List<FertilizedFarmlandBlock.FarmlandTrait> traits) {
            boolean hasStableTrait = traits.stream().anyMatch(it -> it instanceof FertilizedFarmlandBlock.FarmlandStableTrait);
            boolean hasHealthyTrait = traits.stream().anyMatch(it -> it instanceof FertilizedFarmlandBlock.FarmlandHealthyTrait);
            boolean hasRichTrait = traits.stream().anyMatch(it -> it instanceof FertilizedFarmlandBlock.FarmlandRichTrait);
            if (hasStableTrait && !hasRichTrait && !hasHealthyTrait) {
                return ModBlocks.fertilizedFarmlandStable;
            }

            if (!hasStableTrait && hasRichTrait && !hasHealthyTrait) {
                return ModBlocks.fertilizedFarmlandRich;
            }

            if (!hasStableTrait && !hasRichTrait && hasHealthyTrait) {
                return ModBlocks.fertilizedFarmlandHealthy;
            }

            if (hasStableTrait && hasRichTrait && !hasHealthyTrait) {
                return ModBlocks.fertilizedFarmlandRichStable;
            }

            //noinspection ConstantConditions
            if (hasStableTrait && !hasRichTrait && hasHealthyTrait) {
                return ModBlocks.fertilizedFarmlandHealthyStable;
            }

            return null;
        }
    }

    private final FertilizerType fertilizerType;

    public FertilizerItem(FertilizerType fertilizerType) {
        super(new Item.Properties());
        this.fertilizerType = fertilizerType;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack heldItem = context.getItemInHand();
        if (tryApplyFertilizerAt(level, pos) || tryApplyFertilizerAt(level, pos.below())) {
            if (!player.getAbilities().instabuild) {
                heldItem.shrink(1);
            }

            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }

    private boolean tryApplyFertilizerAt(Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        BlockState newState = fertilizerType.applyFertilizer(state);
        if (newState != state) {
            world.setBlockAndUpdate(pos, newState);
            return true;
        }

        return false;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        MutableComponent chatComponent = Component.translatable("tooltip.farmingforblockheads:fertilizer_" + fertilizerType.name().toLowerCase(Locale.ENGLISH));
        chatComponent.withStyle(fertilizerType.getFarmlandTrait().getTraitColor());
        tooltip.add(chatComponent);
    }

}
