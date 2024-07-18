package com.github.teaconmc.friendshipbracelet.client.init;

import com.github.teaconmc.friendshipbracelet.FriendshipBracelet;
import com.github.teaconmc.friendshipbracelet.client.input.BraceletKey;
import com.github.teaconmc.friendshipbracelet.client.screen.FriendshipScreen;
import com.github.teaconmc.friendshipbracelet.inventory.FriendshipContainer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT, modid = FriendshipBracelet.MOD_ID)
public class ClientSetup {
    @SubscribeEvent
    public static void onRegisterScreen(RegisterMenuScreensEvent evt) {
        evt.register(FriendshipContainer.TYPE, FriendshipScreen::new);
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(BraceletKey.BRACELET_KEY);
    }
}
