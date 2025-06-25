package com.kyler.addon.utils;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class AddonUtils {
    public static List<BlockPos> scanBlocks(World world, BlockPos center, double radius, List<Block> targetBlocks) {
        List<BlockPos> found = new ArrayList<>();
        int r = (int) radius;
        for (BlockPos pos : BlockPos.iterate(
            center.getX() - r, center.getY() - r, center.getZ() - r,
            center.getX() + r, center.getY() + r, center.getZ() + r
        )) {
            if (targetBlocks.contains(world.getBlockState(pos).getBlock())) {
                found.add(pos.toImmutable());
            }
        }
        return found;
    }

    public static PlayerEntity getClosestPlayer(World world, Vec3d pos, double maxDistance) {
        PlayerEntity closest = null;
        double minDist = maxDistance;
        for (PlayerEntity player : world.getPlayers()) {
            double dist = player.getPos().distanceTo(pos);
            if (dist < minDist && !player.isSpectator()) {
                closest = player;
                minDist = dist;
            }
        }
        return closest;
    }

    public static boolean isSafeFromPlayers(World world, Vec3d pos, double safeDistance) {
        for (PlayerEntity player : world.getPlayers()) {
            if (player.getPos().distanceTo(pos) < safeDistance && !player.isSpectator()) {
                return false;
            }
        }
        return true;
    }
}
