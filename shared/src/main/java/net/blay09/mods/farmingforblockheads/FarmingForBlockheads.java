package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.CropGrowEvent;
import net.blay09.mods.balm.api.event.PlayerLoginEvent;
import net.blay09.mods.farmingforblockheads.api.Payment;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.blay09.mods.farmingforblockheads.block.entity.ModBlockEntities;
import net.blay09.mods.farmingforblockheads.entity.ModEntities;
import net.blay09.mods.farmingforblockheads.menu.ModMenus;
import net.blay09.mods.farmingforblockheads.item.ModItems;
import net.blay09.mods.farmingforblockheads.loot.ModLootModifiers;
import net.blay09.mods.farmingforblockheads.network.ModNetworking;
import net.blay09.mods.farmingforblockheads.recipe.MarketRecipe;
import net.blay09.mods.farmingforblockheads.recipe.ModRecipes;
import net.blay09.mods.farmingforblockheads.registry.MarketCategoryLoader;
import net.blay09.mods.farmingforblockheads.registry.MarketCategoryRegistry;
import net.blay09.mods.farmingforblockheads.registry.MarketPresetLoader;
import net.blay09.mods.farmingforblockheads.sound.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FarmingForBlockheads {

    public static final String MOD_ID = "farmingforblockheads";

    public static Logger logger = LogManager.getLogger();

    public static void initialize() {
        FarmingForBlockheadsConfig.initialize();
        ModNetworking.initialize(Balm.getNetworking());
        ModBlocks.initialize(Balm.getBlocks());
        ModBlockEntities.initialize(Balm.getBlockEntities());
        ModEntities.initialize(Balm.getEntities());
        ModItems.initialize(Balm.getItems());
        ModSounds.initialize(Balm.getSounds());
        ModMenus.initialize(Balm.getMenus());
        ModLootModifiers.initialize(Balm.getLootTables());
        ModRecipes.initialize(Balm.getRecipes());

        Balm.addServerReloadListener(new ResourceLocation(MOD_ID, "market_category_loader"), new MarketCategoryLoader());
        Balm.addServerReloadListener(new ResourceLocation(MOD_ID, "market_preset_loader"), new MarketPresetLoader());

        Balm.getEvents().onEvent(PlayerLoginEvent.class, MarketCategoryRegistry.INSTANCE::onLogin);
        Balm.getEvents().onEvent(CropGrowEvent.Post.class, FarmlandHandler::onGrowEvent);
    }

    public static Component getPaymentComponent(Payment payment) {
        final var ingredient = payment.ingredient();
        if (ingredient.isEmpty()) {
            return Component.literal("<invalid>");
        }

        final var itemStack = ingredient.getItems()[0];
        return Component.translatable("tooltip.farmingforblockheads.payment_item", payment.count(), itemStack.getHoverName());
    }
}
