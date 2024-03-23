package cc.xypp.battery_shield.items.ShieldCore;


import cc.xypp.battery_shield.data.ShieldType;
import net.minecraft.world.item.Item;


public class ShieldCoreW extends Item {
    public static final ShieldType CoreLevel = ShieldType.SHIELD_WHITE;
    public ShieldCoreW() {
        super(new Properties().stacksTo(2));
    }
}