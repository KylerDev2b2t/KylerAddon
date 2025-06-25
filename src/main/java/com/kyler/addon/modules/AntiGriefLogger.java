package com.kyler.addon.modules;

import com.kyler.addon.KylerAddon;
import com.kyler.addon.utils.AddonUtils;
import meteordevelopment.meteorclient.events.world.BlockUpdateEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class AntiGriefLogger extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgRender = settings.createGroup("Render");

    private final Setting<BlockPos> centerPos = sgGeneral.add(new BlockPosSetting.Builder()
        .name("center-pos")
        .description("Center of the base to monitor.")
        .defaultValue(BlockPos.ORIGIN)
        .build()
    );

    private final Setting<Double> radius = sgGeneral.add(new DoubleSetting.Builder()
        .name("radius")
        .description("Monitoring radius.")
        .defaultValue(32.0)
        .range(16.0, 64.0)
        .build()
    );

    private final Setting<SettingColor> color = sgRender.add(new ColorSetting.Builder()
        .name("color")
        .description("Color of highlighted changed blocks.")
        .defaultValue(new SettingColor(255, 0, 0))
        .build()
    );

    public AntiGriefLogger() {
        super(KylerAddon.CATEGORY, "anti-grief-logger", "Logs block changes near a base.");
    }

    @EventHandler
    private void onBlockUpdate(BlockUpdateEvent event) {
        BlockPos pos = event.pos;
        if (pos.isWithinDistance(centerPos.get(), radius.get())) {
            String message = "Block change at " + pos.toShortString() + ": " + event.oldState.getBlock().getName().getString() + " -> " + event.newState.getBlock().getName().getString();
            mc.player.sendMessage(Text.of(message));
        }
    }
}
