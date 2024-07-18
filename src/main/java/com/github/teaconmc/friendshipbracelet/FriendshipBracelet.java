package com.github.teaconmc.friendshipbracelet;

import com.github.teaconmc.friendshipbracelet.init.ModCreativeTab;
import com.github.teaconmc.friendshipbracelet.init.ModData;
import com.github.teaconmc.friendshipbracelet.init.ModItems;
import com.github.teaconmc.friendshipbracelet.init.ModMenu;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(FriendshipBracelet.MOD_ID)
public class FriendshipBracelet {
    public static final String MOD_ID = "friendship_bracelet";

    public FriendshipBracelet(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.ITEMS.register(modEventBus);
        ModItems.DATA_COMPONENTS.register(modEventBus);
        ModCreativeTab.TABS.register(modEventBus);
        ModData.ATTACHMENT_TYPES.register(modEventBus);
        ModMenu.MENU_TYPES.register(modEventBus);
    }
}
