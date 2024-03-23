package cc.xypp.battery_shield;

import cc.xypp.battery_shield.helper.DamageNumberManager;
import cc.xypp.battery_shield.helper.TrackingManager;
import cc.xypp.battery_shield.helper.UsageEventManager;
import cc.xypp.battery_shield.items.Register;
import cc.xypp.battery_shield.items.SoundRegistry;
import cc.xypp.battery_shield.recipes.RecipesRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("battery_shield")
public class BatteryShield {
    public static final String MODID = "battery_shield";

    public BatteryShield() {
        TrackingManager.getInstance();
        DamageNumberManager.getInstance();
        UsageEventManager.getInstance();
        RecipesRegistry.RECIPE_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        Register.TAB_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        Register.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        SoundRegistry.SOUND.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
