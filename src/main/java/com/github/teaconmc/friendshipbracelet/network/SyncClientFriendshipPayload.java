package com.github.teaconmc.friendshipbracelet.network;

import com.github.teaconmc.friendshipbracelet.FriendshipBracelet;
import com.github.teaconmc.friendshipbracelet.entity.data.FriendshipData;
import com.github.teaconmc.friendshipbracelet.init.ModData;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class SyncClientFriendshipPayload implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncClientFriendshipPayload> NETWORK_TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(FriendshipBracelet.MOD_ID, "sync_friendship"));
    public static final StreamCodec<ByteBuf, SyncClientFriendshipPayload> STREAM_CODEC = StreamCodec.composite(FriendshipData.STREAM_CODEC, SyncClientFriendshipPayload::getFriendshipData, SyncClientFriendshipPayload::new);
    private final FriendshipData friendshipData;

    public SyncClientFriendshipPayload(FriendshipData friendshipData) {
        this.friendshipData = friendshipData;
    }

    public static void clientHandler(final SyncClientFriendshipPayload data, final IPayloadContext context) {
        context.enqueueWork(() -> setClientFriendshipData(data));
    }

    @OnlyIn(Dist.CLIENT)
    private static void setClientFriendshipData(final SyncClientFriendshipPayload friendshipData) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        player.setData(ModData.FRIENDSHIP_BRACELET.get(), friendshipData.friendshipData);
    }

    public FriendshipData getFriendshipData() {
        return friendshipData;
    }

    @Override
    @NotNull
    public Type<? extends CustomPacketPayload> type() {
        return NETWORK_TYPE;
    }
}
