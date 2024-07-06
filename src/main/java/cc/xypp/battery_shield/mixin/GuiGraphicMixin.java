package cc.xypp.battery_shield.mixin;

import cc.xypp.battery_shield.api.IGuiGraphics;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicMixin implements IGuiGraphics {
    @Mutable
    @Shadow
    @Final
    private PoseStack pose;

    @Override
    public void battery_shield$setPose(PoseStack pose) {
        this.pose = pose;
    }
}
