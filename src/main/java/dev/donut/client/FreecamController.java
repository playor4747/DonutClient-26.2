package dev.donut.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;

public final class FreecamController {
    private static boolean active;
    private static Vec3 position = Vec3.ZERO;
    private static float yaw;
    private static float pitch;
    private static final double SPEED = 0.55D;

    private FreecamController() {}

    public static boolean isActive() {
        return active;
    }

    public static void toggle(Minecraft client) {
        LocalPlayer player = client.player;
        if (player == null) return;

        active = !active;
        if (active) {
            position = player.getEyePosition();
            yaw = player.getYRot();
            pitch = player.getXRot();
        }
    }

    public static void tick(Minecraft client) {
        LocalPlayer player = client.player;
        if (player == null) {
            active = false;
            return;
        }

        yaw = player.getYRot();
        pitch = player.getXRot();

        Vec3 forward = player.getLookAngle().normalize();
        Vec3 right = forward.cross(new Vec3(0, 1, 0)).normalize().scale(-1);
        Vec3 up = new Vec3(0, 1, 0);

        double x = 0;
        double y = 0;
        double z = 0;

        if (client.options.keyUp.isDown()) {
            x += forward.x;
            y += forward.y;
            z += forward.z;
        }
        if (client.options.keyDown.isDown()) {
            x -= forward.x;
            y -= forward.y;
            z -= forward.z;
        }
        if (client.options.keyLeft.isDown()) {
            x += right.x;
            y += right.y;
            z += right.z;
        }
        if (client.options.keyRight.isDown()) {
            x -= right.x;
            y -= right.y;
            z -= right.z;
        }
        if (client.options.keyJump.isDown()) {
            x += up.x;
            y += up.y;
            z += up.z;
        }
        if (client.options.keyShift.isDown()) {
            x -= up.x;
            y -= up.y;
            z -= up.z;
        }

        Vec3 movement = new Vec3(x, y, z);
        if (movement.lengthSqr() > 0.0) {
            position = position.add(movement.normalize().scale(SPEED));
        }
    }

    public static Vec3 position() {
        return position;
    }

    public static float yaw() {
        return yaw;
    }

    public static float pitch() {
        return pitch;
    }
}
