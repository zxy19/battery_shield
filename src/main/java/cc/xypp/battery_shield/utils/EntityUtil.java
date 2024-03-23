package cc.xypp.battery_shield.utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.io.IOException;

public class EntityUtil {
    public static boolean entityLevelIsClient(Entity entity) {
        return entity.level().isClientSide;
    }
}
