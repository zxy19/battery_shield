package cc.xypp.battery_shield.api;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;

public interface IGuiGraphicsGetter {
    GuiGraphics getGuiGraphics(PoseStack pose);
}
