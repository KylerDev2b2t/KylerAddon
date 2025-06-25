package com.kyler.addon.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InventoryUtils {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static boolean hasItem(Item item) {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == item) {
                return true;
            }
        }
        return false;
    }

    public static void selectItem(Item item) {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == item) {
                mc.player.getInventory().selectedSlot = i;
                return;
            }
        }
    }

    public static int countItem(Item item) {
        int count = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() == item) {
                count += stack.getCount();
            }
        }
        return count;
    }
}
