package cc.xypp.battery_shield.helper;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class AssetsManager {
    public static class ImageAssets {
        public final ResourceLocation resourceLocation;
        public final int width;
        public final int height;

        public ImageAssets(String namespace, String path, int width, int height) {
            this.resourceLocation = new ResourceLocation(namespace, path);
            this.width = width;
            this.height = height;
        }

        public void blit(GuiGraphics pGuiGraphics, int x, int y) {
            this.blit(pGuiGraphics, x, y, 0, 0, width, height);
        }

        public void blit(GuiGraphics pGuiGraphics, int x, int y, int drawWidth, int drawHeight) {
            this.blit(pGuiGraphics, x, y, 0, 0, drawWidth, drawHeight);
        }

        public void blit(GuiGraphics pGuiGraphics, int x, int y, int drawX, int drawY, int drawWidth, int drawHeight) {
            pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();
            RenderSystem.disableDepthTest();
            pGuiGraphics.blit(resourceLocation, x, y, drawX, drawY, drawWidth, drawHeight, width, height);
            pGuiGraphics.flush();
            RenderSystem.disableBlend();
            RenderSystem.enableDepthTest();
        }

        public void blit(GuiGraphics pGuiGraphics, int x, int y, int order, int drawX, int drawY, int drawWidth, int drawHeight) {
            pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();
            RenderSystem.disableDepthTest();
            pGuiGraphics.blit(resourceLocation, x, y, order, drawX, drawY, drawWidth, drawHeight, width, height);
            pGuiGraphics.flush();
            RenderSystem.disableBlend();
            RenderSystem.enableDepthTest();
        }
    }

    public static ImageAssets SHIELD_BORDER = new ImageAssets("battery_shield", "textures/gui/battery_empty.png", 21, 6);
    public static ImageAssets SHIELD_WHITE = new ImageAssets("battery_shield", "textures/gui/battery_fill_w.png", 21, 6);
    public static ImageAssets SHIELD_BLUE = new ImageAssets("battery_shield", "textures/gui/battery_fill_b.png", 21, 6);
    public static ImageAssets SHIELD_PERPLE = new ImageAssets("battery_shield", "textures/gui/battery_fill_p.png", 21, 6);
    public static ImageAssets SHIELD_RED = new ImageAssets("battery_shield", "textures/gui/battery_fill_r.png", 21, 6);
    public static ImageAssets HEALTH_FILL = new ImageAssets("battery_shield", "textures/gui/blood_fill.png", 96, 6);
    public static ImageAssets HEALTH_EMPTY = new ImageAssets("battery_shield", "textures/gui/blood_empty.png", 96, 6);
    public static ImageAssets SHIELD_ICON_RED = new ImageAssets("battery_shield", "textures/gui/shield_r.png", 16, 16);
    public static ImageAssets SHIELD_ICON_PERPLE = new ImageAssets("battery_shield", "textures/gui/shield_p.png", 16, 16);
    public static ImageAssets SHIELD_ICON_BLUE = new ImageAssets("battery_shield", "textures/gui/shield_b.png", 16, 16);
    public static ImageAssets SHIELD_ICON_WHITE = new ImageAssets("battery_shield", "textures/gui/shield_w.png", 16, 16);

}
