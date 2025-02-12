package net.blay09.mods.farmingforblockheads.block;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.farmingforblockheads.block.entity.MarketBlockEntity;
import net.blay09.mods.farmingforblockheads.entity.MerchantEntity;
import net.blay09.mods.farmingforblockheads.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class MarketBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape RENDER_SHAPE = Block.box(0, 0.01, 0, 16, 16, 16);

    public MarketBlock() {
        super(Properties.of().sound(SoundType.WOOD).strength(2f));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MarketBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext useContext) {
        return defaultBlockState().setValue(FACING, useContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        Direction facing = state.getValue(FACING);
        BlockPos entityPos = pos.relative(facing.getOpposite());

        MerchantEntity.SpawnAnimationType spawnAnimationType = MerchantEntity.SpawnAnimationType.MAGIC;
        if (level.canSeeSkyFromBelowWater(entityPos)) {
            spawnAnimationType = MerchantEntity.SpawnAnimationType.FALLING;
        } else if (!level.isEmptyBlock(entityPos.below())) {
            spawnAnimationType = MerchantEntity.SpawnAnimationType.DIGGING;
        }

        if (!level.isClientSide) {
            MerchantEntity merchant = new MerchantEntity(ModEntities.merchant.get(), level);
            merchant.setMarket(pos, facing);
            merchant.setToFacingAngle();
            merchant.setSpawnAnimation(spawnAnimationType);

            if (level.canSeeSkyFromBelowWater(entityPos)) {
                merchant.setPos(entityPos.getX() + 0.5, entityPos.getY() + 172, entityPos.getZ() + 0.5);
            } else if (!level.isEmptyBlock(entityPos.below())) {
                merchant.setPos(entityPos.getX() + 0.5, entityPos.getY(), entityPos.getZ() + 0.5);
            } else {
                merchant.setPos(entityPos.getX() + 0.5, entityPos.getY(), entityPos.getZ() + 0.5);
            }

            level.addFreshEntity(merchant);
            merchant.finalizeSpawn(((ServerLevel) level), level.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null, null);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        if (!level.isClientSide) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof MarketBlockEntity market) {
                Balm.getNetworking().openGui(player, market);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        Direction facing = state.getValue(FACING);
        return state.setValue(FACING, mirror.getRotation(facing).rotate(facing));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return RENDER_SHAPE;
    }
}
