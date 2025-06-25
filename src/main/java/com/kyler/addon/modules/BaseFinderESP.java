package com.kyler.addon.modules;

import com.kyler.addon.KylerAddon;
import com.kyler.addon.utils.AddonUtils;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.List;

public class BaseFinderESP extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgRender = settings.createGroup("Render");

    private final Setting<List<Block>> blockFilter = sgGeneral.add(new BlockListSetting.Builder()
        .name("block-filter")
        .description("Blocks to highlight.")
        .defaultValue(Arrays.asList(Blocks.CHEST, Blocks.SHULKER_BOX, Blocks.FURNACE))
        .build()
    );

    private final Setting<Double> range = sgGeneral.add(new DoubleSetting.Builder()
        .name("range")
        .description("Scan radius for blocks.")
        .defaultValue(32.0)
        .range(16.0, 128.0)
        .build()
    );

    private final Setting<SettingColor> color = sgRender.add(new ColorSetting.Builder()
        .name("color")
        .description("Color of highlighted blocks.")
        .defaultValue(new SettingColor(255, 0, 255))
        .build()
    );

    public BaseFinderESP() {
        super(KylerAddon.CATEGORY, "base-finder-esp", "Highlights blocks commonly found in bases.");
    }

    @EventHandler
    private void onRender3d(Render3DEvent event) {
        List<BlockPos> blocks = AddonUtils.scanBlocks(mc.world, mc.player.getBlockPos(), range.get(), blockFilter.get());
        for (BlockPos pos : blocks) {
            event.renderer.box(pos, color.get(), color.get(), ShapeMode.Both, 0);
        }
    }
}
