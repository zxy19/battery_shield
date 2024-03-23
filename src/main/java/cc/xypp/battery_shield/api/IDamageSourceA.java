package cc.xypp.battery_shield.api;

import cc.xypp.battery_shield.data.DamageNumberType;

public interface IDamageSourceA {
    boolean isByBatteryShield();
    void setByBatteryShield(boolean byBatteryShield);
    void setShieldDamage(float damage);
    float getShieldDamage();
    DamageNumberType getShieldDamageType();
    void setShieldDamageType(DamageNumberType type);
}
