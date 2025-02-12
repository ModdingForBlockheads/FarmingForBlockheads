package net.blay09.mods.farmingforblockheads.client;

import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.farmingforblockheads.client.gui.screen.MarketScreen;
import net.blay09.mods.farmingforblockheads.menu.ModMenus;

public class ModScreens {
    public static void initialize(BalmScreens screens) {
        screens.registerScreen(ModMenus.market::get, MarketScreen::new);
    }
}
