package net.blay09.mods.farmingforblockheads.menu;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;

public class ModMenus {
    public static DeferredObject<MenuType<MarketMenu>> market;

    public static void initialize(BalmMenus menus) {
        market = menus.registerMenu(id("market"), (windowId, playerInventory, data) -> {
            BlockPos pos = data.readBlockPos();
            return new MarketClientMenu(windowId, playerInventory, pos);
        });
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(FarmingForBlockheads.MOD_ID, path);
    }
}
