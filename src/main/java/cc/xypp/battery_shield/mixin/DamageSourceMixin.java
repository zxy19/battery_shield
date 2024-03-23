package cc.xypp.battery_shield.mixin;

import cc.xypp.battery_shield.api.IDamageSourceA;
import cc.xypp.battery_shield.data.DamageNumberType;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(DamageSource.class)
public abstract class DamageSourceMixin implements IDamageSourceA {
    @Unique
    private boolean byBatteryShield;

    @Unique
    private float shieldDamage;
    @Unique
    private DamageNumberType shieldDamageType;
    public boolean isByBatteryShield() {
        return byBatteryShield;
    }
    public void setByBatteryShield(boolean byBatteryShield) {
        this.byBatteryShield = byBatteryShield;
    }

    @Override
    public void setShieldDamage(float damage) {
        this.shieldDamage = damage;
    }
    @Override
    public float getShieldDamage() {
        return shieldDamage;
    }

    @Override
    public DamageNumberType getShieldDamageType() {
        return shieldDamageType;
    }

    @Override
    public void setShieldDamageType(DamageNumberType type) {
        this.shieldDamageType = type;
    }

}
