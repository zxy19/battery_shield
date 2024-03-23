package cc.xypp.battery_shield.hud;

import cc.xypp.battery_shield.BatteryShield;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid= BatteryShield.MODID, bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class HudRegister {
    @SubscribeEvent
    public static void RegisterGuiOverlaysEvent(RegisterGuiOverlaysEvent event){
        PlayerShieldOverlay playerShieldOverlay = new PlayerShieldOverlay();
        event.registerAboveAll("battery_shield_overlay",playerShieldOverlay);
    }
}
