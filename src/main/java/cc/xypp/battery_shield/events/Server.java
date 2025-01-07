package cc.xypp.battery_shield.events;

import cc.xypp.battery_shield.BatteryShield;
import cc.xypp.battery_shield.Config;
import cc.xypp.battery_shield.api.IDamageSourceA;
import cc.xypp.battery_shield.api.ILivingEntityA;
import cc.xypp.battery_shield.data.DamageNumberType;
import cc.xypp.battery_shield.data.ShieldType;
import cc.xypp.battery_shield.data.UsageEvent;
import cc.xypp.battery_shield.helper.DamageNumberManager;
import cc.xypp.battery_shield.helper.TrackingManager;
import cc.xypp.battery_shield.helper.UsageEventManager;
import cc.xypp.battery_shield.utils.EntityUtil;
import cc.xypp.battery_shield.utils.ShieldUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = BatteryShield.MODID)
public class Server {
    private static final Random random = new Random();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (EntityUtil.entityLevelIsClient(event.getEntity())) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity) {
            TrackingManager.getInstance().sendTrackingPacket((LivingEntity) event.getEntity());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntitySpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (EntityUtil.entityLevelIsClient(event.getEntity())) {
            return;
        }
        Entity entity = event.getEntity();
        if (event.getEntity() == null) {
            return;
        }
        if (Config.mobs_shield) {
            if (random.nextFloat() > Config.mobs_shield_add_percent) return;
            ILivingEntityA a = (ILivingEntityA) entity;
            ItemStack shieldArmor = ShieldUtil.getShieldArmorNonCore((LivingEntity) entity);
            if (shieldArmor == null) return;
            if (a.battery_shield$getMaxShield() != 0) return;
            float maxValue = ShieldUtil.getMaxShieldValue((LivingEntity) entity);
            if (maxValue > 50) maxValue = 50;
            a.battery_shield$setShield(maxValue);
            a.battery_shield$setMaxShield(maxValue);
            if (shieldArmor.getTag() == null) {
                shieldArmor.setTag(new CompoundTag());
            }
            shieldArmor.getTag().putInt("core_level", ShieldUtil.getShieldTypeByMaxValue(maxValue).ordinal());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingHurt(LivingHurtEvent event) {
        IDamageSourceA damageSource = (IDamageSourceA) event.getSource();
        if (damageSource.battery_shield$isByBatteryShield()) {
            if (Config.zero_damage_event) {
                event.setAmount(Math.max(0,event.getAmount() - damageSource.battery_shield$getShieldDamage()));
            }
        }
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDamage(LivingDamageEvent event) {
        if (EntityUtil.entityLevelIsClient(event.getEntity())) {
            return;
        }
        if (event.getSource().getEntity() instanceof ServerPlayer sp && event.getSource() instanceof IDamageSourceA iDamageSourceA) {
            float amount = event.getAmount();
            float restAmount = amount - iDamageSourceA.battery_shield$getShieldDamage();
            boolean isBreakShield = iDamageSourceA.battery_shield$isBreakShield();
            if (iDamageSourceA.battery_shield$isByBatteryShield()) {
                amount = iDamageSourceA.battery_shield$getShieldDamage();
            }

            // 破甲音效
            if (isBreakShield) {
                UsageEventManager.getInstance().send(sp, UsageEvent.SHIELD_BREAK);
            }
            // 伤害显示包
            DamageNumberManager.getInstance().sendDamagePacket(event.getEntity(),
                    ShieldUtil.getTypeBySourceValue(event.getSource(), ((ILivingEntityA) event.getEntity()).battery_shield$getMaxShield()),
                    isBreakShield,
                    amount,
                    sp);

            //溢出伤害计算
            if (isBreakShield && restAmount>= 0) {
                DamageNumberManager.getInstance().sendDamagePacket(event.getEntity(),
                        DamageNumberType.RAW,
                        false,
                        restAmount,
                        sp);
            }
        }
        if (Config.calc_damage_with_event) {
            if (event.getSource() instanceof IDamageSourceA iDamageSourceA && event.getEntity() instanceof ILivingEntityA iLivingEntityA) {
                if (iDamageSourceA.battery_shield$isByBatteryShield()) {
                    iLivingEntityA.battery_shield$shieldHurt(iDamageSourceA.battery_shield$getShieldDamage());
                    event.setAmount(Math.max(0,event.getAmount() - iDamageSourceA.battery_shield$getShieldDamage()));
                }

                //Attacker's shield accumulate.
                if(event.getSource() != null && event.getSource().getEntity() instanceof ILivingEntityA iLivingEntityA1){
                    float amount = event.getAmount();
                    if (iDamageSourceA.battery_shield$isByBatteryShield()) {
                        amount =iDamageSourceA.battery_shield$getShieldDamage();
                    }
                    iLivingEntityA1.battery_shield$accumulateShieldPoint(amount);
                }
            }
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
        CompoundTag tag = event.getItemStack().getTag();
        if (tag != null && tag.contains("core_level")) {
            ShieldType coreLevel = ShieldType.values()[tag.getInt("core_level")];
            if (coreLevel == ShieldType.RAW) return;
            String color = ShieldUtil.getShieldIdByType(coreLevel);
            float maxValue = tag.getFloat("shield_max");
            float currentValue = tag.getFloat("shield_value");

            event.getToolTip().add(
                    Component.translatable(String.format("tooltip.battery_shield.level.%s", color),
                            String.format("%.1f", maxValue),
                            String.format("%.1f", currentValue)
                    ));
        }
    }

    @SubscribeEvent
    public static void onEquip(LivingEquipmentChangeEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        TrackingManager.getInstance().sendTrackingPacket(event.getEntity());
    }
}