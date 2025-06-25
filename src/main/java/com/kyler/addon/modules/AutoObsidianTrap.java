package com.kyler.addon.modules;

import com.kyler.addon.KylerAddon;
import com.kyler.addon.utils.blocks.BlockUtils;
import com.kyler.addon.utils.entities.EntityUtils;
import com.kyler.addon.utils.InventoryUtils;
import com.kyler.addon.utils.RenderUtils;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class AutoObsidianTrap extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgRender = settings.createGroup("Render");

    private final Setting<Double> range = sgGeneral.add(new DoubleSetting.Builder()
        .name("range")
        .description("Range to detect players.")
        .defaultValue(5.0)
        .range(3.0, 8.0)
        .build()
    );

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("Ticks between block placements.")
        .defaultValue(2)
        .range(0, 10)
        .build()
    );

    private final Setting<SettingColor> color = sgRender.add(new ColorSetting.Builder()
        .name("color")
        .description("Color of trap preview.")
        .defaultValue(new SettingColor(255, 0, 0))
        .build()
    );

    private int tickCounter = 0;

    public AutoObsidianTrap() {
        super(KylerAddon.CATEGORY, "auto-obsidian-trap", "Traps nearby players in obsidian.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (tickCounter++ < delay.get()) return;
        tickCounter = 0;

        if (!InventoryUtils.hasItem(Blocks.OBSIDIAN.asItem())) return;

        PlayerEntity target = EntityUtils.getClosestPlayer(mc.player.getPos(), range.get());
        if (target == null) return;

        BlockPos playerPos = target.getBlockPos();
        for (BlockPos pos : new BlockPos[] {
            playerPos.up(2), playerPos.down(), playerPos.north(), playerPos.south(), playerPos.east(), playerPos.west()
        }) {
            BlockUtils.placeBlock(pos, Blocks.OBSIDIAN, Hand.MAIN_HAND);
        }
    }

    @EventHandler
    private void onRender3d(Render3DEvent event) {
        PlayerEntity target = EntityUtils.getClosestPlayer(mc.player.getPos(), range.get());
        if (target == null) return;

        BlockPos playerPos = target.getBlockPos();
        for (BlockPos pos : new BlockPos[] {
            playerPos.up(2), playerPos.down(), playerPos.north(), playerPos.south(), playerPos.east(), playerPos.west()
        }) {
            RenderUtils.renderBox(event, pos, color.get());
        }
    }
}
