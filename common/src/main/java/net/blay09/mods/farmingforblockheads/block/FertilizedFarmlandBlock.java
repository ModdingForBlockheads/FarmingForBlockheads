package net.blay09.mods.farmingforblockheads.block;

import com.google.common.collect.Lists;
import net.blay09.mods.balm.api.block.CustomFarmBlock;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheadsConfig;
import net.blay09.mods.farmingforblockheads.mixin.FarmBlockAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class FertilizedFarmlandBlock extends FarmBlock implements CustomFarmBlock {

    public interface FarmlandTrait {
        default float getDoubleGrowthChance() {
            return 0f;
        }

        default float getBonusCropChance() {
            return 0f;
        }

        default boolean isStable() {
            return false;
        }

        String getTraitName();

        ChatFormatting getTraitColor();
    }

    public static class FarmlandHealthyTrait implements FarmlandTrait {
        @Override
        public float getDoubleGrowthChance() {
            return (float) FarmingForBlockheadsConfig.getActive().fertilizerBonusGrowthChance;
        }

        @Override
        public String getTraitName() {
            return "healthy";
        }

        @Override
        public ChatFormatting getTraitColor() {
            return ChatFormatting.DARK_RED;
        }
    }

    public static class FarmlandRichTrait implements FarmlandTrait {
        @Override
        public float getBonusCropChance() {
            return (float) FarmingForBlockheadsConfig.getActive().fertilizerBonusCropChance;
        }

        @Override
        public String getTraitName() {
            return "rich";
        }

        @Override
        public ChatFormatting getTraitColor() {
            return ChatFormatting.GREEN;
        }
    }

    public static class FarmlandStableTrait implements FarmlandTrait {
        @Override
        public boolean isStable() {
            return true;
        }

        @Override
        public String getTraitName() {
            return "stable";
        }

        @Override
        public ChatFormatting getTraitColor() {
            return ChatFormatting.YELLOW;
        }
    }

    private final List<FarmlandTrait> traits;

    public FertilizedFarmlandBlock(FarmlandTrait... traits) {
        super(BlockBehaviour.Properties.of().sound(SoundType.GRAVEL).strength(0.6f).randomTicks());
        this.traits = Lists.newArrayList(traits);
    }

    @Override
    public boolean canSustainPlant(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction, Block block) {
        return true;
    }

    @Override
    public boolean isFertile(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return state.getValue(MOISTURE) > 0;
    }

    public float getDoubleGrowthChance() {
        return (float) traits.stream().mapToDouble(FarmlandTrait::getDoubleGrowthChance).sum();
    }

    public float getBonusCropChance() {
        return (float) traits.stream().mapToDouble(FarmlandTrait::getBonusCropChance).sum();
    }

    public float getRegressionChance() {
        return (float) FarmingForBlockheadsConfig.getActive().fertilizerRegressionChance;
    }

    private boolean isStable() {
        return traits.stream().anyMatch(FarmlandTrait::isStable);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!isStable()) {
            super.fallOn(level, state, pos, entity, fallDistance);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int moisture = state.getValue(MOISTURE);

        if (!FarmBlockAccessor.callIsNearWater(level, pos) && !level.isRainingAt(pos.above())) {
            if (moisture > 0) {
                level.setBlock(pos, state.setValue(MOISTURE, moisture - 1), 2);
            } else if (!FarmBlockAccessor.callShouldMaintainFarmland(level, pos) && traits.stream().noneMatch(FarmlandTrait::isStable)) {
                turnToDirt(null, state, level, pos);
            }
        } else if (moisture < 7) {
            level.setBlock(pos, state.setValue(MOISTURE, 7), 2);
        }
    }

    public Collection<FarmlandTrait> getTraits() {
        return traits;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter blockGetter, List<Component> tooltip, TooltipFlag flag) {
        for (FarmlandTrait trait : traits) {
            MutableComponent traitComponent = Component.translatable("tooltip.farmingforblockheads:trait_" + trait.getTraitName());
            traitComponent.withStyle(trait.getTraitColor());
            tooltip.add(traitComponent);
        }
    }
}
