package cc.xypp.battery_shield.mixin;

import cc.xypp.battery_shield.api.IPoseStack;
import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Deque;

@Mixin(PoseStack.class)
public class PoseStackMixin implements IPoseStack {
    @Final
    @Shadow
    private Deque<PoseStack.Pose> poseStack;
    @Override
    public void battery_shield$replacePose(PoseStack.Pose pose) {
        poseStack.removeLast();
        poseStack.addLast(pose);
    }
}
