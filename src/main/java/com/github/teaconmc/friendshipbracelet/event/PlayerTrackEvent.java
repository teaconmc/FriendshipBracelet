package com.github.teaconmc.friendshipbracelet.event;

import com.github.teaconmc.friendshipbracelet.FriendshipBracelet;
import com.github.teaconmc.friendshipbracelet.entity.data.FriendshipData;
import com.github.teaconmc.friendshipbracelet.init.ModData;
import com.github.teaconmc.friendshipbracelet.network.SyncClientFriendshipPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = FriendshipBracelet.MOD_ID)
public class PlayerTrackEvent {
    @SubscribeEvent
    public static void onPlayerTrack(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof Player trackPlayer && event.getEntity() instanceof ServerPlayer myself) {
            FriendshipData data = myself.getData(ModData.FRIENDSHIP_BRACELET.get());
            // 当追踪实体为自己的朋友时，同步朋友数据，用于客户端渲染
            if (trackPlayer.getUUID().equals(data.getFriendId())) {
                PacketDistributor.sendToPlayer(myself, new SyncClientFriendshipPayload(data));
            }
        }
    }
}
