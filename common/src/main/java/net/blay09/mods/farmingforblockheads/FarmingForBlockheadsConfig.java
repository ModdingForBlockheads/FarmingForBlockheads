package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.balm.api.Balm;

import java.util.Arrays;
import java.util.List;

public class FarmingForBlockheadsConfig {

    public static final List<String> DEFAULT_MERCHANT_NAMES = Arrays.asList(
            "Swap-O-Matic",
            "Emerald Muncher",
            "Weathered Salesperson"
    );
    public static FarmingForBlockheadsConfigData getActive() {
        return Balm.getConfig().getActive(FarmingForBlockheadsConfigData.class);
    }

    public static void initialize() {
        Balm.getConfig().registerConfig(FarmingForBlockheadsConfigData.class, null);
    }
}
