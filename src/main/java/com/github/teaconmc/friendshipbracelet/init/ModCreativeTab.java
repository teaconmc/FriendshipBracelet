package com.github.teaconmc.friendshipbracelet.init;

import com.github.teaconmc.friendshipbracelet.FriendshipBracelet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FriendshipBracelet.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TABS = TABS.register(FriendshipBracelet.MOD_ID, () -> CreativeModeTab.builder()
            .title(Component.translatable("item_group." + FriendshipBracelet.MOD_ID + ".name"))
            .icon(() -> ModItems.FRIENDSHIP_BRACELET.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.FRIENDSHIP_BRACELET.get());
            }).build());
}
