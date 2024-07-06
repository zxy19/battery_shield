package cc.xypp.battery_shield.api;

import cc.xypp.battery_shield.data.DamageNumberType;

public interface IDamageSourceA {
    boolean battery_shield$isByBatteryShield();
    void battery_shield$setByBatteryShield(boolean byBatteryShield);
    void battery_shield$setShieldDamage(float damage);
    float battery_shield$getShieldDamage();
    DamageNumberType battery_shield$getShieldDamageType();
    void battery_shield$setShieldDamageType(DamageNumberType type);
    void battery_shield$setIsBreakShield(boolean isBreakShield);
    boolean battery_shield$isBreakShield();
}
