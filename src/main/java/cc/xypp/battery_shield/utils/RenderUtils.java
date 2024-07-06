package cc.xypp.battery_shield.utils;

import cc.xypp.battery_shield.BatteryShield;
import cc.xypp.battery_shield.Config;
import cc.xypp.battery_shield.api.ILivingEntityA;
import cc.xypp.battery_shield.data.DamageNumberType;
import cc.xypp.battery_shield.helper.AssetsManager;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RenderUtils {
    public static final Set<ResourceLocation> blendedHeadTextures = new HashSet<>();
    static int CLIP_WIDTH = 3;

    public static void renderHealth(GuiGraphics guiGraphics, int x, int y, int width, int height, double value, double max) {
        renderHealth(guiGraphics, x, y, width, height, value, max, false);
    }

    public static void renderHealth(GuiGraphics guiGraphics, int x, int y, int width, int height, double value, double max, boolean force) {
        if (!force && !Config.display_health) return;
        AssetsManager.HEALTH_EMPTY.blit(guiGraphics, x, y, 0, 0, width, height);
        int targetValue = (int) (width * value / max);
        int realClip = (int) (1.0 * width / AssetsManager.HEALTH_FILL.width * CLIP_WIDTH);
        if (targetValue >= AssetsManager.HEALTH_FILL.width) {
            AssetsManager.HEALTH_FILL.blit(guiGraphics, x, y, 0, 0, AssetsManager.HEALTH_FILL.width, height);
        } else if (targetValue <= realClip) {
            AssetsManager.HEALTH_FILL.blit(guiGraphics, x, y, 0, 0, targetValue, height);
        } else {
            AssetsManager.HEALTH_FILL.blit(guiGraphics, x, y, 0, 0, realClip, height);
            AssetsManager.HEALTH_FILL.blit(guiGraphics, x + realClip, y, AssetsManager.HEALTH_FILL.width - targetValue, 0, targetValue, height);
        }
    }

    public static void renderBar(GuiGraphics guiGraphics,
                                 int x,
                                 int y,
                                 int width,
                                 int height,
                                 AssetsManager.ImageAssets bg,
                                 AssetsManager.ImageAssets fill,
                                 float value,
                                 float max) {
        renderBar(guiGraphics, x, y, width, height, bg, fill, value, max, false);
    }

    public static void renderBar(GuiGraphics guiGraphics,
                                 int x,
                                 int y,
                                 int width,
                                 int height,
                                 AssetsManager.ImageAssets bg,
                                 AssetsManager.ImageAssets fill,
                                 float value,
                                 float max,
                                 boolean force) {
        if (!Config.display_shield && !force) return;
        for (int i = 0; i < 5; i++) {
            renderBarCell(guiGraphics, x + i * width / 5, y, width / 5, height, fill, value - i * 10);
        }
        for (int i = 0; i < 5; i++) {
            renderBarCell(guiGraphics, x + i * width / 5, y, width / 5, height, bg, max - i * 10);
        }
    }

    protected static void renderBarCell(GuiGraphics guiGraphics, int x, int y, int width, int height, AssetsManager.ImageAssets im, float value) {
        if (value < 0) return;
        int targetValue = (int) (width / 5 * value);
        int realClip = (int) (1.0 * width / im.width * CLIP_WIDTH);
        if (targetValue >= im.width) {
            im.blit(guiGraphics, x, y, 0, 0, im.width, height);
        } else if (targetValue <= realClip) {
            im.blit(guiGraphics, x, y, 0, 0, targetValue, height);
        } else {
            im.blit(guiGraphics, x, y, 0, 0, realClip, height);
            im.blit(guiGraphics, x + realClip, y, im.width - targetValue, 0, targetValue, height);
        }
    }

    public static void renderDamageNumber(GuiGraphics guiGraphics, int x, int y, DamageNumberType type, double value) {
        if (!Config.display_number) return;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0F, 5F, 0.05F);

        guiGraphics.drawString(Minecraft.getInstance().font, String.format("%.1f", value), x + 9, y, ShieldUtil.getColor(type));
        guiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
        AssetsManager.ImageAssets icon = ShieldUtil.getIconByType(type);
        if (icon != null) icon.blit(guiGraphics, x * 2, y * 2, 16, 16);
        guiGraphics.pose().popPose();
    }

    public static boolean raytrace(LivingEntity a) {
        RayTrace rayTrace = new RayTrace();
        return !rayTrace.entityReachable(20, Minecraft.getInstance(), Minecraft.getInstance().player.getEyePosition(), a);
    }

    public static NativeImage extractBlendedHead(NativeImage skin) {
        // vanilla skins are 64x64 pixels, HD skins (e.g. with CustomSkinLoader) 128x128
        int xScale = skin.getWidth() / 64;
        int yScale = skin.getHeight() / 64;

        NativeImage head = new NativeImage(8 * xScale, 8 * yScale, false);

        for (int y = 0; y < head.getHeight(); y++) {
            for (int x = 0; x < head.getWidth(); x++) {
                int headColor = skin.getPixelRGBA(8 * xScale + x, 8 * yScale + y);
                int hatColor = skin.getPixelRGBA(40 * xScale + x, 8 * yScale + y);

                // blend layers together
                head.setPixelRGBA(x, y, headColor);
                head.blendPixel(x, y, hatColor);
            }
        }

        return head;
    }

    public static void renderHead(GuiGraphics guiGraphics, ResourceLocation head, int x, int y) {
        if (blendedHeadTextures.contains(head)) {
            // draw head in one draw call, fixing transparency issues of the "vanilla" path below
            guiGraphics.blit(new ResourceLocation(BatteryShield.MODID, head.getPath()),
                    x,
                    y,
                    8,
                    8,
                    0,
                    0,
                    8,
                    8,
                    8,
                    8);
        } else {
            // draw base layer
            guiGraphics.blit(head,
                    x,
                    y,
                    8,
                    8,
                    8.0f,
                    8,
                    8,
                    8,
                    64,
                    64);
            // draw hat
            guiGraphics.blit(head,
                    x,
                    y,
                    8,
                    8,
                    40.0f,
                    8,
                    8,
                    8,
                    64,
                    64);
        }
    }

}
