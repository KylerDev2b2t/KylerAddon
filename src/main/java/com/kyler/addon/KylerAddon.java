package com.kyler.addon;

import com.kyler.addon.commands.CommandExample;
import com.kyler.addon.hud.HudExample;
import com.kyler.addon.modules.*;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

public class KylerAddon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("Kyler");
    public static final HudGroup HUD_GROUP = new HudGroup("Kyler");

    @Override
    public void onInitialize() {
        LOG.info("Initializing Kyler Addon");

        // Modules
        Modules.get().add(new AntiGriefLogger());
        Modules.get().add(new AntiHungerBypass());
        Modules.get().add(new AutoHighwayAssist());
        Modules.get().add(new BaseFinderESP());
        Modules.get().add(new ChunkBanDetector());
        Modules.get().add(new CrystalAuraLite());
        Modules.get().add(new PortalTrapESP());
        Modules.get().add(new AutoObsidianTrap());
        Modules.get().add(new StashSniffer());
        Modules.get().add(new AutoElytraLaunch());

        // Commands
        Commands.add(new CommandExample());

        // HUD
        Hud.get().register(HudExample.INFO);
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "com.kyler.addon";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("Kyler", "kyler-addon");
    }
}
