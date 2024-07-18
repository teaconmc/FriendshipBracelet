package com.github.teaconmc.friendshipbracelet.inventory;

import com.github.teaconmc.friendshipbracelet.entity.data.FriendshipData;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

public class FriendshipContainer extends AbstractContainerMenu {
    public static final MenuType<FriendshipContainer> TYPE = IMenuTypeExtension.create((windowId, inv, buf) -> new FriendshipContainer(windowId, inv, FriendshipData.STREAM_CODEC.decode(buf)));

    private final FriendshipData friendshipData;

    public FriendshipContainer(int id, Inventory inventory, FriendshipData friendshipData) {
        super(TYPE, id);
        this.addPlayerSlots(inventory);
        this.friendshipData = friendshipData;
    }

    public FriendshipData getFriendshipData() {
        return friendshipData;
    }

    private void addPlayerSlots(Inventory inventory) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 150 + row * 18));
            }
        }

        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(inventory, col, 8 + col * 18, 208));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }
}
