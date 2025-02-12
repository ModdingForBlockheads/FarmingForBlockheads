package net.blay09.mods.farmingforblockheads.client;

import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.farmingforblockheads.block.entity.ModBlockEntities;
import net.blay09.mods.farmingforblockheads.client.render.ChickenNestRenderer;
import net.blay09.mods.farmingforblockheads.client.render.FeedingTroughRenderer;
import net.blay09.mods.farmingforblockheads.client.render.MerchantRenderer;
import net.blay09.mods.farmingforblockheads.entity.ModEntities;

public class ModRenderers {
    public static void initialize(BalmRenderers renderers) {
        renderers.registerEntityRenderer(ModEntities.merchant::get, MerchantRenderer::new);

        renderers.registerBlockEntityRenderer(ModBlockEntities.chickenNest::get, ChickenNestRenderer::new);
        renderers.registerBlockEntityRenderer(ModBlockEntities.feedingTrough::get, FeedingTroughRenderer::new);
    }
}
