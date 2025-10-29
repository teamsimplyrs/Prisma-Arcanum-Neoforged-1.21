package com.teamsimplyrs.prismaarcanum.api.utils;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class PlayerUtils {
    public static void moveItemStackToPlayerInventory(Player player, ItemStack stack, AbstractContainerMenu source, int sourceSlot) {
        int playerInvFreeSlot = player.getInventory().getFreeSlot();

        if (sourceSlot != -1 && playerInvFreeSlot != -1 && source.getSlot(sourceSlot).getItem().equals(stack)) {
            ItemStack copyStack = stack.copy();
            source.getSlot(sourceSlot).remove(stack.getCount());
            player.getInventory().setItem(playerInvFreeSlot, copyStack);
        }
    }
}
