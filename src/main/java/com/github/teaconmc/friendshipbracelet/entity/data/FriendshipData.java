package com.github.teaconmc.friendshipbracelet.entity.data;

import com.github.teaconmc.friendshipbracelet.item.component.FriendshipContents;
import io.netty.buffer.ByteBuf;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.UnknownNullability;

import java.util.UUID;

public class FriendshipData implements INBTSerializable<CompoundTag> {
    public static final AttachmentType<FriendshipData> ATTACHMENT_TYPE = AttachmentType.serializable(FriendshipData::new).copyOnDeath().build();
    public static final StreamCodec<ByteBuf, FriendshipData> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, FriendshipData::getFriendId,
            ByteBufCodecs.STRING_UTF8, FriendshipData::getFriendName,
            ByteBufCodecs.BOOL, FriendshipData::isShareInv,
            FriendshipData::new
    );

    private UUID friendId;
    private String friendName;
    private boolean shareInv;

    public FriendshipData() {
        this.friendId = Util.NIL_UUID;
        this.friendName = StringUtils.EMPTY;
        this.shareInv = false;
    }

    public FriendshipData(UUID friendId, String friendName, boolean shareInv) {
        this.friendId = friendId;
        this.friendName = friendName;
        this.shareInv = shareInv;
    }

    public static FriendshipData getInstance(FriendshipContents contents) {
        FriendshipData data = new FriendshipData();
        data.friendId = contents.friendId();
        data.friendName = contents.friendName();
        return data;
    }

    @Override
    @UnknownNullability
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("FriendId", friendId);
        tag.putString("FriendName", friendName);
        tag.putBoolean("ShareInv", shareInv);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.friendId = nbt.getUUID("FriendId");
        this.friendName = nbt.getString("FriendName");
        this.shareInv = nbt.getBoolean("ShareInv");
    }

    public UUID getFriendId() {
        return friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public boolean isShareInv() {
        return shareInv;
    }

    public void setFriendId(UUID friendId) {
        this.friendId = friendId;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public void setShareInv(boolean shareInv) {
        this.shareInv = shareInv;
    }
}
