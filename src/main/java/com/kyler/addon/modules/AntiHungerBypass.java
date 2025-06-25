package com.kyler.addon.modules;

import com.kyler.addon.KylerAddon;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;

public class AntiHungerBypass extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> sprint = sgGeneral.add(new BoolSetting.Builder()
        .name("sprint")
        .description("Prevent hunger depletion while sprinting.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> mining = sgGeneral.add(new BoolSetting.Builder()
        .name("mining")
        .description("Prevent hunger depletion while mining.")
        .defaultValue(true)
        .build()
    );

    public AntiHungerBypass() {
        super(KylerAddon.CATEGORY, "anti-hunger-bypass", "Reduces hunger depletion during activities.");
    }

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        if (sprint.get() && event.packet instanceof ClientCommandC2SPacket packet) {
            if (packet.getMode() == ClientCommandC2SPacket.Mode.START_SPRINTING) {
                event.cancel();
            }
        }
        if (mining.get() && event.packet instanceof PlayerActionC2SPacket packet) {
            if (packet.getAction() == PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) {
                event.cancel();
            }
        }
    }
}
