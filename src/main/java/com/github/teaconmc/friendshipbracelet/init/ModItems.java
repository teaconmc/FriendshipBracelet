package com.github.teaconmc.friendshipbracelet.init;

import com.github.teaconmc.friendshipbracelet.FriendshipBracelet;
import com.github.teaconmc.friendshipbracelet.item.ItemFriendshipBracelet;
import com.github.teaconmc.friendshipbracelet.item.component.FriendshipContents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FriendshipBracelet.MOD_ID);
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(FriendshipBracelet.MOD_ID);

    public static DeferredItem<Item> FRIENDSHIP_BRACELET = ITEMS.register("friendship_bracelet", ItemFriendshipBracelet::new);
    public static DeferredHolder<DataComponentType<?>, DataComponentType<FriendshipContents>> FRIENDSHIP_CONTENTS = DATA_COMPONENTS.registerComponentType(
            "friendship_bracelet", builder -> builder.persistent(FriendshipContents.CODEC).networkSynchronized(FriendshipContents.STREAM_CODEC).cacheEncoding()
    );
}
