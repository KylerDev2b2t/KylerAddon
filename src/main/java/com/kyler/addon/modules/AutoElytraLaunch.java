package com.kyler.addon.modules;

import com.kyler.addon.KylerAddon;
import com.kyler.addon.utils.InventoryUtils;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class AutoElytraLaunch extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> pitch = sgGeneral.add(new DoubleSetting.Builder()
        .name("pitch")
        .description("Pitch angle for takeoff.")
        .defaultValue(30.0)
        .range(0.0, 90.0)
        .build()
    );

    private final Setting<Integer> rocketDelay = sgGeneral.add(new IntSetting.Builder()
        .name("rocket-delay")
        .description("Ticks between rocket uses.")
        .defaultValue(5)
        .range(0, 20)
        .build()
    );

    private int tickCounter = 0;

    public AutoElytraLaunch() {
        super(KylerAddon.CATEGORY, "auto-elytra-launch", "Automates elytra takeoffs with rockets.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (!mc.player.isFallFlying() || !InventoryUtils.hasItem(Items.FIREWORK_ROCKET)) return;

        if (tickCounter++ < rocketDelay.get()) return;
        tickCounter = 0;

        mc.player.setPitch(pitch.get().floatValue());
        InventoryUtils.selectItem(Items.FIREWORK_ROCKET);
        mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
    }
}
