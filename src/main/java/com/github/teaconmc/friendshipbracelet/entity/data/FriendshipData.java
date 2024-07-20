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
            ByteBufCodecs.BOOL, FriendshipData::isMyselfShareInv,
            ByteBufCodecs.BOOL, FriendshipData::isFriendShareInv,
            FriendshipData::new
    );
    private UUID friendId;
    private String friendName;
    private boolean myselfShareInv;
    private boolean friendShareInv;

    public FriendshipData() {
        this.friendId = Util.NIL_UUID;
        this.friendName = StringUtils.EMPTY;
        this.myselfShareInv = false;
        this.friendShareInv = false;
    }

    public FriendshipData(UUID friendId, String friendName, boolean myselfShareInv, boolean friendShareInv) {
        this.friendId = friendId;
        this.friendName = friendName;
        this.myselfShareInv = myselfShareInv;
        this.friendShareInv = friendShareInv;
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
        tag.putBoolean("MyselfShareInv", myselfShareInv);
        tag.putBoolean("FriendShareInv", friendShareInv);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.friendId = nbt.getUUID("FriendId");
        this.friendName = nbt.getString("FriendName");
        this.myselfShareInv = nbt.getBoolean("MyselfShareInv");
        this.friendShareInv = nbt.getBoolean("FriendShareInv");
    }

    public UUID getFriendId() {
        return friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public boolean isMyselfShareInv() {
        return myselfShareInv;
    }

    public boolean isFriendShareInv() {
        return friendShareInv;
    }

    public void setMyselfShareInv(boolean myselfShareInv) {
        this.myselfShareInv = myselfShareInv;
    }

    public void setFriendShareInv(boolean friendShareInv) {
        this.friendShareInv = friendShareInv;
    }
}
