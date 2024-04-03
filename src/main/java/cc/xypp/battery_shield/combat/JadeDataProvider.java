package cc.xypp.battery_shield.combat;

import cc.xypp.battery_shield.api.ILivingEntityA;
import cc.xypp.battery_shield.utils.ShieldUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

public enum JadeDataProvider implements IEntityComponentProvider {
    INSTANCE;
    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation("battery_shield", "jade_data_provider");
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
        if(entityAccessor.getEntity() instanceof LivingEntity living){
            ILivingEntityA iliving = (ILivingEntityA) living;
            if(iliving.battery_shield$getMaxShield() > 0){
                String shieldTypeId = ShieldUtil.getShieldIdByType(iliving.battery_shield$getShieldType());
                iTooltip.add(Component.translatable(String.format("tooltip.battery_shield.level.%s", shieldTypeId),
                        String.format("%.1f", iliving.battery_shield$getMaxShield()),
                        String.format("%.1f", iliving.battery_shield$getShield())
                ));
            }
        }
    }
}