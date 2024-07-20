package com.github.teaconmc.friendshipbracelet.client.event;

import com.github.teaconmc.friendshipbracelet.FriendshipBracelet;
import com.github.teaconmc.friendshipbracelet.entity.data.FriendshipData;
import com.github.teaconmc.friendshipbracelet.init.ModData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;

@EventBusSubscriber(modid = FriendshipBracelet.MOD_ID, value = Dist.CLIENT)
public class RenderFriendNameEvent {
    @SubscribeEvent
    public static void onRenderFriendNameEvent(RenderNameTagEvent event) {
        if (event.getEntity() instanceof Player player) {
            LocalPlayer myself = Minecraft.getInstance().player;
            if (myself == null) {
                return;
            }
            FriendshipData data = myself.getData(ModData.FRIENDSHIP_BRACELET.get());
            if (player.getUUID().equals(data.getFriendId())) {
                MutableComponent prefix = Component.translatable("render.friendship_bracelet.friendship.prefix").withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.UNDERLINE);
                Component nameTag = Component.literal("").append(prefix).append(CommonComponents.space()).append(event.getContent());
                event.setContent(nameTag);
            }
        }
    }
}
