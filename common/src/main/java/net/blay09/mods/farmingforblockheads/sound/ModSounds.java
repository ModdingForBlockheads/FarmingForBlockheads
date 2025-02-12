package net.blay09.mods.farmingforblockheads.sound;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
    public static DeferredObject<SoundEvent> falling;

    public static void initialize(BalmSounds sounds) {
        falling = sounds.register(new ResourceLocation(FarmingForBlockheads.MOD_ID, "falling"));
    }

}
