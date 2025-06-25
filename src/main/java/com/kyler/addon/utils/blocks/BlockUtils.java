package com.kyler.addon.utils.blocks;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.List;

public class BlockUtils {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static boolean placeBlock(BlockPos pos, Block block, Hand hand) {
        if (mc.world.getBlockState(pos).isAir()) {
            selectItem(block.asItem());
            BlockHitResult hitResult = new BlockHitResult(Vec3d.ofCenter(pos), Direction.UP, pos.down(), false);
            mc.interactionManager.interactBlock(mc.player, hand, hitResult);
            return true;
        }
        return false;
    }

    public static boolean breakBlock(BlockPos pos) {
        if (!mc.world.getBlockState(pos).isAir()) {
            mc.interactionManager.attackBlock(pos, Direction.UP);
            return true;
        }
        return false;
    }

    public static List<BlockPos> findBlocksInRadius(BlockPos center, double radius, List<Block> targetBlocks) {
        List<BlockPos> found = new ArrayList<>();
        int r = (int) radius;
        for (BlockPos pos : BlockPos.iterate(
            center.getX() - r, center.getY() - r, center.getZ() - r,
            center.getX() + r, center.getY() + r, center.getZ() + r
        )) {
            if (targetBlocks.contains(mc.world.getBlockState(pos).getBlock())) {
                found.add(pos.toImmutable());
            }
        }
        return found;
    }

    private static void selectItem(net.minecraft.item.Item item) {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == item) {
                mc.player.getInventory().selectedSlot = i;
                return;
            }
        }
    }
}
