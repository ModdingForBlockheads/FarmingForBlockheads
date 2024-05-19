package net.blay09.mods.farmingforblockheads.client;

import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.neoforge.NeoForgeLoadContext;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = FarmingForBlockheads.MOD_ID, dist = Dist.CLIENT)
public class NeoForgeFarmingForBlockheadsClient {
    public NeoForgeFarmingForBlockheadsClient(IEventBus modEventBus) {
        final var context = new NeoForgeLoadContext(modEventBus);
        BalmClient.initialize(FarmingForBlockheads.MOD_ID, context, FarmingForBlockheadsClient::initialize);
    }
}
