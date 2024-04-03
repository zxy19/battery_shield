package cc.xypp.battery_shield.items.ShieldCore;


import cc.xypp.battery_shield.data.ShieldType;
import net.minecraft.world.item.Item;


public class ShieldCoreW extends Item implements IShieldCore {
    public static final ShieldType coreLevel = ShieldType.SHIELD_WHITE;
    public static final ShieldType required = ShieldType.RAW;
    @Override
    public ShieldType getCoreLevel() {
        return coreLevel;
    }

    @Override
    public ShieldType getRequired() {
        return required;
    }
    public ShieldCoreW() {
        super(new Properties().stacksTo(1));
    }
}