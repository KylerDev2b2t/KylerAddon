package com.kyler.addon.modules;

import com.kyler.addon.KylerAddon;
import com.kyler.addon.utils.AddonUtils;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class CrystalAuraLite extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgRender = settings.createGroup("Render");

    private final Setting<Double> range = sgGeneral.add(new DoubleSetting.Builder()
        .name("range")
        .description("Range for crystal placement and detonation.")
        .defaultValue(4.5)
        .range(3.0, 6.0)
        .build()
    );

    private final Setting<Boolean> antiSelf = sgGeneral.add(new BoolSetting.Builder()
        .name("anti-self")
        .description("Prevent placing crystals that could harm you.")
        .defaultValue(true)
        .build()
    );

    private final Setting<SettingColor> color = sgRender.add(new ColorSetting.Builder()
        .name("color")
        .description("Color of placement preview.")
        .defaultValue(new SettingColor(255, 0, 255))
        .build()
    );

    public CrystalAuraLite() {
        super(KylerAddon.CATEGORY, "crystal-aura-lite", "Automates end crystal placement and detonation.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (!AddonUtils.isSafeFromPlayers(mc.world, mc.player.getPos(), 10.0)) {
            return;
        }

        BlockPos target = findCrystalPlacement();
        if (target != null) {
            if (antiSelf.get() && mc.player.getPos().distanceTo(Vec3d.ofCenter(target)) < 2.0) {
                return;
            }
            selectCrystal();
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, new BlockHitResult(Vec3d.ofCenter(target), Direction.UP, target, false));
        }

        for (EndCrystalEntity crystal : mc.world.getEntitiesByClass(EndCrystalEntity.class, new Box(mc.player.getPos(), mc.player.getPos()).expand(range.get()), e -> true)) {
            if (AddonUtils.getClosestPlayer(mc.world, crystal.getPos(), range.get()) != null) {
                mc.interactionManager.attackEntity(mc.player, crystal);
            }
        }
    }

    @EventHandler
    private void onRender3d(Render3DEvent event) {
        BlockPos target = findCrystalPlacement();
        if (target != null) {
            event.renderer.box(target, color.get(), color.get(), ShapeMode.Both, 0);
        }
    }

    private BlockPos findCrystalPlacement() {
        BlockPos playerPos = mc.player.getBlockPos();
        int r = range.get().intValue();
        for (BlockPos pos : BlockPos.iterate(
            playerPos.getX() - r, playerPos.getY() - 1, playerPos.getZ() - r,
            playerPos.getX() + r, playerPos.getY() + 1, playerPos.getZ() + r
        )) {
            if ((mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK)
                && mc.world.getBlockState(pos.up()).isAir()) {
                return pos;
            }
        }
        return null;
    }

    private void selectCrystal() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == Items.END_CRYSTAL) {
                mc.player.getInventory().selectedSlot = i;
                return;
            }
        }
    }
}
