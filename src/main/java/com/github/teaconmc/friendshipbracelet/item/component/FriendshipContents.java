package com.github.teaconmc.friendshipbracelet.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.Util;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;

import java.util.UUID;

public record FriendshipContents(UUID owner, UUID friend) {
    public static final Codec<FriendshipContents> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    UUIDUtil.CODEC.fieldOf("owner").forGetter(FriendshipContents::owner),
                    UUIDUtil.CODEC.fieldOf("friend").forGetter(FriendshipContents::friend)
            ).apply(instance, FriendshipContents::new)
    );
    public static final StreamCodec<ByteBuf, FriendshipContents> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, FriendshipContents::owner,
            UUIDUtil.STREAM_CODEC, FriendshipContents::friend,
            FriendshipContents::new
    );

    public static FriendshipContents empty() {
        return new FriendshipContents(Util.NIL_UUID, Util.NIL_UUID);
    }
}
