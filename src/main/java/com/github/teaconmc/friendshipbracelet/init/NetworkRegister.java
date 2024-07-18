package com.github.teaconmc.friendshipbracelet.init;

import com.github.teaconmc.friendshipbracelet.FriendshipBracelet;
import com.github.teaconmc.friendshipbracelet.data.FriendshipData;
import com.github.teaconmc.friendshipbracelet.data.RequestOpen;
import net.minecraft.network.codec.StreamCodec;
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
        registrar.playToClient(FriendshipData.NETWORK_TYPE, FriendshipData.STREAM_CODEC, FriendshipData::clientHandler);
        registrar.playToServer(RequestOpen.NETWORK_TYPE, StreamCodec.unit(new RequestOpen()), RequestOpen::serverHandler);
    }
}
