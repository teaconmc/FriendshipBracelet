package com.github.teaconmc.friendshipbracelet.network;

import com.github.teaconmc.friendshipbracelet.FriendshipBracelet;
import com.github.teaconmc.friendshipbracelet.entity.data.FriendshipData;
import com.github.teaconmc.friendshipbracelet.init.ModData;
import com.github.teaconmc.friendshipbracelet.inventory.FriendshipContainer;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class RequestServerPayload implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<RequestServerPayload> NETWORK_TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(FriendshipBracelet.MOD_ID, "request_open"));
    public static final StreamCodec<ByteBuf, RequestServerPayload> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, RequestServerPayload::getEventId, RequestServerPayload::new);

    private final Event event;

    public RequestServerPayload(int eventId) {
        this.event = Event.values()[eventId];
    }

    public static RequestServerPayload openFriendshipMenu() {
        return new RequestServerPayload(Event.OPEN_FRIENDSHIP_MENU.ordinal());
    }

    public static void serverHandler(final RequestServerPayload data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (data.event.equals(Event.OPEN_FRIENDSHIP_MENU)) {
                openFriendshipMenu(context);
            }
        });
    }

    private static void openFriendshipMenu(IPayloadContext context) {
        if (context.player() instanceof ServerPlayer serverPlayer) {
            MutableComponent title = Component.translatable("screen.friendship_bracelet.friendship.name");
            FriendshipData data = serverPlayer.getData(ModData.FRIENDSHIP_BRACELET.get());
            SimpleMenuProvider provider = new SimpleMenuProvider((id, inventory, playerIn) -> new FriendshipContainer(id, inventory, data), title);
            serverPlayer.openMenu(provider, buf -> FriendshipData.STREAM_CODEC.encode(buf, data));
        }
    }

    public int getEventId() {
        return event.ordinal();
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return NETWORK_TYPE;
    }

    public enum Event {
        OPEN_FRIENDSHIP_MENU
    }
}
