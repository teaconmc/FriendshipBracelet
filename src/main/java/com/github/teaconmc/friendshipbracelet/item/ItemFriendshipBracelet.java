package com.github.teaconmc.friendshipbracelet.item;

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

import java.util.List;
import java.util.UUID;

public class ItemFriendshipBracelet extends Item {
    public ItemFriendshipBracelet() {
        super(new Properties().stacksTo(1).component(ModItems.FRIENDSHIP_CONTENTS.value(), FriendshipContents.empty()));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemInHand = player.getItemInHand(usedHand);
        FriendshipContents contents = itemInHand.getOrDefault(ModItems.FRIENDSHIP_CONTENTS.value(), FriendshipContents.empty());
        if (Util.NIL_UUID.equals(contents.owner())) {
            itemInHand.set(ModItems.FRIENDSHIP_CONTENTS.value(), new FriendshipContents(player.getUUID(), Util.NIL_UUID));
            return InteractionResultHolder.success(itemInHand);
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        FriendshipContents contents = stack.get(ModItems.FRIENDSHIP_CONTENTS.value());
        if (contents != null) {
            UUID owner = contents.owner();
            if (Util.NIL_UUID.equals(owner)) {
                components.add(Component.literal("No Owner"));
            } else {
                components.add(Component.literal("Hahaha"));
            }
        }
    }
}
