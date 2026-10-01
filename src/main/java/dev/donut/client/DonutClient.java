package dev.donut.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public final class DonutClient implements ClientModInitializer {
    public static final String MOD_ID = "donutclient";

    public static final KeyMapping OPEN_MENU = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.donutclient.menu",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "main"))
    ));

    public static final KeyMapping TOGGLE_ESP = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.donutclient.toggle_esp",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_F7,
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "main"))
    ));

    public static final KeyMapping TOGGLE_FREECAM = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.donutclient.toggle_freecam",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_F6,
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "main"))
    ));

    private static boolean storageEsp = true;
    private static boolean searchEsp = false;
    private static boolean labels = true;
    private static int espRadius = 48;

    @Override
    public void onInitializeClient() {
        StorageEsp.init();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (OPEN_MENU.consumeClick()) {
                if (client.gui != null) {
                    client.setScreen(new DonutMenuScreen());
                }
            }

            while (TOGGLE_ESP.consumeClick()) {
                storageEsp = !storageEsp;
            }

            while (TOGGLE_FREECAM.consumeClick()) {
                FreecamController.toggle(client);
            }

            if (FreecamController.isActive()) {
                FreecamController.tick(client);
            }

            if (storageEsp || searchEsp) {
                StorageEsp.tick(client, espRadius);
            }
        });
    }

    public static boolean storageEspEnabled() {
        return storageEsp;
    }

    public static void setStorageEsp(boolean enabled) {
        storageEsp = enabled;
    }

    public static boolean searchEspEnabled() {
        return searchEsp;
    }

    public static void setSearchEsp(boolean enabled) {
        searchEsp = enabled;
    }

    public static boolean labelsEnabled() {
        return labels;
    }

    public static void setLabels(boolean enabled) {
        labels = enabled;
    }

    public static int espRadius() {
        return espRadius;
    }

    public static void setEspRadius(int radius) {
        espRadius = Math.max(8, Math.min(radius, 96));
    }
}
