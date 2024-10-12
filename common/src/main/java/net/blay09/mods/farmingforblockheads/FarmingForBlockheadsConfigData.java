package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.balm.api.config.*;

import java.util.List;
import java.util.Random;
import java.util.Set;

@Config(FarmingForBlockheads.MOD_ID)
public class FarmingForBlockheadsConfigData implements BalmConfigData {

    @Comment("List of groups to enable. 'default' is an alias for the inbuilt defaults (selling.seeds, selling.saplings and selling.fertilizers.minecraft).")
    @ExpectedType(String.class)
    public Set<String> includedGroups = Set.of("default");

    @Comment("List of groups to disable. Has precedence over includedGroups. 'selling.seeds' for example would disable all seeds from the market.")
    @ExpectedType(String.class)
    public Set<String> excludedPresets = Set.of();

    @ExpectedType(String.class)
    @Comment("List of names the merchant can have.")
    public List<String> merchantNames = FarmingForBlockheadsConfig.DEFAULT_MERCHANT_NAMES;

    @Comment("The range within animals can be fed by the feeding trough.")
    public int feedingTroughRange = 8; // 1-16

    @Comment("The maximum amount of animals (per type) until the feeding trough stops feeding.")
    public int feedingTroughMaxAnimals = 24; // 1-max

    @Comment("The range at which the chicken nest picks up laid eggs.")
    public int chickenNestRange = 8; // 1-16

    @Comment("The chance to get a bonus crop when using Green Fertilizer.")
    public double fertilizerBonusCropChance = 1f; // 0-1

    @Comment("The chance to get a bonus growth when using Red Fertilizer.")
    public double fertilizerBonusGrowthChance = 1f; // 0-1

    @Comment("The chance for Fertilized Farmland to turn back into regular Farmland (per provided bonus).")
    public double fertilizerRegressionChance = 0f; // 0-1

    @Comment("If true, merchants will be considered babies (on a technical level only), which may resolve exploits based on merchant death loot (like blood)")
    public boolean treatMerchantsLikeBabies = true;

    public String getRandomMerchantName(Random rand) {
        List<? extends String> candidates = merchantNames;
        if (candidates.isEmpty()) {
            candidates = FarmingForBlockheadsConfig.DEFAULT_MERCHANT_NAMES;
        }

        return candidates.get(rand.nextInt(candidates.size()));
    }

}
