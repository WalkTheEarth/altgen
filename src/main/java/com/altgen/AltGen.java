package com.altgen;

import com.altgen.gui.AltGenConfigScreen;
import com.altgen.gui.AltGenLoginScreen;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.Systems;
import net.minecraft.client.gui.screens.Screen;
import org.slf4j.Logger;

public class AltGen extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        LOG.info("Initializing AltGen");

        // Config (API key persistence)
        Systems.add(new AltGenConfig());
        AltGenConfig.get().load();

        // Track the last server connection so we can reconnect after a kick/ban
        MeteorClient.EVENT_BUS.subscribe(new AltGenServerTracker());
    }

    public static void openLoginScreen(Screen parent) {
        if (AltGenConfig.get().apiKey.get().isEmpty()) {
            MeteorClient.mc.gui.setScreen(new AltGenConfigScreen(parent));
        } else {
            MeteorClient.mc.gui.setScreen(new AltGenLoginScreen());
        }
    }

    @Override
    public String getPackage() {
        return "com.altgen";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("walktheearth", "altgen");
    }
}
