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
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.Position;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = BatteryShield.MODID, value = Dist.CLIENT)
public class Client {
    @SubscribeEvent
    public static void onRenderLivingEvent(RenderLivingEvent.Post<?, ?> event) {

    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            final GuiGraphics guiGraphics = ((IGuiGraphicsGetter) Minecraft.getInstance()).getGuiGraphics(event.getPoseStack());
            DamageNumberManager.getInstance().render(guiGraphics, event.getCamera(),event.getPartialTick());

            EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null && minecraft.player != null) {
                Vec3 fromPos = minecraft.player.getEyePosition(event.getPartialTick());
                minecraft.level.getEntities(minecraft.player,
                        AABB.ofSize(minecraft.player.position(), 20, 20, 20),
                        EntitySelector.LIVING_ENTITY_STILL_ALIVE).forEach((entity) -> {
                    LivingEntity living = (LivingEntity) entity;
                    ILivingEntityA iLivingEntityA = (ILivingEntityA) living;
                    if (RenderUtils.raytrace(living)) return;

                    PoseStack poseStack = guiGraphics.pose();
                    poseStack.pushPose();
                    Vec3 livingFrom = living.getPosition(event.getPartialTick()).add(0, living.getBbHeight() + 0.5f, 0);
                    Vec3 posFromPlayer = fromPos.vectorTo(livingFrom);
                    poseStack.translate(posFromPlayer.x, posFromPlayer.y, posFromPlayer.z);
                    poseStack.mulPose(Axis.YP.rotationDegrees(-event.getCamera().getYRot()));
                    poseStack.mulPose(Axis.XP.rotationDegrees(event.getCamera().getXRot()));
                    poseStack.scale(-0.025f, -0.025f, 1);
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
                    poseStack.popPose();
                });
            }
        }
    }
}
