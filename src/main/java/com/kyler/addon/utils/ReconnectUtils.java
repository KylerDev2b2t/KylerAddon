package com.kyler.addon.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;

public class ReconnectUtils {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private static ServerInfo lastServer = null;
    private static int reconnectAttempts = 0;
    private static long lastDisconnectTime = 0;
    private static boolean shouldReconnect = false;

    public static void setLastServer(ServerInfo server) {
        lastServer = server;
    }

    public static ServerInfo getLastServer() {
        return lastServer;
    }

    public static void triggerReconnect() {
        shouldReconnect = true;
        reconnectAttempts = 0;
        lastDisconnectTime = System.currentTimeMillis();
    }

    public static boolean shouldReconnect() {
        return shouldReconnect && lastServer != null;
    }

    public static void attemptReconnect(int maxAttempts, int delay) {
        if (reconnectAttempts >= maxAttempts) {
            shouldReconnect = false;
            return;
        }
        if (System.currentTimeMillis() - lastDisconnectTime >= delay * 1000L) {
            if (lastServer != null && mc.getNetworkHandler() == null) {
                ServerAddress serverAddress = ServerAddress.parse(lastServer.address);
                ConnectScreen.connect(mc.currentScreen, mc, serverAddress, lastServer, false, null);
                reconnectAttempts++;
                lastDisconnectTime = System.currentTimeMillis();
            }
        }
    }

    public static int getReconnectAttempts() {
        return reconnectAttempts;
    }

    public static long getTimeSinceDisconnect() {
        return System.currentTimeMillis() - lastDisconnectTime;
    }

    public static void reset() {
        shouldReconnect = false;
        reconnectAttempts = 0;
        lastServer = null;
    }
}
