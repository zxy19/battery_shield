package cc.xypp.battery_shield.items.ShieldCore;


import cc.xypp.battery_shield.data.ShieldType;
import net.minecraft.world.item.Item;


public class ShieldCoreB extends Item implements IShieldCore {
    public static final ShieldType coreLevel = ShieldType.SHIELD_BLUE;
    public static final ShieldType required = ShieldType.SHIELD_WHITE;
    public ShieldCoreB() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public ShieldType getCoreLevel() {
        return coreLevel;
    }

    @Override
    public ShieldType getRequired() {
        return required;
    }

}
