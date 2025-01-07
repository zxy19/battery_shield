package cc.xypp.battery_shield.api;

import cc.xypp.battery_shield.data.ShieldType;

public interface ILivingEntityA {
    float battery_shield$getShield();

    void battery_shield$setShield(float shield);
    void battery_shield$setMaxShield(float shield);
    float battery_shield$getMaxShield();
    ShieldType battery_shield$getShieldType();
    void battery_shield$shieldHurt(float amount);

    float battery_shield$getAccumulateShieldPoint();

    void battery_shield$accumulateShieldPoint(float amount);
}
