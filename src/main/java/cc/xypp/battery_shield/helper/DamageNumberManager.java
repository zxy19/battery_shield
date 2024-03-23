package cc.xypp.battery_shield.helper;

import cc.xypp.battery_shield.data.DamageNumberType;
import cc.xypp.battery_shield.packet.DamagePacket;
import cc.xypp.battery_shield.utils.EntityUtil;
import cc.xypp.battery_shield.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class DamageNumberManager {
    private Map<DamageNumberType, Map<Integer, DamageNumberItem>> map = new HashMap<>();
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
        } else {
            map.get(type).put(entity.getId(), new DamageNumberItem(entity, type, number));
        }
    }

    public void sendDamagePacket(LivingEntity entity, DamageNumberType type, float number, ServerPlayer player) {
        if(EntityUtil.entityLevelIsClient(entity)) return;
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new DamagePacket(number, type, entity.getId()));
    }

    public void render(GuiGraphics guiGraphics, LivingEntity entity) {
        for (Map<Integer, DamageNumberItem> mp : this.map.values()) {
            DamageNumberItem item = mp.get(entity.getId());
            if (item == null) continue;
            if (item.time + 3000 > System.currentTimeMillis()) {
                RenderUtils.renderDamageNumber(guiGraphics, 0, 0, item.type, item.number,item.posOffset);
            } else {
                mp.remove(entity.getId());
            }
        }
    }

    public static class DamageNumberItem {
        protected static Random random = new Random();

        public double number;
        public long time;
        public LivingEntity entity;
        public DamageNumberType type;
        public Vector3f posOffset;
        public DamageNumberItem(LivingEntity entity, DamageNumberType type, double number) {
            this.entity = entity;
            this.type = type;
            this.number = number;
            this.time = System.currentTimeMillis();
            float offset = (entity.getBbHeight() / 2 + 0.3f)*10;
            this.posOffset = new Vector3f(random.nextFloat(30)-15, offset, 0);
        }
    }

}