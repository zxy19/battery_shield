package cc.xypp.battery_shield.items.ShieldCore;


import cc.xypp.battery_shield.data.ShieldType;
import net.minecraft.world.item.Item;


public class ShieldCoreB extends Item implements IShieldCore {
    public static final ShieldType CoreLevel = ShieldType.SHIELD_BLUE;
    public ShieldCoreB() {
        super(new Properties().stacksTo(2));
    }

}
