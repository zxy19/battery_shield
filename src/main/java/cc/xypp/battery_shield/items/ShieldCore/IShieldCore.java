package cc.xypp.battery_shield.items.ShieldCore;

import cc.xypp.battery_shield.data.ShieldType;

public interface IShieldCore {
    ShieldType getCoreLevel();
    ShieldType getRequired();
}
