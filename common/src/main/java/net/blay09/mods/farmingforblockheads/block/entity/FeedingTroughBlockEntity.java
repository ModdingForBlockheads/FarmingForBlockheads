package net.blay09.mods.farmingforblockheads.block.entity;

import com.google.common.collect.ArrayListMultimap;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.container.ContainerUtils;
import net.blay09.mods.balm.api.container.DefaultContainer;
import net.blay09.mods.balm.common.BalmBlockEntity;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheadsConfig;
import net.blay09.mods.farmingforblockheads.api.FeedingTroughEvent;
import net.blay09.mods.farmingforblockheads.network.ChickenNestEffectMessage;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FeedingTroughBlockEntity extends BalmBlockEntity implements BalmContainerProvider {

    private static final int TICK_INTERVAL = 20 * 5;

    private final DefaultContainer container = new DefaultContainer(1) {
        @Override
        public void setChanged() {
            isDirty = true;
            FeedingTroughBlockEntity.this.setChanged();
        }
    };

    private int tickTimer;
    private boolean isDirty;

    public FeedingTroughBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.feedingTrough.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, FeedingTroughBlockEntity blockEntity) {
        blockEntity.serverTick(level, pos, state);
    }

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        tickTimer++;
        if (tickTimer >= TICK_INTERVAL) {
            teehee();
            tickTimer = 0;
        }

        if (isDirty) {
            sync();
            isDirty = false;
        }
    }

    @Override
    public void load(CompoundTag tagCompound) {
        super.load(tagCompound);
        container.deserialize(tagCompound.getCompound("ItemHandler"));
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.put("ItemHandler", container.serialize());
    }

    @Override
    public void writeUpdateTag(CompoundTag tag) {
        saveAdditional(tag);
    }

    private void teehee() {
        ItemStack itemStack = container.getItem(0);
        if (itemStack.isEmpty() || itemStack.getCount() < 2) {
            return;
        }

        final float range = FarmingForBlockheadsConfig.getActive().feedingTroughRange;
        AABB aabb = new AABB(worldPosition.getX() - range,
                worldPosition.getY() - range,
                worldPosition.getZ() - range,
                worldPosition.getX() + range,
                worldPosition.getY() + range,
                worldPosition.getZ() + range);
        List<Animal> entities = level.getEntitiesOfClass(Animal.class, aabb);
        if (entities.isEmpty()) {
            return;
        }

        ArrayListMultimap<Class<? extends Animal>, Animal> map = ArrayListMultimap.create();
        for (Animal animal : entities) {
            map.put(animal.getClass(), animal);
        }

        List<Class<? extends Animal>> keys = new ArrayList<>(map.keySet());
        Collections.shuffle(keys);

        for (Class<? extends Animal> key : keys) {
            List<Animal> list = map.get(key);
            if (list.size() < FarmingForBlockheadsConfig.getActive().feedingTroughMaxAnimals) {
                if (list.stream().filter(p -> isSubmissiveAndBreedable(p, itemStack)).count() >= 2) {
                    for (Animal entity : list) {
                        if ( isSubmissiveAndBreedable(entity, itemStack)) {
                            FeedingTroughEvent event = new FeedingTroughEvent(this, entity, itemStack);
                            Balm.getEvents().fireEvent(event);
                            if (!event.isCanceled()) {
                                entity.setInLove(null);
                                ContainerUtils.extractItem(container, 0, 1, false);
                                setChanged();
                            }
                            if (event.shouldPlayEffect()) {
                                Balm.getNetworking().sendToTracking(((ServerLevel) level), worldPosition, new ChickenNestEffectMessage(worldPosition));
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    private boolean isSubmissiveAndBreedable(Animal animal, ItemStack itemStack) {
        // age is negative for babies, 0 for adults, and positive for adults that just created a baby (cooldown)
        return animal.getAge() == 0 && animal.canFallInLove() && !animal.isBaby() && animal.isFood(itemStack);
    }

    public ItemStack getContentStack() {
        return container.getItem(0);
    }

    @Override
    public Container getContainer() {
        return container;
    }
}
