package com.github.teaconmc.friendshipbracelet.capability;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public class FriendshipCapability implements INBTSerializable<CompoundTag> {
    public static final AttachmentType<FriendshipCapability> TYPE = AttachmentType.serializable(FriendshipCapability::new).build();

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return null;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {

    }
}
