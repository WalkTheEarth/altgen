package com.altgen.gui;

import com.altgen.AlteningApi;
import com.altgen.AltGen;
import com.altgen.AltGenConfig;
import com.altgen.AltGenServerTracker;
import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.gui.WindowScreen;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import meteordevelopment.meteorclient.systems.accounts.Accounts;
import meteordevelopment.meteorclient.systems.accounts.types.TheAlteningAccount;
import meteordevelopment.meteorclient.utils.network.MeteorExecutor;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class AltGenLoginScreen extends WindowScreen {
    private final ServerAddress address;
    private final ServerData server;

    public AltGenLoginScreen() {
        super(GuiThemes.get(), "AltGen");

        // WidgetScreen.parent is the screen that was open when this one opened,
        // which is the disconnect screen when launched from our button.
        if (parent instanceof DisconnectedScreen && AltGenServerTracker.lastConnection() != null) {
            this.address = AltGenServerTracker.lastConnection().left();
            this.server = AltGenServerTracker.lastConnection().right();
        } else {
            this.address = null;
            this.server = null;
        }
    }

    @Override
    public void initWidgets() {
        WTable t = add(theme.table()).widget();

        // Status label
        WTable status = t.add(theme.table()).widget();
        status.add(theme.label("Generates a fresh Altening account and logs into it.")).widget().color = theme.textSecondaryColor();
        t.row();

        // Start button
        WButton start = t.add(theme.button("Generate & Login")).expandX().widget();
        start.action = () -> {
            start.visible = false;
            run(status);
        };
        t.row();

        // Back button
        WButton back = t.add(theme.button("Back")).expandX().widget();
        back.action = this::onClose;
    }

    private void run(WTable status) {
        String apiKey = AltGenConfig.get().apiKey.get();

        MeteorExecutor.execute(() -> {
            setStatus(status, "Generating account...");
            AlteningApi.Result res = AlteningApi.generate(apiKey);

            if (res == null || res.token == null) {
                fail(status, "Generation failed. Check your API key.");
                return;
            }

            if (res.limit) {
                fail(status, "Daily limit reached.");
                return;
            }

            TheAlteningAccount account = new TheAlteningAccount(res.token);

            setStatus(status, "Logging in...");
            if (!account.fetchInfo()) {
                fail(status, "Login failed: invalid credentials.");
                return;
            }

            if (!account.login()) {
                fail(status, "Login failed.");
                return;
            }

            Accounts.get().add(account);

            mc.execute(() -> {
                AltGen.LOG.info("Logged in as {}", account.getUsername());

                if (address != null && server != null) {
                    ConnectScreen.startConnecting(new TitleScreen(), mc, address, server, false, null);
                } else {
                    mc.gui.setScreen(new JoinMultiplayerScreen(new TitleScreen()));
                }
            });
        });
    }

    private void setStatus(WTable table, String text) {
        mc.execute(() -> {
            table.clear();
            table.add(theme.label(text)).expandX().widget().color = theme.textSecondaryColor();
        });
    }

    private void fail(WTable table, String error) {
        mc.execute(() -> {
            table.clear();
            table.add(theme.label(error)).expandX().widget().color = theme.textColor();
        });
    }
}
