package net.blay09.mods.farmingforblockheads.api;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class FeedingTroughEvent extends BalmEvent {
    private final BlockEntity blockEntity;
    private final Animal entity;
    private final ItemStack itemStack;

    private boolean shouldPlayEffect = true;

    public FeedingTroughEvent(BlockEntity blockEntity, Animal entity, ItemStack itemStack) {
        this.blockEntity = blockEntity;
        this.entity = entity;
        this.itemStack = itemStack;
    }

    public BlockEntity getBlockEntity() {
        return blockEntity;
    }

    public Animal getEntity() {
        return entity;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public boolean shouldPlayEffect() {
        return shouldPlayEffect;
    }

    public void setShouldPlayEffect(boolean shouldPlayEffect) {
        this.shouldPlayEffect = shouldPlayEffect;
    }
}
