package cc.xypp.battery_shield.helper;

import cc.xypp.battery_shield.api.IGuiGraphics;
import cc.xypp.battery_shield.api.IPoseStack;
import cc.xypp.battery_shield.data.DamageNumberType;
import cc.xypp.battery_shield.packet.DamagePacket;
import cc.xypp.battery_shield.utils.EntityUtil;
import cc.xypp.battery_shield.utils.RenderUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.Supplier;

public class DamageNumberManager {
    private final Map<DamageNumberType, Map<Integer, DamageNumberItem>> map = new HashMap<>();
    public static DamageNumberManager Instance;
    public final SimpleChannel INSTANCE;

    public static DamageNumberManager getInstance() {
        return Instance != null ? Instance : (Instance = new DamageNumberManager());
    }

    public DamageNumberManager() {
        for (DamageNumberType type : DamageNumberType.values()) {
            map.put(type, new HashMap<>());
        }
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation("shield_tracking", "damage_number"),
                () -> "1.0.0",
                (v) -> v.equals("1.0.0"),
                (v) -> v.equals("1.0.0")
        );
        INSTANCE.registerMessage(1, DamagePacket.class, DamagePacket::encode, DamagePacket::new, this::handle);
    }

    public void handle(DamagePacket packet, Supplier<NetworkEvent.Context> ctx) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) return;
        ctx.get().enqueueWork(() -> {
            Entity entity = null;
            if (Minecraft.getInstance().level != null) {
                entity = Minecraft.getInstance().level.getEntity(packet.entityId);
            }
            if (entity instanceof LivingEntity) {
                this.addDamage((LivingEntity) entity, packet.type, packet.number);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public void addDamage(LivingEntity entity, DamageNumberType type, double number) {
        if (map.get(type).containsKey(entity.getId())) {
            map.get(type).get(entity.getId()).number += number;
            map.get(type).get(entity.getId()).time = System.currentTimeMillis();
            map.get(type).get(entity.getId()).position = entity.position();
        } else {
            map.get(type).put(entity.getId(), new DamageNumberItem(entity, type, number));
        }
    }


    public void sendDamagePacket(LivingEntity entity, DamageNumberType type, boolean isBreakShield, float number, ServerPlayer player) {
        if (EntityUtil.entityLevelIsClient(entity)) return;
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new DamagePacket(number, type, isBreakShield, entity.getId()));
    }


    @Deprecated
    public void render(GuiGraphics guiGraphics,LivingEntity entity){
        long curTime = System.currentTimeMillis();
        for (Map<Integer, DamageNumberItem> mp : this.map.values()) {
            DamageNumberItem item = mp.get(entity.getId());
            if (item == null) continue;
            if (item.time + 300000 > curTime) {
                RenderUtils.renderDamageNumber(guiGraphics, 0, 0, item.type, item.number);
            }else {
                mp.remove(entity.getId());
            }
        }
    }
    public void render(GuiGraphics guiGraphics, Camera camera) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        Vec3 fromPos = player.getEyePosition(1.0F);
        Vec3 dir = player.getLookAngle();
        PoseStack poseStack = guiGraphics.pose();
        long curTime = System.currentTimeMillis();
        for (Map<Integer, DamageNumberItem> mp : this.map.values()) {
            for (DamageNumberItem item : mp.values()) {
                if (item == null) continue;
                if (item.time + 3000 > curTime) {
                    LivingEntity living = item.entity;
                    Vec3 livingFrom = item.position.add(0, living.getBbHeight() + 0.5f, 0);
                    Vec3 posFromPlayer = fromPos.vectorTo(livingFrom);
                    double d1 = posFromPlayer.length();
                    double d2 = dir.scale(d1).distanceTo(posFromPlayer);
                    double d3 = (2 * d1 * d1 - d2 * d2) / (2 * d1);
                    Vec3 mov = dir.scale(d3).vectorTo(posFromPlayer);
                    RenderUtils.renderDamageNumber(guiGraphics, 0, 0, item.type, item.number);
                    poseStack.pushPose();
                    poseStack.translate(posFromPlayer.x, posFromPlayer.y, posFromPlayer.z);
                    double toDist = d3 - 3;
                    double minDist = living.getBoundingBox().getCenter().distanceToSqr(living.position());
                    if (toDist < minDist) {
                        toDist = minDist;
                    }
                    if(toDist > d3 - 1){
                        toDist = d3 - 1;
                    }
                    double d4 = toDist / d1;
                    mov = mov.scale(d4);
                    RenderSystem.disableDepthTest();
                    poseStack.translate(-mov.x, -mov.y, -mov.z);
                    poseStack.mulPose(Axis.YP.rotationDegrees(-camera.getYRot()));
                    poseStack.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));
                    poseStack.translate(0.f, 0.0F, -toDist);
                    poseStack.scale(-0.025f, -0.025f, -1f);
                    RenderUtils.renderDamageNumber(guiGraphics, 0, 0, item.type, item.number);
                    RenderSystem.enableDepthTest();
                    poseStack.popPose();
                }else {
                    item.toRemove = true;
                }
            }
        }
        for (Map<Integer, DamageNumberItem> mp : this.map.values()) {
            List<Integer> keys = mp.keySet().stream().toList();
            for (Integer key : keys) {
                if (mp.get(key).toRemove) {
                    mp.remove(key);
                }
            }
        }
    }

    public static class DamageNumberItem {
        protected static Random random = new Random();

        public double number;
        public long time;
        public LivingEntity entity;
        public DamageNumberType type;
        public Vec3 position;
        @Nullable
        @Deprecated
        public PoseStack.Pose pose;
        public boolean toRemove;
        public DamageNumberItem(LivingEntity entity, DamageNumberType type, double number) {
            this.entity = entity;
            this.type = type;
            this.number = number;
            this.time = System.currentTimeMillis();
            this.position = entity.position();
            this.toRemove = false;
        }
    }
}