package com.github.teaconmc.friendshipbracelet.init;

import com.github.teaconmc.friendshipbracelet.FriendshipBracelet;
import com.github.teaconmc.friendshipbracelet.entity.data.FriendshipData;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModData {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, FriendshipBracelet.MOD_ID);

    public static final Supplier<AttachmentType<FriendshipData>> FRIENDSHIP_BRACELET = ATTACHMENT_TYPES.register("friendship_bracelet", () -> FriendshipData.ATTACHMENT_TYPE);
}
