package cc.xypp.battery_shield.mixin;

import cc.xypp.battery_shield.api.IDamageSourceA;
import cc.xypp.battery_shield.data.DamageNumberType;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(DamageSource.class)
public abstract class DamageSourceMixin implements IDamageSourceA {
    @Unique
    private boolean battery_shield$byBatteryShield;

    @Unique
    private float shieldDamage;
    @Unique
    private DamageNumberType shieldDamageType;
    @Unique
    private boolean breakShield;

    public boolean battery_shield$isByBatteryShield() {
        return battery_shield$byBatteryShield;
    }
    public void battery_shield$setByBatteryShield(boolean byBatteryShield) {
        this.battery_shield$byBatteryShield = byBatteryShield;
    }
    public void battery_shield$setIsBreakShield(boolean isBreakShield) {
        this.breakShield = isBreakShield;
    }
    public boolean battery_shield$isBreakShield() {
        return breakShield;
    }
    @Override
    public void battery_shield$setShieldDamage(float damage) {
        this.shieldDamage = damage;
    }
    @Override
    public float battery_shield$getShieldDamage() {
        return shieldDamage;
    }

    @Override
    public DamageNumberType battery_shield$getShieldDamageType() {
        return shieldDamageType;
    }

    @Override
    public void battery_shield$setShieldDamageType(DamageNumberType type) {
        this.shieldDamageType = type;
    }

}
