package com.kyler.addon.modules;

import com.kyler.addon.KylerAddon;
import com.kyler.addon.utils.blocks.BlockUtils;
import com.kyler.addon.utils.RenderUtils;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.ChunkDataEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StashSniffer extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgRender = settings.createGroup("Render");

    private final Setting<Double> range = sgGeneral.add(new DoubleSetting.Builder()
        .name("range")
        .description("Scan radius per chunk.")
        .defaultValue(16.0)
        .range(8.0, 32.0)
        .build()
    );

    private final Setting<SettingColor> color = sgRender.add(new ColorSetting.Builder()
        .name("color")
        .description("Color of stash highlights.")
        .defaultValue(new SettingColor(0, 255, 0))
        .build()
    );

    private final List<BlockPos> foundStashes = new ArrayList<>();

    public StashSniffer() {
        super(KylerAddon.CATEGORY, "stash-sniffer", "Detects buried chests or shulkers.");
    }

    @EventHandler
    private void onChunkData(ChunkDataEvent event) {
        BlockPos center = new BlockPos(event.chunk().getPos().getCenterX(), 0, event.chunk().getPos().getCenterZ());
        List<BlockPos> stashes = BlockUtils.findBlocksInRadius(center, range.get(), Arrays.asList(Blocks.CHEST, Blocks.SHULKER_BOX));
        for (BlockPos pos : stashes) {
            if (!foundStashes.contains(pos)) {
                foundStashes.add(pos.toImmutable());
                mc.player.sendMessage(Text.of("Found stash at " + pos.toShortString()));
            }
        }
    }

    @EventHandler
    private void onRender3d(Render3DEvent event) {
        for (BlockPos pos : foundStashes) {
            RenderUtils.renderBox(event, pos, color.get());
        }
    }
}
