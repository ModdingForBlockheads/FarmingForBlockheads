package net.blay09.mods.farmingforblockheads.entity;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.entity.BalmEntities;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
    public static DeferredObject<EntityType<MerchantEntity>> merchant;

    public static void initialize(BalmEntities entities) {
        merchant = entities.registerEntity(id("merchant"), EntityType.Builder.of(MerchantEntity::new, MobCategory.MISC).sized(0.6f, 1.95f), MerchantEntity::createAttributes);
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(FarmingForBlockheads.MOD_ID, path);
    }

}
