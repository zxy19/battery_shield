package cc.xypp.battery_shield.utils;

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
    public static void getEntityRenderPose(float partialTick,List<LivingEntity> entitys, GuiGraphics guiGraphics, Camera camera) {
        if(entitys.isEmpty())return;
        Minecraft client = Minecraft.getInstance();

        if (camera == null) {
            camera = client.getEntityRenderDispatcher().camera;
        }

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE,
                GL11.GL_ZERO);

        for (LivingEntity entity : entitys) {
            float scaleToGui = 0.025f;
            boolean sneaking = entity.isCrouching();
            float height = entity.getBbHeight() + 0.6F - (sneaking ? 0.25F : 0.0F);

            double x = Mth.lerp(partialTick, entity.xo, entity.getX());
            double y = Mth.lerp(partialTick, entity.yo, entity.getY());
            double z = Mth.lerp(partialTick, entity.zo, entity.getZ());

            Vec3 camPos = camera.getPosition();
            double camX = camPos.x();
            double camY = camPos.y();
            double camZ = camPos.z();

            final PoseStack matrix = guiGraphics.pose();

            matrix.pushPose();
            matrix.translate(x - camX, (y + height) - camY, z - camZ);
            matrix.mulPose(Axis.YP.rotationDegrees(-camera.getYRot()));
            matrix.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));
            matrix.scale(-scaleToGui, -scaleToGui, scaleToGui);

            RenderUtils.renderEntity(guiGraphics, entity);

            matrix.popPose();
        }

        RenderSystem.disableBlend();
    }

    private static void renderEntity(GuiGraphics guiGraphics, LivingEntity entity) {
        renderHealth(guiGraphics,
                0,
                0,
                96,
                6,
                entity.getHealth(),
                entity.getMaxHealth());
        ILivingEntityA iLivingEntityA = (ILivingEntityA) entity;
        renderBar(guiGraphics,
                0,
                0,
                96,
                6,
                AssetsManager.SHIELD_BORDER,
                ShieldUtil.getShieldTypeByValue(iLivingEntityA.effect_test$getMaxShield()),
                iLivingEntityA.effect_test$getShield(),
                iLivingEntityA.effect_test$getMaxShield());
    }

    public static void renderHealth(GuiGraphics guiGraphics, int x, int y, int width, int height, double value, double max) {
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