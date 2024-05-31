package cc.xypp.battery_shield.mixin;

import cc.xypp.battery_shield.Config;
import cc.xypp.battery_shield.api.IDamageSourceA;
import cc.xypp.battery_shield.api.ILivingEntityA;
import cc.xypp.battery_shield.data.DamageNumberType;
import cc.xypp.battery_shield.data.ShieldType;
import cc.xypp.battery_shield.helper.TrackingManager;
import cc.xypp.battery_shield.utils.ShieldUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements ILivingEntityA {
    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }
    @Shadow public abstract @NotNull Iterable<ItemStack> getArmorSlots();

    @Override
    public float battery_shield$getShield() {
        ItemStack shieldArmor = ShieldUtil.getShieldArmor((LivingEntity) (Object)this);
        if(shieldArmor != null && shieldArmor.getTag() != null) {
            return shieldArmor.getTag().getFloat("shield_value");
        }
        return 0;
    }
    @Override
    public void battery_shield$setShield(float shield) {
        shield = Math.min(this.battery_shield$getMaxShield(), shield);
        ItemStack shieldArmor = ShieldUtil.getShieldArmor((LivingEntity) (Object)this);
        if(shieldArmor != null && shieldArmor.getTag() != null) {
            shieldArmor.getTag().putFloat("shield_value",shield);
        }
    }
    @Override
    public float battery_shield$getMaxShield() {
        ItemStack shieldArmor = ShieldUtil.getShieldArmor((LivingEntity) (Object)this);
        if(shieldArmor != null && shieldArmor.getTag() != null) {
            return shieldArmor.getTag().getFloat("shield_max");
        }
        return 0;
    }

    @Override
    public ShieldType battery_shield$getShieldType() {
        ItemStack shieldArmor = ShieldUtil.getShieldArmor((LivingEntity) (Object)this);
        if(shieldArmor != null && shieldArmor.getTag() != null) {
            return ShieldType.values()[shieldArmor.getTag().getInt("core_level")];
        }
        return ShieldType.RAW;
    }

    @Override
    public void battery_shield$setMaxShield(float maxShield) {
        ItemStack shieldArmor = ShieldUtil.getShieldArmor((LivingEntity) (Object)this);
        if(shieldArmor != null && shieldArmor.getTag() != null) {
            shieldArmor.getTag().putFloat("shield_max",maxShield);
        }
    }

    @Override
    public void battery_shield$shieldHurt(float amount) {
        float afterValue = this.battery_shield$getShield() - amount;
        if(afterValue < 0) {
            afterValue = 0;
            playSound(SoundEvents.GLASS_BREAK, 1.0F, 1.0F);
        }
        this.battery_shield$setShield(afterValue);
        TrackingManager.getInstance().sendTrackingPacket((LivingEntity) (Object)this);
    }


    @Inject(method = "hurt", at = @At(value = "HEAD"))
    public void hurt(DamageSource p_21016_, float p_21017_, CallbackInfoReturnable<Boolean> cir) {
        if(this.battery_shield$getShield() > 0){
            ((IDamageSourceA) p_21016_).setByBatteryShield(true);
            ((IDamageSourceA) p_21016_).setShieldDamage(p_21017_);
            ((IDamageSourceA) p_21016_).setShieldDamageType(ShieldUtil.getTypeBySourceValue(p_21016_,this.battery_shield$getMaxShield()));
        }else{
            ((IDamageSourceA) p_21016_).setByBatteryShield(false);
            ((IDamageSourceA) p_21016_).setShieldDamage(0);
            ((IDamageSourceA) p_21016_).setShieldDamageType(DamageNumberType.RAW);
        }
    }
    @Inject(method = "hurtHelmet", at = @At(value = "HEAD"),cancellable = true)
    public void hurtHelmet(DamageSource p_147213_, float p_147214_, CallbackInfo ci) {
        if(((IDamageSourceA)p_147213_).isByBatteryShield()){
            ci.cancel();
        }
    }
    @Inject(method = "hurtArmor", at = @At(value = "HEAD"),cancellable = true)
    public void hurtArmor(DamageSource p_147213_, float p_147214_, CallbackInfo ci) {
        if(((IDamageSourceA)p_147213_).isByBatteryShield()){
            ci.cancel();
        }
    }
    @ModifyVariable(method = "actuallyHurt", at = @At(value = "INVOKE",target = "Lnet/minecraftforge/common/ForgeHooks;onLivingDamage(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/damagesource/DamageSource;F)F", shift = At.Shift.BY,by = 2),name = "f1",require = 0)
    public float beforeHurtEffect(float f1,DamageSource p_21240_) {
        if(Config.calc_damage_with_event)return f1;
        if(((IDamageSourceA) p_21240_).isByBatteryShield()) {
            if(Config.zero_damage_event)f1=((IDamageSourceA) p_21240_).getShieldDamage();
            ((ILivingEntityA) this).battery_shield$shieldHurt(f1);
            return 0;
        }
        return f1;
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    public void readAdditionalSaveData(CompoundTag p_21145_, CallbackInfo ci) {
        if(p_21145_.contains("shield")) {
            this.battery_shield$setShield(p_21145_.getFloat("shield"));
        }
        if(p_21145_.contains("maxShield")) {
            this.battery_shield$setMaxShield(p_21145_.getFloat("maxShield"));
        }
    }
    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    public void addAdditionalSaveData(CompoundTag p_21145_, CallbackInfo ci) {
        p_21145_.putFloat("shield", this.battery_shield$getShield());
        p_21145_.putFloat("maxShield", this.battery_shield$getMaxShield());
    }
}
