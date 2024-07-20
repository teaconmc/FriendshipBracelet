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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class RequestServerPayload implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<RequestServerPayload> NETWORK_TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(FriendshipBracelet.MOD_ID, "request_open"));
    public static final StreamCodec<ByteBuf, RequestServerPayload> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, RequestServerPayload::getEventId, RequestServerPayload::new);
    private final Event event;

    private RequestServerPayload(int eventId) {
        this.event = Event.values()[eventId];
    }

    public static RequestServerPayload openFriendshipMenu() {
        return new RequestServerPayload(Event.OPEN_FRIENDSHIP_MENU.ordinal());
    }

    public static RequestServerPayload changeShareInventory() {
        return new RequestServerPayload(Event.SHARE_INVENTORY.ordinal());
    }

    public static RequestServerPayload teleportToFriend() {
        return new RequestServerPayload(Event.TELEPORT_TO_FRIEND.ordinal());
    }

    public static void serverHandler(final RequestServerPayload data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (data.event.equals(Event.OPEN_FRIENDSHIP_MENU)) {
                openFriendshipMenu(context);
                return;
            }
            if (data.event.equals(Event.SHARE_INVENTORY)) {
                changeShareInventory(context);
            }
            if (data.event.equals(Event.TELEPORT_TO_FRIEND)) {
                teleportToFriend(context);
            }
        });
    }

    private static void openFriendshipMenu(IPayloadContext context) {
        if (context.player() instanceof ServerPlayer serverPlayer) {
            MutableComponent title = Component.translatable("screen.friendship_bracelet.friendship.name");
            FriendshipData data = serverPlayer.getData(ModData.FRIENDSHIP_BRACELET.get());
            Player friendPlayer = serverPlayer.server.getPlayerList().getPlayer(data.getFriendId());
            // 如果朋友在线，那么需要同步一下物品栏分享的是否打开
            if (friendPlayer != null && friendPlayer.isAlive()) {
                FriendshipData friendPlayerData = friendPlayer.getData(ModData.FRIENDSHIP_BRACELET.get());
                data.setFriendShareInv(friendPlayerData.isMyselfShareInv());
            }
            SimpleMenuProvider provider = new SimpleMenuProvider((id, inventory, playerIn) -> new FriendshipContainer(id, inventory, data), title);
            serverPlayer.openMenu(provider, buf -> FriendshipData.STREAM_CODEC.encode(buf, data));
        }
    }

    private static void changeShareInventory(IPayloadContext context) {
        if (context.player() instanceof ServerPlayer serverPlayer && serverPlayer.containerMenu instanceof FriendshipContainer) {
            FriendshipData data = serverPlayer.getData(ModData.FRIENDSHIP_BRACELET.get());
            data.setMyselfShareInv(!data.isMyselfShareInv());
        }
    }

    private static void teleportToFriend(IPayloadContext context) {
        if (context.player() instanceof ServerPlayer serverPlayer && serverPlayer.containerMenu instanceof FriendshipContainer) {
            FriendshipData data = serverPlayer.getData(ModData.FRIENDSHIP_BRACELET.get());
            Player friendPlayer = serverPlayer.server.getPlayerList().getPlayer(data.getFriendId());
            if (friendPlayer != null && friendPlayer.isAlive() && friendPlayer.level() instanceof ServerLevel serverLevel) {
                Vec3 position = friendPlayer.position();
                // 支持跨维度传送
                serverPlayer.teleportTo(serverLevel, position.x, position.y, position.z, friendPlayer.getYRot(), friendPlayer.getXRot());
            }
        }
    }

    @Override
    @NotNull
    public Type<? extends CustomPacketPayload> type() {
        return NETWORK_TYPE;
    }

    private int getEventId() {
        return event.ordinal();
    }

    private enum Event {
        OPEN_FRIENDSHIP_MENU, SHARE_INVENTORY, TELEPORT_TO_FRIEND
    }
}
