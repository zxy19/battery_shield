package cc.xypp.battery_shield.items.ShieldCore;


import cc.xypp.battery_shield.data.ShieldType;
import net.minecraft.world.item.Item;


public class ShieldCoreP extends Item {
    public static final ShieldType CoreLevel = ShieldType.SHIELD_PERP;
    public ShieldCoreP() {
        super(new Properties().stacksTo(2));
    }

}
