package net.blay09.mods.farmingforblockheads.item;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModItems {

    public static Item fertilizerGreen;
    public static Item fertilizerRed;
    public static Item fertilizerYellow;

    public static TagKey<Item> EGGS_TAG;

    public static void initialize(BalmItems items) {
        items.registerItem(() -> fertilizerGreen = new FertilizerItem(FertilizerItem.FertilizerType.RICH), id("green_fertilizer"));
        items.registerItem(() -> fertilizerRed = new FertilizerItem(FertilizerItem.FertilizerType.HEALTHY), id("red_fertilizer"));
        items.registerItem(() -> fertilizerYellow = new FertilizerItem(FertilizerItem.FertilizerType.STABLE), id("yellow_fertilizer"));

        items.registerCreativeModeTab(id("farmingforblockheads"), () -> new ItemStack(ModBlocks.market));

        EGGS_TAG = Balm.getRegistries().getItemTag(new ResourceLocation("balm", "eggs"));
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(FarmingForBlockheads.MOD_ID, path);
    }

}
