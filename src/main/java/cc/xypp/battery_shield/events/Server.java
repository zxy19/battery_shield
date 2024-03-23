package cc.xypp.battery_shield.events;

import cc.xypp.battery_shield.BatteryShield;
import cc.xypp.battery_shield.api.ILivingEntityA;
import cc.xypp.battery_shield.helper.DamageNumberManager;
import cc.xypp.battery_shield.helper.TrackingManager;
import cc.xypp.battery_shield.utils.EntityUtil;
import cc.xypp.battery_shield.utils.ShieldUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = BatteryShield.MODID)
public class Server {

    @SubscribeEvent
    public static void onSpawn(EntityJoinLevelEvent event) {
        if (EntityUtil.entityLevelIsClient(event.getEntity())) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity) {
            ILivingEntityA a = (ILivingEntityA) (Object) entity;
            if(a.effect_test$getMaxShield()!=0)return;
            a.effect_test$setShield(ShieldUtil.getMaxShieldValue((LivingEntity) entity));
            a.effect_test$setMaxShield(a.effect_test$getShield());
            TrackingManager.getInstance().sendTrackingPacket((LivingEntity) event.getEntity());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDamage(LivingDamageEvent event) {
        if (EntityUtil.entityLevelIsClient(event.getEntity())) {
            return;
        }
        if (event.getSource().getEntity() instanceof ServerPlayer sp) {
            DamageNumberManager.getInstance().sendDamagePacket((LivingEntity) event.getEntity(),
                    ShieldUtil.getTypeBySourceValue(event.getSource(), ((ILivingEntityA) event.getEntity()).effect_test$getMaxShield()),
                    event.getAmount(),
                    sp);
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof LivingEntity) {
            TrackingManager.getInstance().sendTrackingPacket((LivingEntity) event.getTarget());
        }
    }

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        event.getToolTip().add(Component.literal("123123"));
    }
}
