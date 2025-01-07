package cc.xypp.battery_shield.mixin;

import cc.xypp.battery_shield.Config;
import cc.xypp.battery_shield.api.IDamageSourceA;
import cc.xypp.battery_shield.api.ILivingEntityA;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @ModifyVariable(method = "actuallyHurt", at = @At(value = "INVOKE",target = "Lnet/minecraftforge/common/ForgeHooks;onLivingDamage(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/damagesource/DamageSource;F)F", shift = At.Shift.BY,by = 2),name = "f1",require = 0)
    public float beforeHurtEffect(float f1,DamageSource damageSource) {
        if(Config.calc_damage_with_event)return f1;
        //Attacker's shield accumulate.
        float amount = f1;
        if (((IDamageSourceA) damageSource).battery_shield$isByBatteryShield()) {
            amount =((IDamageSourceA) damageSource).battery_shield$getShieldDamage();
        }
        ((ILivingEntityA) this).battery_shield$accumulateShieldPoint(amount);

        if(((IDamageSourceA) damageSource).battery_shield$isByBatteryShield()) {
            if(Config.zero_damage_event)f1=((IDamageSourceA) damageSource).battery_shield$getShieldDamage();
            ((ILivingEntityA) this).battery_shield$shieldHurt(f1);

            // 抵扣护盾伤害
            return Math.max(0,f1 - ((IDamageSourceA)damageSource).battery_shield$getShieldDamage());
        }
        return f1;
    }
}
