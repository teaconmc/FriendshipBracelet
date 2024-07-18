package com.github.teaconmc.friendshipbracelet.data;

import com.github.teaconmc.friendshipbracelet.FriendshipBracelet;
import com.github.teaconmc.friendshipbracelet.item.component.FriendshipContents;
import io.netty.buffer.ByteBuf;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.UUID;

public class FriendshipData implements INBTSerializable<CompoundTag>, CustomPacketPayload {
    public static final AttachmentType<FriendshipData> ATTACHMENT_TYPE = AttachmentType.serializable(FriendshipData::new).build();
    public static final CustomPacketPayload.Type<FriendshipData> NETWORK_TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(FriendshipBracelet.MOD_ID, "friendship_bracelet"));
    public static final StreamCodec<ByteBuf, FriendshipData> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, FriendshipData::getFriendId,
            ByteBufCodecs.STRING_UTF8, FriendshipData::getFriendName,
            ByteBufCodecs.BOOL, FriendshipData::isShareInv,
            FriendshipData::new
    );

    private UUID friendId = Util.NIL_UUID;
    private String friendName = StringUtils.EMPTY;
    private boolean shareInv = false;

    public FriendshipData() {
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

    public static void clientHandler(final FriendshipData data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            player.setData(ATTACHMENT_TYPE, data);
        });
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

    @Override
    @NotNull
    public Type<? extends CustomPacketPayload> type() {
        return NETWORK_TYPE;
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
