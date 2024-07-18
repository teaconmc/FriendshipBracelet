package com.github.teaconmc.friendshipbracelet.data;

import com.github.teaconmc.friendshipbracelet.FriendshipBracelet;
import com.github.teaconmc.friendshipbracelet.inventory.FriendshipContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class RequestOpen implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<RequestOpen> NETWORK_TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(FriendshipBracelet.MOD_ID, "request_open"));

    public static void serverHandler(final RequestOpen data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            player.openMenu(new SimpleMenuProvider(
                    (id, inventory, playerIn) -> new FriendshipContainer(id, inventory),
                    Component.literal("Friendship Menu")));
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return NETWORK_TYPE;
    }
}
