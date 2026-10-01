package dev.donut.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class StorageEsp {
    private static final List<BlockPos> storageMatches = new ArrayList<>();
    private static final List<BlockPos> searchMatches = new ArrayList<>();
    private static String searchQuery = "";
    private static int tickCounter = 0;

    private StorageEsp() {}

    public static void init() {
        LevelRenderEvents.END_MAIN.register(StorageEsp::render);
    }

    public static void tick(Minecraft client, int radius) {
        if (client.level == null || client.player == null) return;
        tickCounter++;
        if (tickCounter % 10 != 0) return;

        storageMatches.clear();
        LocalPlayer player = client.player;
        BlockPos origin = player.blockPosition();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -24; y <= 24; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = origin.offset(x, y, z);
                    if (!client.level.hasChunkAt(pos)) continue;
                    BlockState state = client.level.getBlockState(pos);
                    if (isStorageBlock(state.getBlock())) {
                        storageMatches.add(pos.immutable());
                    }
                }
            }
        }
    }

    public static void runSearch(Minecraft client, String query, int radius) {
        searchQuery = query == null ? "" : query.trim().toLowerCase();
        searchMatches.clear();
        if (client.level == null || client.player == null || searchQuery.isEmpty()) return;

        BlockPos origin = client.player.blockPosition();
        int r = Math.min(radius, 64);
        for (int x = -r; x <= r; x++) {
            for (int y = -32; y <= 32; y++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos pos = origin.offset(x, y, z);
                    if (!client.level.hasChunkAt(pos)) continue;
                    BlockState state = client.level.getBlockState(pos);
                    Identifier id = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(state.getBlock());
                    String text = id.toString().toLowerCase();
                    if (text.contains(searchQuery) || state.getBlock().getName().getString().toLowerCase().contains(searchQuery)) {
                        searchMatches.add(pos.immutable());
                    }
                }
            }
        }

        Collections.sort(searchMatches, (a, b) -> Double.compare(
                a.distSqr(origin),
                b.distSqr(origin)
        ));

        if (searchMatches.size() > 100) {
            searchMatches.subList(100, searchMatches.size()).clear();
        }
    }

    public static List<BlockPos> searchMatches() {
        return List.copyOf(searchMatches);
    }

    private static boolean isStorageBlock(Block block) {
        String id = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(block).getPath().toLowerCase();
        return id.contains("chest") || id.contains("barrel") || id.contains("shulker_box") || id.contains("hopper");
    }

    private static void render(LevelRenderContext context) {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null) return;
        if (!DonutClient.storageEspEnabled() && !DonutClient.searchEspEnabled()) return;

        PoseStack poseStack = context.poseStack();
        Vec3Camera camera = new Vec3Camera(context.levelState().cameraRenderState.pos.x, context.levelState().cameraRenderState.pos.y, context.levelState().cameraRenderState.pos.z);
        MultiBufferSource.BufferSource buffers = client.gameRenderer.renderBuffers().bufferSource();
        VertexConsumer consumer = buffers.getBuffer(RenderTypes.lines());

        poseStack.pushPose();
        poseStack.translate(-camera.x, -camera.y, -camera.z);

        if (DonutClient.storageEspEnabled()) {
            for (BlockPos pos : storageMatches) {
                drawBox(poseStack, consumer, new AABB(pos), 0.2f, 0.85f, 1.0f, 1.0f);
            }
        }
        if (DonutClient.searchEspEnabled()) {
            for (BlockPos pos : searchMatches) {
                drawBox(poseStack, consumer, new AABB(pos).inflate(0.02), 1.0f, 0.45f, 0.15f, 1.0f);
            }
        }

        poseStack.popPose();
        buffers.endBatch(RenderTypes.lines());
    }

    private static void drawBox(PoseStack poseStack, VertexConsumer consumer, AABB box, float r, float g, float b, float a) {
        LevelRenderer.renderLineBox(
                poseStack,
                consumer,
                box.minX, box.minY, box.minZ,
                box.maxX, box.maxY, box.maxZ,
                r, g, b, a
        );
    }

    private record Vec3Camera(double x, double y, double z) {}
}
