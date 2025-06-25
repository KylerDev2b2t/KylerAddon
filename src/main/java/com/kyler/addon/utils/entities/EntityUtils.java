package com.kyler.addon.utils.entities;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class EntityUtils {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static PlayerEntity getClosestPlayer(Vec3d pos, double maxDistance) {
        PlayerEntity closest = null;
        double minDist = maxDistance;
        for (PlayerEntity player : mc.world.getPlayers()) {
            double dist = player.getPos().distanceTo(pos);
            if (dist < minDist && !player.isSpectator() && player != mc.player) {
                closest = player;
                minDist = dist;
            }
        }
        return closest;
    }

    public static boolean isSafeFromPlayers(Vec3d pos, double safeDistance) {
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player.getPos().distanceTo(pos) < safeDistance && !player.isSpectator() && player != mc.player) {
                return false;
            }
        }
        return true;
    }

    public static <T extends Entity> List<T> getEntitiesInBox(Class<T> entityClass, Box box) {
        return mc.world.getEntitiesByClass(entityClass, box, e -> true);
    }
}
