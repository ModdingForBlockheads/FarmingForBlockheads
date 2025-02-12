package net.blay09.mods.farmingforblockheads.compat;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.farmingforblockheads.api.FarmingForBlockheadsAPI;
import net.blay09.mods.farmingforblockheads.api.IMarketRegistryDefaultHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

public class TerraqueousAddon {

    private static final String KEY_SAPLINGS = "Terraqueous Saplings";

    public TerraqueousAddon() {
        FarmingForBlockheadsAPI.registerMarketDefaultHandler(KEY_SAPLINGS, new IMarketRegistryDefaultHandler() {
            @Override
            public void register(@Nullable ItemStack overridePayment, @Nullable Integer overrideCount) {
                final String[] SAPLINGS = new String[]{"sapling"};

                ItemStack effectivePayment = overridePayment;
                if (effectivePayment == null) {
                    effectivePayment = getDefaultPayment();
                }

                for (String SAPLING : SAPLINGS) {
                    ResourceLocation location = new ResourceLocation(Compat.NATURA, SAPLING);
                    Block blockSapling = Balm.getRegistries().getBlock(location);
                    if (blockSapling != Blocks.AIR) {
                        for (int j = 0; j <= 9; j++) {
                            ItemStack saplingStack = new ItemStack(blockSapling, overrideCount != null ? overrideCount : 1);
                            FarmingForBlockheadsAPI.registerMarketEntry(saplingStack, effectivePayment, FarmingForBlockheadsAPI.getMarketCategorySaplings());
                        }
                    }
                }
            }

            @Override
            public boolean isEnabledByDefault() {
                return true;
            }

            @Override
            public ItemStack getDefaultPayment() {
                return new ItemStack(Items.EMERALD);
            }
        });
    }

}
