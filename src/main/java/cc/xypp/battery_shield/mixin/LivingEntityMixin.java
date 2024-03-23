package cc.xypp.battery_shield.mixin;

import cc.xypp.battery_shield.api.IDamageSourceA;
import cc.xypp.battery_shield.api.ILivingEntityA;
import cc.xypp.battery_shield.data.DamageNumberType;
import cc.xypp.battery_shield.helper.TrackingManager;
import cc.xypp.battery_shield.utils.ShieldUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements ILivingEntityA {

    @Unique
    private float effect_test$shield;
    @Unique
    private float effect_test$maxShield;

    @Override
    public float effect_test$getShield() {
        return effect_test$shield;
    }
    @Override
    public void effect_test$setShield(float shield) {
        this.effect_test$shield = shield;
    }
    @Override
    public float effect_test$getMaxShield() {
        return effect_test$maxShield;
    }

    @Override
    public void effect_test$shieldHurt(float amount) {
        this.effect_test$shield -= amount;
        if(this.effect_test$shield < 0) {
            this.effect_test$setShield(0);
            this.effect_test$setMaxShield(0);
            playSound(SoundEvents.GLASS_BREAK, 1.0F, 1.0F);
        }
        TrackingManager.getInstance().sendTrackingPacket((LivingEntity) (Object)this);
    }
//
    @Override
    public void effect_test$setMaxShield(float maxShield) {
        this.effect_test$maxShield = maxShield;
    }


    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }
    @Inject(method = "hurt", at = @At(value = "HEAD"))
    public void hurt(DamageSource p_21016_, float p_21017_, CallbackInfoReturnable<Boolean> cir) {
        if(this.effect_test$shield > 0){
            ((IDamageSourceA)(Object)p_21016_).setByBatteryShield(true);
            ((IDamageSourceA)(Object)p_21016_).setShieldDamage(p_21017_);
            ((IDamageSourceA)(Object)p_21016_).setShieldDamageType(ShieldUtil.getTypeBySourceValue(p_21016_,this.effect_test$maxShield));
        }else{
            ((IDamageSourceA)(Object)p_21016_).setByBatteryShield(false);
            ((IDamageSourceA)(Object)p_21016_).setShieldDamage(0);
            ((IDamageSourceA)(Object)p_21016_).setShieldDamageType(DamageNumberType.RAW);
        }
    }
    @ModifyVariable(method = "actuallyHurt", at = @At(value = "INVOKE",target = "Lnet/minecraftforge/common/ForgeHooks;onLivingDamage(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/damagesource/DamageSource;F)F", shift = At.Shift.BY,by = 2),name = "f1")
    public float beforeHurtEffect(float f1,DamageSource p_21240_) {
        if(((IDamageSourceA)(Object)p_21240_).isByBatteryShield()) {
            ((ILivingEntityA)(Object)this).effect_test$shieldHurt(f1);
            return 0;
        }
        return f1;
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    public void readAdditionalSaveData(CompoundTag p_21145_, CallbackInfo ci) {
        if(p_21145_.contains("shield")) {
            this.effect_test$shield = p_21145_.getFloat("shield");
        }
        if(p_21145_.contains("maxShield")) {
            this.effect_test$maxShield = p_21145_.getFloat("maxShield");
        }
    }
    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    public void addAdditionalSaveData(CompoundTag p_21145_, CallbackInfo ci) {
        p_21145_.putFloat("shield", this.effect_test$shield);
        p_21145_.putFloat("maxShield", this.effect_test$maxShield);
    }
}
