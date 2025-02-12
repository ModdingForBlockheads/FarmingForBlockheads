package net.blay09.mods.farmingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.entity.MerchantEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MerchantRenderer extends LivingEntityRenderer<MerchantEntity, VillagerModel<MerchantEntity>> {

    private static final ResourceLocation MERCHANT_TEXTURE = new ResourceLocation(FarmingForBlockheads.MOD_ID, "textures/entity/merchant.png");
    private static final Map<ResourceLocation, ResourceLocation> verifiedTextures = new HashMap<>();

    public MerchantRenderer(EntityRendererProvider.Context context) {
        super(context, new VillagerModel<>(context.bakeLayer(ModelLayers.VILLAGER)), 0.5f);
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(MerchantEntity entity) {
        ResourceLocation textureLocation = entity.getTextureLocation();
        if (textureLocation == null) {
            return MERCHANT_TEXTURE;
        }

        return verifiedTextures.computeIfAbsent(textureLocation, it -> {
            if (Minecraft.getInstance().getResourceManager().getResource(it).isPresent()) {
                return it;
            }
            return MERCHANT_TEXTURE;
        });
    }

    @Override
    public void render(MerchantEntity entity, float p_225623_2_, float p_225623_3_, PoseStack poseStack, MultiBufferSource buffers, int p_225623_6_) {
        int diggingAnimation = entity.getDiggingAnimation();
        if (diggingAnimation > 0) {
            poseStack.translate(0.0, -diggingAnimation * 0.05, 0.0);
        }
        super.render(entity, p_225623_2_, p_225623_3_, poseStack, buffers, p_225623_6_);
    }

    @Override
    protected boolean shouldShowName(MerchantEntity entity) {
        return entity.getDiggingAnimation() <= 0 && super.shouldShowName(entity);
    }
}
