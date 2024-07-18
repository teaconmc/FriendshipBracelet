package com.github.teaconmc.friendshipbracelet.client.screen;

import com.github.teaconmc.friendshipbracelet.FriendshipBracelet;
import com.github.teaconmc.friendshipbracelet.inventory.FriendshipContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FriendshipScreen extends AbstractContainerScreen<FriendshipContainer> {
    private static final ResourceLocation BG = ResourceLocation.fromNamespaceAndPath(FriendshipBracelet.MOD_ID, "textures/screen/friendship.png");

    public FriendshipScreen(FriendshipContainer menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BG);
        graphics.blit(BG, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }
}
