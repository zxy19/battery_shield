package cc.xypp.battery_shield.utils;

import cc.xypp.battery_shield.api.IDamageSourceA;
import cc.xypp.battery_shield.data.DamageNumberType;
import cc.xypp.battery_shield.data.ShieldType;
import cc.xypp.battery_shield.helper.AssetsManager;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public class ShieldUtil {
    public static float getMaxShieldByType(ShieldType type) {
        return switch (type) {
            case RAW -> 0;
            case SHIELD_WHITE -> 20;
            case SHIELD_BLUE -> 30;
            case SHIELD_PERP -> 40;
            case SHIELD_RED -> 50;
        };
    }
    public static AssetsManager.ImageAssets getShieldTypeByValue(float value) {
        if (value > 40) {
            return AssetsManager.SHIELD_RED;
        } else if (value > 30) {
            return AssetsManager.SHIELD_PERPLE;
        } else if (value > 20) {
            return AssetsManager.SHIELD_BLUE;
        } else {
            return AssetsManager.SHIELD_WHITE;
        }
    }
    public static int getColor(DamageNumberType type) {
        return switch (type) {
            case RAW -> 0x5d4037;
            case NORMAL -> 0xf44336;
            case SHIELD_WHITE -> 0xcfd8dc;
            case SHIELD_BLUE -> 0x2196f3;
            case SHIELD_PERP -> 0xab47bc;
            case SHIELD_RED -> 0xff1744;
            default -> 0xFFFFFF;
        };
    }
    public static DamageNumberType getTypeBySourceValue(DamageSource source, float value) {
        if (((IDamageSourceA)(Object)source).isByBatteryShield()) {
            if (value > 40) {
                return DamageNumberType.SHIELD_RED;
            } else if (value > 30) {
                return DamageNumberType.SHIELD_PERP;
            } else if (value > 20) {
                return DamageNumberType.SHIELD_BLUE;
            } else {
                return DamageNumberType.SHIELD_WHITE;
            }
        } else {
            return DamageNumberType.RAW;
        }
    }
    @Nullable
    public static AssetsManager.ImageAssets getIconByType(DamageNumberType type) {
        return switch (type) {
            case SHIELD_WHITE -> AssetsManager.SHIELD_ICON_WHITE;
            case SHIELD_BLUE -> AssetsManager.SHIELD_ICON_BLUE;
            case SHIELD_PERP -> AssetsManager.SHIELD_ICON_PERPLE;
            case SHIELD_RED -> AssetsManager.SHIELD_ICON_RED;
            default -> null;
        };
    }
    public static int getMaxShieldValue(LivingEntity entity) {
        return Mth.clamp((int)Math.ceil(entity.getHealth()/10) * 5, 0, 50);
    }
}
