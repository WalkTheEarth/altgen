package com.altgen;

import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.Settings;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.System;
import meteordevelopment.meteorclient.systems.Systems;
import net.minecraft.nbt.CompoundTag;

public class AltGenConfig extends System<AltGenConfig> {
    public final Settings settings = new Settings();
    private final SettingGroup sgGeneral = settings.createGroup("General");

    public final Setting<String> apiKey = sgGeneral.add(new StringSetting.Builder()
        .name("api-key")
        .description("Your TheAltening API key.")
        .defaultValue("")
        .build()
    );

    public AltGenConfig() {
        super("altgen");
    }

    public static AltGenConfig get() {
        return Systems.get(AltGenConfig.class);
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.put("settings", settings.toTag());
        return tag;
    }

    @Override
    public AltGenConfig fromTag(CompoundTag tag) {
        settings.fromTag(tag.getCompound("settings").orElse(new CompoundTag()));
        return this;
    }
}
