package com.github.teaconmc.friendshipbracelet.init;

import com.github.teaconmc.friendshipbracelet.FriendshipBracelet;
import com.github.teaconmc.friendshipbracelet.inventory.FriendshipContainer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenu {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, FriendshipBracelet.MOD_ID);

    public static final Supplier<MenuType<FriendshipContainer>> FRIENDSHIP_CONTAINER = MENU_TYPES.register("friendship_bracelet", () -> FriendshipContainer.TYPE);
}
