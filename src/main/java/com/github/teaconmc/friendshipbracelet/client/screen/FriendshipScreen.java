package com.github.teaconmc.friendshipbracelet.client.screen;

import com.github.teaconmc.friendshipbracelet.FriendshipBracelet;
import com.github.teaconmc.friendshipbracelet.entity.data.FriendshipData;
import com.github.teaconmc.friendshipbracelet.inventory.FriendshipContainer;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class FriendshipScreen extends AbstractContainerScreen<FriendshipContainer> {
    private static final ResourceLocation BG = ResourceLocation.fromNamespaceAndPath(FriendshipBracelet.MOD_ID, "textures/screen/friendship.png");

    private final FriendshipData friendshipData;
    private @Nullable GameProfile friendGameProfile;
    private boolean playerOffline = true;

    public FriendshipScreen(FriendshipContainer menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 232;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = this.imageHeight - 93;
        this.friendshipData = menu.getFriendshipData();
        GameProfile emptyGameProfile = new GameProfile(this.friendshipData.getFriendId(), this.friendshipData.getFriendName());
        SkullBlockEntity.fetchGameProfile(this.friendshipData.getFriendId()).thenApply(gameProfile -> this.friendGameProfile = gameProfile.orElse(emptyGameProfile));
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(Button.builder(Component.translatable("screen.friendship_bracelet.friendship.teleport"),
                        b -> {
                        })
                .size(123, 20)
                .pos(leftPos + 45, topPos + 30).build());
        this.addRenderableWidget(Checkbox.builder(CommonComponents.EMPTY, font)
                .selected(friendshipData.isShareInv())
                .onValueChange((checkbox, value) -> friendshipData.setShareInv(value))
                .pos(leftPos + 151, topPos + 62).build());
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BG);
        graphics.blit(BG, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        ResourceLocation skin;

        if (friendGameProfile != null) {
            skin = getMinecraft().getSkinManager().getInsecureSkin(friendGameProfile).texture();
        } else {
            skin = DefaultPlayerSkin.get(friendshipData.getFriendId()).texture();
        }
        RenderSystem.setShaderTexture(0, skin);
        graphics.blit(skin, leftPos + 8, topPos + 18, 32, 32, 32, 32, 256, 256);

        if (getMinecraft().player != null) {
            Collection<PlayerInfo> players = getMinecraft().player.connection.getListedOnlinePlayers();
            if (players.stream().anyMatch(info -> info.getProfile().getId().equals(friendshipData.getFriendId()))) {
                graphics.drawString(font, Component.translatable("screen.friendship_bracelet.friendship.player.online", friendshipData.getFriendName()), leftPos + 45, topPos + 20, 0x404040, false);
            } else {
                graphics.drawString(font, Component.translatable("screen.friendship_bracelet.friendship.player.offline", friendshipData.getFriendName()), leftPos + 45, topPos + 20, 0x404040, false);
            }
        }
        MutableComponent checkboxText = Component.translatable("screen.friendship_bracelet.friendship.share");
        graphics.drawString(font, checkboxText, leftPos + 148 - font.width(checkboxText), topPos + 66, 0x404040, false);
    }
}
