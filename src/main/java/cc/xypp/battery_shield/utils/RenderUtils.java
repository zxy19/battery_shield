package cc.xypp.battery_shield.utils;

import cc.xypp.battery_shield.Config;
import cc.xypp.battery_shield.api.ILivingEntityA;
import cc.xypp.battery_shield.data.DamageNumberType;
import cc.xypp.battery_shield.helper.AssetsManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class RenderUtils {
    public static void renderHealth(GuiGraphics guiGraphics, int x, int y, int width, int height, double value, double max) {
        if(!Config.display_health)return;
        AssetsManager.HEALTH_EMPTY.blit(guiGraphics, x, y, 0, 0, width, height);
        AssetsManager.HEALTH_FILL.blit(guiGraphics, x, y, 0, 0, (int) (width * value / max), height);
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
        if(!Config.display_shield)return;
        for (int i = 0; i < 5; i++) {
            renderBarCell(guiGraphics, x + i * width / 5, y, width / 5, height, fill, value - i * 10);
        }
        for (int i = 0; i < 5; i++) {
            renderBarCell(guiGraphics, x + i * width / 5, y, width / 5, height, bg, max - i * 10);
        }
    }

    protected static void renderBarCell(GuiGraphics guiGraphics, int x, int y, int width, int height, AssetsManager.ImageAssets im, float value) {
        if (value < 0) return;
        if (value < 5) {
            im.blit(guiGraphics, x, y, 0, 0, (int) (width / 5 * value), height);
        } else {
            im.blit(guiGraphics, x, y, 0, 0, width, height);
        }
    }

    public static void renderDamageNumber(GuiGraphics guiGraphics, int x, int y, DamageNumberType type, double value, Vector3f offset) {
        if(!Config.display_number)return;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0F, 5F, 0.05F);
        guiGraphics.pose().translate(offset.x(), offset.y(),0);
        guiGraphics.drawString(Minecraft.getInstance().font, String.format("%.1f", value), x + 9, y, ShieldUtil.getColor(type));
        guiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
        AssetsManager.ImageAssets icon = ShieldUtil.getIconByType(type);
        if (icon != null) icon.blit(guiGraphics, x*2, y*2, 0, 0, 16, 16);
        guiGraphics.pose().popPose();
    }

    public static boolean raytrace(LivingEntity a) {
        RayTrace rayTrace = new RayTrace();
        return !rayTrace.entityReachable(20, Minecraft.getInstance(), Minecraft.getInstance().player.getEyePosition(), a);
    }
}