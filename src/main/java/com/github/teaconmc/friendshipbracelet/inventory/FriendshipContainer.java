package com.github.teaconmc.friendshipbracelet.inventory;

import com.github.teaconmc.friendshipbracelet.entity.data.FriendshipData;
import net.minecraft.Util;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FriendshipContainer extends AbstractContainerMenu {
    public static final MenuType<FriendshipContainer> TYPE = IMenuTypeExtension.create((windowId, inv, buf) -> new FriendshipContainer(windowId, inv, FriendshipData.STREAM_CODEC.decode(buf)));
    private final FriendshipData friendshipData;
    private @Nullable Player friendPlayer = null;

    public FriendshipContainer(int id, Inventory inventory, FriendshipData friendshipData) {
        super(TYPE, id);
        this.addPlayerSlots(inventory);
        this.friendshipData = friendshipData;
        this.handlerFriendInventory(inventory, friendshipData);
    }

    @Override
    @SuppressWarnings("all")
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack1 = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        int indexSize = this.friendshipData.isFriendShareInv() ? 36 : 9;
        boolean reverseDirection = this.friendshipData.isFriendShareInv();
        if (slot != null && slot.hasItem()) {
            ItemStack stack2 = slot.getItem();
            stack1 = stack2.copy();
            if (index < indexSize) {
                if (!this.moveItemStackTo(stack2, indexSize, this.slots.size(), !reverseDirection)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack2, 0, indexSize, reverseDirection)) {
                return ItemStack.EMPTY;
            }
            if (stack2.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return stack1;
    }

    @Override
    public boolean stillValid(Player player) {
        if (!player.isAlive()) {
            return false;
        }
        // 如果朋友离线了，那么立马关闭界面
        return friendPlayer == null || friendPlayer.isAlive();
    }

    public FriendshipData getFriendshipData() {
        return friendshipData;
    }

    private void addPlayerSlots(Inventory inventory) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 150 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(inventory, col, 8 + col * 18, 208));
        }
    }

    private void handlerFriendInventory(Inventory inventory, FriendshipData friendshipData) {
        // UUID 为空，不进行添加
        UUID friendId = friendshipData.getFriendId();
        if (Util.NIL_UUID.equals(friendId)) {
            return;
        }
        // 如果朋友分享物品栏，那么显示物品栏
        if (friendshipData.isFriendShareInv()) {
            if (inventory.player instanceof ServerPlayer player) {
                // 服务端，尝试获取朋友实体并添加
                this.friendPlayer = player.server.getPlayerList().getPlayer(friendId);
                if (this.friendPlayer != null) {
                    addFriendSlots(this.friendPlayer.getInventory());
                }
            } else {
                // 客户端，由于朋友可能不在旁边，直接 new 一个空的
                addFriendSlots(new SimpleContainer(36));
            }
        }
    }

    private void addFriendSlots(Container container) {
        // 朋友只加背包，不加快捷栏，朋友之间也是有隐私的
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(container, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
    }
}
