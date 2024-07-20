package com.github.teaconmc.friendshipbracelet.client.screen;

import com.github.teaconmc.friendshipbracelet.FriendshipBracelet;
import com.github.teaconmc.friendshipbracelet.entity.data.FriendshipData;
import com.github.teaconmc.friendshipbracelet.inventory.FriendshipContainer;
import com.github.teaconmc.friendshipbracelet.network.RequestServerPayload;
import com.mojang.authlib.GameProfile;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

public class FriendshipScreen extends AbstractContainerScreen<FriendshipContainer> {
    private static final ResourceLocation BG = ResourceLocation.fromNamespaceAndPath(FriendshipBracelet.MOD_ID, "textures/screen/friendship.png");
    private final FriendshipData friendshipData;
    private @Nullable GameProfile friendGameProfile;

    public FriendshipScreen(FriendshipContainer menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 232;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = this.imageHeight - 93;
        this.friendshipData = menu.getFriendshipData();
        this.updateGameProfile();
    }

    /**
     * 更新 GameProfile，从而获取玩家头像
     */
    private void updateGameProfile() {
        if (friendIdIsEmpty()) {
            return;
        }
        GameProfile emptyGameProfile = new GameProfile(this.friendshipData.getFriendId(), this.friendshipData.getFriendName());
        SkullBlockEntity.fetchGameProfile(this.friendshipData.getFriendId()).thenApply(gameProfile -> this.friendGameProfile = gameProfile.orElse(emptyGameProfile));
    }

    @Override
    protected void init() {
        super.init();

        // 传送按钮
        Button teleportButton = Button.builder(Component.translatable("screen.friendship_bracelet.friendship.teleport"), b -> {
            PacketDistributor.sendToServer(RequestServerPayload.teleportToFriend());
            this.onClose();
        }).size(123, 20).pos(leftPos + 45, topPos + 30).build();
        this.addRenderableWidget(teleportButton);

        // 如果朋友离线，那么传送按钮无法启用
        if (!friendIsOnline(getMinecraft().player)) {
            teleportButton.active = false;
        }

        // 是否分享自己的物品栏
        this.addRenderableWidget(Checkbox.builder(CommonComponents.EMPTY, font).selected(friendshipData.isMyselfShareInv()).onValueChange((checkbox, value) -> {
            PacketDistributor.sendToServer(RequestServerPayload.changeShareInventory());
            friendshipData.setMyselfShareInv(value);
        }).pos(leftPos + 151, topPos + 62).build());
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        // 渲染背景
        graphics.blit(BG, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        // 当没有朋友数据，或者朋友没有分享物品栏，显示灰色界面
        if (this.friendIdIsEmpty() || !friendshipData.isFriendShareInv()) {
            graphics.fill(leftPos + 7, topPos + 83, leftPos + 169, topPos + 137, 0xEA404040);
            graphics.blit(BG, leftPos + 82, topPos + 104, 176, 0, 11, 11);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        // 渲染朋友头像
        this.renderFriendAvatar(graphics);
        // 渲染朋友名字
        if (getMinecraft().player != null) {
            this.renderFriendName(graphics, getMinecraft().player);
        }
        // 渲染是否分享自己背包的按钮
        this.renderShareButton(graphics);
        // 渲染文本提示
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    private void renderFriendAvatar(GuiGraphics graphics) {
        ResourceLocation skin;
        if (!this.friendIdIsEmpty() && friendGameProfile != null) {
            skin = getMinecraft().getSkinManager().getInsecureSkin(friendGameProfile).texture();
        } else {
            skin = DefaultPlayerSkin.get(friendshipData.getFriendId()).texture();
        }
        graphics.blit(skin, leftPos + 8, topPos + 18, 32, 32, 32, 32, 256, 256);
    }

    private void renderFriendName(GuiGraphics graphics, LocalPlayer player) {
        // 没朋友时，渲染提示
        if (friendIdIsEmpty()) {
            MutableComponent emptyText = Component.translatable("screen.friendship_bracelet.friendship.player.empty");
            graphics.drawString(font, emptyText, leftPos + 45, topPos + 20, 0xAA0000, false);
            return;
        }
        // 朋友在线或者离线
        String friendName = friendshipData.getFriendName();
        if (this.friendIsOnline(player)) {
            MutableComponent onlineText = Component.translatable("screen.friendship_bracelet.friendship.player.online", friendName);
            graphics.drawString(font, onlineText, leftPos + 45, topPos + 20, ChatFormatting.DARK_PURPLE.getColor(), false);
        } else {
            MutableComponent offlineText = Component.translatable("screen.friendship_bracelet.friendship.player.offline", friendName);
            graphics.drawString(font, offlineText, leftPos + 45, topPos + 20, 0x404040, false);
        }
    }

    private void renderShareButton(GuiGraphics graphics) {
        MutableComponent checkboxText = Component.translatable("screen.friendship_bracelet.friendship.share");
        graphics.drawString(font, checkboxText, leftPos + 148 - font.width(checkboxText), topPos + 66, 0x404040, false);
    }

    private boolean friendIsOnline(@Nullable LocalPlayer player) {
        if (player == null) {
            return false;
        }
        if (friendIdIsEmpty()) {
            return false;
        }
        Collection<PlayerInfo> players = player.connection.getListedOnlinePlayers();
        final UUID friendId = friendshipData.getFriendId();
        return players.stream().anyMatch(info -> info.getProfile().getId().equals(friendId));
    }

    private boolean friendIdIsEmpty() {
        return Util.NIL_UUID.equals(friendshipData.getFriendId());
    }
}
