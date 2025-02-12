package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.CropGrowEvent;
import net.blay09.mods.balm.api.event.PlayerLoginEvent;
import net.blay09.mods.farmingforblockheads.api.FarmingForBlockheadsAPI;
import net.blay09.mods.farmingforblockheads.api.MarketRegistryReloadEvent;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.blay09.mods.farmingforblockheads.block.entity.ModBlockEntities;
import net.blay09.mods.farmingforblockheads.client.ClientProxy;
import net.blay09.mods.farmingforblockheads.compat.Compat;
import net.blay09.mods.farmingforblockheads.entity.ModEntities;
import net.blay09.mods.farmingforblockheads.menu.ModMenus;
import net.blay09.mods.farmingforblockheads.item.ModItems;
import net.blay09.mods.farmingforblockheads.loot.ModLootModifiers;
import net.blay09.mods.farmingforblockheads.network.ModNetworking;
import net.blay09.mods.farmingforblockheads.registry.market.MarketRegistryLoader;
import net.blay09.mods.farmingforblockheads.sound.ModSounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FarmingForBlockheads {

    public static final String MOD_ID = "farmingforblockheads";

    public static Logger logger = LogManager.getLogger();

    public static void initialize() {
        FarmingForBlockheadsAPI.__setupAPI(new InternalMethodsImpl());

        FarmingForBlockheadsConfig.initialize();
        ModNetworking.initialize(Balm.getNetworking());
        ModBlocks.initialize(Balm.getBlocks());
        ModBlockEntities.initialize(Balm.getBlockEntities());
        ModEntities.initialize(Balm.getEntities());
        ModItems.initialize(Balm.getItems());
        ModSounds.initialize(Balm.getSounds());
        ModMenus.initialize(Balm.getMenus());
        ModLootModifiers.initialize(Balm.getLootTables());

        Balm.addServerReloadListener(new ResourceLocation(MOD_ID, "market_registry"), new MarketRegistryLoader());

        Balm.initializeIfLoaded(Compat.FORESTRY, "net.blay09.mods.farmingforblockheads.compat.ForestryAddon");
        Balm.initializeIfLoaded(Compat.NATURA, "net.blay09.mods.farmingforblockheads.compat.NaturaAddon");
        Balm.initializeIfLoaded(Compat.TERRAQUEOUS, "net.blay09.mods.farmingforblockheads.compat.TerraqueousAddon");

        Balm.getEvents().onEvent(MarketRegistryReloadEvent.Pre.class, event -> {
            FarmingForBlockheadsAPI.registerMarketCategory(FarmingForBlockheadsAPI.MARKET_CATEGORY_SEEDS, "gui.farmingforblockheads:market.tooltip_seeds", new ItemStack(Items.WHEAT_SEEDS), 10);
            FarmingForBlockheadsAPI.registerMarketCategory(FarmingForBlockheadsAPI.MARKET_CATEGORY_SAPLINGS, "gui.farmingforblockheads:market.tooltip_saplings", new ItemStack(Items.OAK_SAPLING), 20);
            FarmingForBlockheadsAPI.registerMarketCategory(FarmingForBlockheadsAPI.MARKET_CATEGORY_FLOWERS, "gui.farmingforblockheads:market.tooltip_flowers", new ItemStack(Items.DANDELION), 30);
            FarmingForBlockheadsAPI.registerMarketCategory(FarmingForBlockheadsAPI.MARKET_CATEGORY_OTHER, "gui.farmingforblockheads:market.tooltip_other", new ItemStack(Items.BONE_MEAL), 40);
        });

        Balm.getEvents().onEvent(PlayerLoginEvent.class, MarketRegistryLoader::onLogin);

        Balm.getEvents().onEvent(CropGrowEvent.Post.class, FarmlandHandler::onGrowEvent);
    }

}
