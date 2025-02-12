package net.blay09.mods.farmingforblockheads.api;

import net.blay09.mods.balm.api.event.BalmEvent;

public abstract class MarketRegistryReloadEvent extends BalmEvent {
    public static class Pre extends MarketRegistryReloadEvent {
        public void registerMarketDefaultHandler(String key, IMarketRegistryDefaultHandler handler) {
            FarmingForBlockheadsAPI.registerMarketDefaultHandler(key, handler);
        }
    }

    public static class Post extends MarketRegistryReloadEvent {
    }
}
