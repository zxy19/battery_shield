package cc.xypp.battery_shield.mixin;

import cc.xypp.battery_shield.api.IDamageSourceA;
import net.minecraft.world.Container;
import net.minecraft.world.Nameable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public abstract class InventoryMixin  implements Container, Nameable {
    @Inject(method = "hurtArmor", at = @At("HEAD"),cancellable = true)
    public void hurtArmor(DamageSource p_150073_, float p_150074_, int[] p_150075_, CallbackInfo ci) {
        if(((IDamageSourceA)p_150073_).isByBatteryShield()) {
            ci.cancel();
        }
    }
}
