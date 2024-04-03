package cc.xypp.battery_shield.items.ShieldCore;


import cc.xypp.battery_shield.data.ShieldType;
import net.minecraft.world.item.Item;


public class ShieldCoreR extends Item implements IShieldCore {
    public static final ShieldType coreLevel = ShieldType.SHIELD_RED;
    public static final ShieldType required = ShieldType.SHIELD_PERP;
    @Override
    public ShieldType getCoreLevel() {
        return coreLevel;
    }

    @Override
    public ShieldType getRequired() {
        return required;
    }
    public ShieldCoreR() {
        super(new Properties().stacksTo(1));
    }

}
