package cc.xypp.battery_shield.events;

import cc.xypp.battery_shield.BatteryShield;
import cc.xypp.battery_shield.api.IGuiGraphicsGetter;
import cc.xypp.battery_shield.api.ILivingEntityA;
import cc.xypp.battery_shield.helper.AssetsManager;
import cc.xypp.battery_shield.helper.DamageNumberManager;
import cc.xypp.battery_shield.utils.RenderUtils;
import cc.xypp.battery_shield.utils.ShieldUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = BatteryShield.MODID, value = Dist.CLIENT)
public class Client {
    @SubscribeEvent
    public static void onRenderNameplateEvent(RenderNameTagEvent event) {
        if (event.getEntity() instanceof LivingEntity living) {
            ILivingEntityA iLivingEntityA = (ILivingEntityA) living;
            if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.distanceTo(living) > 10) {
                return;
            }
            if (RenderUtils.raytrace(living)) {
                return;
            }
            EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
            PoseStack poseStack = event.getPoseStack();
            final GuiGraphics guiGraphics = ((IGuiGraphicsGetter) Minecraft.getInstance()).getGuiGraphics(event.getPoseStack());
            poseStack.pushPose();
            poseStack.translate(0.0F, living.getBbHeight() + 0.5f, 0.0f);
            poseStack.mulPose(dispatcher.cameraOrientation());
            poseStack.scale(-0.025f, -0.025f, 0.025f);
            if (iLivingEntityA.battery_shield$getMaxShield() > 0)
                RenderUtils.renderBar(guiGraphics,
                        -53,
                        -15,
                        96,
                        6,
                        AssetsManager.SHIELD_BORDER,
                        ShieldUtil.getShieldTypeByValue(iLivingEntityA.battery_shield$getMaxShield()),
                        iLivingEntityA.battery_shield$getShield(),
                        iLivingEntityA.battery_shield$getMaxShield());
            RenderUtils.renderHealth(guiGraphics, -50, -10, 96, 6, living.getHealth(), living.getMaxHealth());
            DamageNumberManager.getInstance().render(guiGraphics, living);
            RenderSystem.disableBlend();
            RenderSystem.enableDepthTest();
            poseStack.popPose();
        }
    }
}
