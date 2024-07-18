package com.github.teaconmc.friendshipbracelet.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.Util;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public record FriendshipContents(UUID ownerId, UUID friendId, String ownerName, String friendName) {
    public static final Codec<FriendshipContents> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    UUIDUtil.CODEC.fieldOf("owner_id").forGetter(FriendshipContents::ownerId),
                    UUIDUtil.CODEC.fieldOf("friend_id").forGetter(FriendshipContents::friendId),
                    Codec.STRING.fieldOf("owner_name").forGetter(FriendshipContents::ownerName),
                    Codec.STRING.fieldOf("friend_name").forGetter(FriendshipContents::friendName)
            ).apply(instance, FriendshipContents::new)
    );
    public static final StreamCodec<ByteBuf, FriendshipContents> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, FriendshipContents::ownerId,
            UUIDUtil.STREAM_CODEC, FriendshipContents::friendId,
            ByteBufCodecs.STRING_UTF8, FriendshipContents::ownerName,
            ByteBufCodecs.STRING_UTF8, FriendshipContents::friendName,
            FriendshipContents::new
    );

    public static FriendshipContents empty() {
        return new FriendshipContents(Util.NIL_UUID, Util.NIL_UUID, StringUtils.EMPTY, StringUtils.EMPTY);
    }
}
