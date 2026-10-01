package dev.donut.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class DonutMenuScreen extends Screen {
    private EditBox searchBox;
    private String resultText = "No search yet";

    public DonutMenuScreen() {
        super(Component.literal("DonutClient"));
    }

    @Override
    protected void init() {
        int left = this.width / 2 - 170;
        int top = 35;

        this.searchBox = new EditBox(this.font, left, top + 38, 240, 20, Component.literal("Search blocks"));
        this.searchBox.setHint(Component.literal("e.g. chest, diamond_ore, shulker"));
        this.addRenderableWidget(this.searchBox);

        this.addRenderableWidget(Button.builder(
                Component.literal("Search"),
                button -> search()
        ).bounds(left + 247, top + 38, 93, 20).build());

        this.addRenderableWidget(Button.builder(
                espLabel(),
                button -> {
                    DonutClient.setStorageEsp(!DonutClient.storageEspEnabled());
                    button.setMessage(espLabel());
                }
        ).bounds(left, top + 75, 165, 20).build());

        this.addRenderableWidget(Button.builder(
                freecamLabel(),
                button -> {
                    FreecamController.toggle(Minecraft.getInstance());
                    button.setMessage(freecamLabel());
                }
        ).bounds(left + 175, top + 75, 165, 20).build());

        this.addRenderableWidget(Button.builder(
                searchEspLabel(),
                button -> {
                    DonutClient.setSearchEsp(!DonutClient.searchEspEnabled());
                    button.setMessage(searchEspLabel());
                }
        ).bounds(left, top + 101, 165, 20).build());

        this.addRenderableWidget(Button.builder(
                labelsLabel(),
                button -> {
                    DonutClient.setLabels(!DonutClient.labelsEnabled());
                    button.setMessage(labelsLabel());
                }
        ).bounds(left + 175, top + 101, 165, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("ESP radius: " + DonutClient.espRadius()),
                button -> {
                    int next = DonutClient.espRadius() + 8;
                    if (next > 96) next = 16;
                    DonutClient.setEspRadius(next);
                    button.setMessage(Component.literal("ESP radius: " + DonutClient.espRadius()));
                }
        ).bounds(left, top + 127, 165, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Close"),
                button -> Minecraft.getInstance().setScreen(null)
        ).bounds(left + 175, top + 127, 165, 20).build());
    }

    private void search() {
        StorageEsp.runSearch(Minecraft.getInstance(), this.searchBox.getValue(), 64);
        resultText = "Found: " + StorageEsp.searchMatches().size();
    }

    private Component espLabel() {
        return Component.literal("Storage ESP: " + (DonutClient.storageEspEnabled() ? "ON" : "OFF"));
    }

    private Component freecamLabel() {
        return Component.literal("Freecam: " + (FreecamController.isActive() ? "ON" : "OFF"));
    }

    private Component searchEspLabel() {
        return Component.literal("Search ESP: " + (DonutClient.searchEspEnabled() ? "ON" : "OFF"));
    }

    private Component labelsLabel() {
        return Component.literal("Labels: " + (DonutClient.labelsEnabled() ? "ON" : "OFF"));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);

        int left = this.width / 2 - 170;
        int top = 35;
        graphics.fill(left - 12, top - 12, left + 352, top + 170, 0xD0101018);
        graphics.text(this.font, "DonutClient", left, top, 0xFFFFFFFF, true);
        graphics.text(this.font, "Block search + client utilities", left, top + 17, 0xFFB8B8C8, false);
        graphics.text(this.font, resultText, left + 247, top + 65, 0xFFFFFFFF, false);
        graphics.text(this.font, "F6 Freecam  •  F7 Storage ESP  •  Right Shift Menu", left, top + 154, 0xFFAAAAAA, false);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
