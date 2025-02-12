package net.blay09.mods.farmingforblockheads.block;

import net.blay09.mods.balm.api.container.ContainerUtils;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.block.entity.ChickenNestBlockEntity;
import net.blay09.mods.farmingforblockheads.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChickenNestBlock extends BaseEntityBlock {

    public static final String name = "chicken_nest";
    public static final ResourceLocation registryName = new ResourceLocation(FarmingForBlockheads.MOD_ID, name);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 3, 15);

    protected ChickenNestBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.WET_GRASS).strength(1f));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ChickenNestBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ChickenNestBlockEntity chickenNest) {
                Container container = chickenNest.getContainer();
                ItemStack itemStack = ContainerUtils.extractItem(container, 0, 1, false);
                if (!player.getInventory().add(itemStack)) {
                    ContainerUtils.insertItem(container, 0, itemStack, false);
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext useContext) {
        return defaultBlockState().setValue(FACING, useContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && !state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ChickenNestBlockEntity chickenNest) {
                Container container = chickenNest.getContainer();
                for (int i = 0; i < container.getContainerSize(); i++) {
                    ItemStack itemStack = container.getItem(i);
                    if (!itemStack.isEmpty()) {
                        level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, itemStack.copy()));
                    }
                }
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter blockGetter, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.farmingforblockheads:chicken_nest"));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, ModBlockEntities.chickenNest.get(), ChickenNestBlockEntity::serverTick);
    }
}
