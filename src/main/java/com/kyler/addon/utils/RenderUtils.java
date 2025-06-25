package com.kyler.addon.utils;

import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.util.math.BlockPos;

public class RenderUtils {
    public static void renderBox(Render3DEvent event, BlockPos pos, Color color) {
        event.renderer.box(pos, color, color, ShapeMode.Both, 0);
    }

    public static void renderBox(Render3DEvent event, BlockPos pos, Color sideColor, Color lineColor) {
        event.renderer.box(pos, sideColor, lineColor, ShapeMode.Both, 0);
    }
}
