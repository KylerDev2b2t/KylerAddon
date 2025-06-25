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
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.List;

public class PortalTrapESP extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgRender = settings.createGroup("Render");

    private final Setting<Double> scanRadius = sgGeneral.add(new DoubleSetting.Builder()
        .name("scan-radius")
        .description("Radius to scan for portal traps.")
        .defaultValue(5.0)
        .range(3.0, 10.0)
        .build()
    );

    private final Setting<List<Block>> dangerBlocks = sgGeneral.add(new BlockListSetting.Builder()
        .name("danger-blocks")
        .description("Blocks indicating a trap.")
        .defaultValue(Arrays.asList(Blocks.LAVA, Blocks.OBSIDIAN))
        .build()
    );

    private final Setting<SettingColor> color = sgRender.add(new ColorSetting.Builder()
        .name("color")
        .description("Color of highlighted portals.")
        .defaultValue(new SettingColor(255, 0, 0))
        .build()
    );

    public PortalTrapESP() {
        super(KylerAddon.CATEGORY, "portal-trap-esp", "Highlights potential Nether portal traps.");
    }

    @EventHandler
    private void onRender3d(Render3DEvent event) {
        List<BlockPos> portals = AddonUtils.scanBlocks(mc.world, mc.player.getBlockPos(), scanRadius.get(), List.of(Blocks.NETHER_PORTAL));
        for (BlockPos portal : portals) {
            if (isTrapped(portal)) {
                event.renderer.box(portal, color.get(), color.get(), ShapeMode.Both, 0);
                mc.player.sendMessage(Text.of("Warning: Potential portal trap at " + portal.toShortString()));
            }
        }
    }

    private boolean isTrapped(BlockPos portal) {
        int r = 2;
        for (BlockPos pos : BlockPos.iterate(
            portal.getX() - r, portal.getY() - r, portal.getZ() - r,
            portal.getX() + r, portal.getY() + r, portal.getZ() + r
        )) {
            if (dangerBlocks.get().contains(mc.world.getBlockState(pos).getBlock())) {
                return true;
            }
        }
        return false;
    }
}
