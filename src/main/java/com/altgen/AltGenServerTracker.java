package com.altgen;

import meteordevelopment.meteorclient.events.world.ServerConnectBeginEvent;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;

public class AltGenServerTracker {
    private static Pair<ServerAddress, ServerData> lastConnection;

    public static Pair<ServerAddress, ServerData> lastConnection() {
        return lastConnection;
    }

    @EventHandler
    private void onConnect(ServerConnectBeginEvent event) {
        lastConnection = new ObjectObjectImmutablePair<>(event.address, event.info);
    }
}
