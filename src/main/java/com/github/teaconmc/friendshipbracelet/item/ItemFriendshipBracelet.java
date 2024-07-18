package com.github.teaconmc.friendshipbracelet.item;

import com.github.teaconmc.friendshipbracelet.data.FriendshipData;
import com.github.teaconmc.friendshipbracelet.init.ModItems;
import com.github.teaconmc.friendshipbracelet.item.component.FriendshipContents;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.UUID;

public class ItemFriendshipBracelet extends Item {
    public ItemFriendshipBracelet() {
        super(new Properties().stacksTo(1).component(ModItems.FRIENDSHIP_CONTENTS.value(), FriendshipContents.empty()));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (usedHand == InteractionHand.OFF_HAND) {
            return super.use(level, player, usedHand);
        }

        ItemStack itemInHand = player.getItemInHand(usedHand);
        FriendshipContents contents = itemInHand.getOrDefault(ModItems.FRIENDSHIP_CONTENTS.value(), FriendshipContents.empty());
        UUID ownerId = contents.ownerId();
        UUID friendId = contents.friendId();
        UUID userId = player.getUUID();

        String ownerName = contents.ownerName();
        String userName = player.getScoreboardName();

        // 如果主人栏为空，写入主人
        if (Util.NIL_UUID.equals(ownerId)) {
            itemInHand.set(ModItems.FRIENDSHIP_CONTENTS.value(), new FriendshipContents(userId, Util.NIL_UUID, userName, StringUtils.EMPTY));
            return InteractionResultHolder.success(itemInHand);
        }
        // 如果朋友栏为空
        if (Util.NIL_UUID.equals(friendId)) {
            // 和主人栏 ID 不同，那就是朋友
            if (!ownerId.equals(userId)) {
                itemInHand.set(ModItems.FRIENDSHIP_CONTENTS.value(), new FriendshipContents(ownerId, userId, ownerName, userName));
                return InteractionResultHolder.success(itemInHand);
            } else {
                player.sendSystemMessage(Component.literal("不能和自己做朋友！"));
                return InteractionResultHolder.fail(itemInHand);
            }
        }
        // 是自己的，装上
        if (ownerId.equals(userId)) {
            player.setData(FriendshipData.ATTACHMENT_TYPE, FriendshipData.getInstance(contents));
            player.getItemInHand(usedHand).shrink(1);
            return InteractionResultHolder.consume(itemInHand);
        } else {
            player.sendSystemMessage(Component.literal("这是你东西么？一天天的乱拿"));
            return InteractionResultHolder.fail(itemInHand);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        FriendshipContents contents = stack.get(ModItems.FRIENDSHIP_CONTENTS.value());
        if (contents != null) {
            UUID owner = contents.ownerId();
            if (Util.NIL_UUID.equals(owner)) {
                components.add(Component.literal("No Owner"));
            } else {
                components.add(Component.literal("Hahaha"));
            }
        }
    }
}
