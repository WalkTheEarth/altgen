package com.altgen.gui;

import com.altgen.AltGen;
import com.altgen.AltGenConfig;
import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.gui.WindowScreen;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;
import meteordevelopment.meteorclient.gui.widgets.input.WTextBox;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import net.minecraft.client.gui.screens.Screen;

public class AltGenConfigScreen extends WindowScreen {
    private final Screen parent;

    public AltGenConfigScreen(Screen parent) {
        super(GuiThemes.get(), "AltGen Setup");

        this.parent = parent;
    }

    @Override
    public void initWidgets() {
        WTable t = add(theme.table()).widget();

        t.add(theme.label("TheAltening API key (from thealtening.com/account):"));
        t.row();

        WTextBox key = t.add(theme.textBox(AltGenConfig.get().apiKey.get(), "API key")).minWidth(400).expandX().widget();
        key.action = () -> AltGenConfig.get().apiKey.set(key.get().trim());

        t.row();

        WButton save = t.add(theme.button("Save")).expandX().widget();
        save.action = () -> {
            AltGenConfig.get().save();

            if (!AltGenConfig.get().apiKey.get().isEmpty()) {
                onClose();
                AltGen.openLoginScreen(null);
            }
        };
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
