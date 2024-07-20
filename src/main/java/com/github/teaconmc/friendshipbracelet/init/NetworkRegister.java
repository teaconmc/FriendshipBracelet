package com.github.teaconmc.friendshipbracelet.init;

import com.github.teaconmc.friendshipbracelet.FriendshipBracelet;
import com.github.teaconmc.friendshipbracelet.network.RequestServerPayload;
import com.github.teaconmc.friendshipbracelet.network.SyncClientFriendshipPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = FriendshipBracelet.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkRegister {
    public static final String VERSION = "1.0.0";

    @SubscribeEvent
    public static void registerPayload(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(VERSION);
        registrar.playToServer(RequestServerPayload.NETWORK_TYPE, RequestServerPayload.STREAM_CODEC, RequestServerPayload::serverHandler);
        registrar.playToClient(SyncClientFriendshipPayload.NETWORK_TYPE, SyncClientFriendshipPayload.STREAM_CODEC, SyncClientFriendshipPayload::clientHandler);
    }
}
