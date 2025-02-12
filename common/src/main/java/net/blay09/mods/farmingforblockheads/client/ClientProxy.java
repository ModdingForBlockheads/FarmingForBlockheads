package net.blay09.mods.farmingforblockheads.client;

import com.google.common.collect.ArrayListMultimap;
import net.blay09.mods.farmingforblockheads.CommonProxy;
import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.blay09.mods.farmingforblockheads.api.IMarketEntry;
import net.blay09.mods.farmingforblockheads.menu.MarketClientMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;

public class ClientProxy extends CommonProxy {
    @Override
    public void receivedMarketList(ArrayListMultimap<IMarketCategory, IMarketEntry> entryMap) {

    }
}
