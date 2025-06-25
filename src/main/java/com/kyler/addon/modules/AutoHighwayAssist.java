package com.kyler.addon.modules;

import com.kyler.addon.KylerAddon;
import com.kyler.addon.utils.AddonUtils;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class AutoHighwayAssist extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Direction> axis = sgGeneral.add(new EnumSetting.Builder<Direction>()
        .name("axis")
        .description("Highway direction to dig or place along.")
        .defaultValue(Direction.NORTH)
        .build()
    );

    private final Setting<Integer> width = sgGeneral.add(new IntSetting.Builder()
        .name("width")
        .description("Width of the highway tunnel.")
        .defaultValue(3)
        .range(2, 5)
        .build()
    );

    private final Setting<Block> placeBlock = sgGeneral.add(new BlockSetting.Builder()
        .name("block-type")
        .description("Block to place for highway.")
        .defaultValue(Blocks.OBSIDIAN)
        .build()
    );

    private final Setting<Boolean> autoSwitch = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-switch")
        .description("Automatically switch to pickaxe or block.")
        .defaultValue(true)
        .build()
    );

    public AutoHighwayAssist() {
        super(KylerAddon.CATEGORY, "auto-highway-assist", "Automates digging or placing blocks along Nether highways.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (!AddonUtils.isSafeFromPlayers(mc.world, mc.player.getPos(), 10.0)) {
            return;
        }

        BlockPos playerPos = mc.player.getBlockPos();
        Direction dir = axis.get();
        int w = width.get();

        for (int x = -w / 2; x <= w / 2; x++) {
            for (int y = 0; y <= 2; y++) {
                BlockPos pos = playerPos.offset(dir, 1).add(x, y, 0);
                if (dir.getAxis() == Direction.Axis.Z) {
                    pos = playerPos.offset(dir, 1).add(0, y, x);
                }

                if (!mc.world.getBlockState(pos).isAir()) {
                    if (autoSwitch.get()) {
                        selectPickaxe();
                    }
                    mc.interactionManager.attackBlock(pos, Direction.UP);
                } else if (placeBlock.get() != Blocks.AIR) {
                    if (autoSwitch.get()) {
                        selectBlock(placeBlock.get());
                    }
                    BlockHitResult hitResult = new BlockHitResult(
                        Vec3d.ofCenter(pos),
                        Direction.UP,
                        pos.down(),
                        false
                    );
                    mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hitResult);
                }
            }
        }
    }

    private void selectPickaxe() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() instanceof PickaxeItem) {
                mc.player.getInventory().selectedSlot = i;
                return;
            }
        }
    }

    private void selectBlock(Block block) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() == block.asItem()) {
                mc.player.getInventory().selectedSlot = i;
                return;
            }
        }
    }
}
