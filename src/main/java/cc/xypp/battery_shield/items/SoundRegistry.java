package cc.xypp.battery_shield.items;

import cc.xypp.battery_shield.BatteryShield;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistry {
    public static DeferredRegister<SoundEvent> SOUND = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BatteryShield.MODID);


    public static ResourceLocation charge_start = new ResourceLocation(BatteryShield.MODID, "battery_charge_start");
    public static RegistryObject<SoundEvent> CHARGE_START = SOUND.register("battery_charge_start", () -> SoundEvent.createVariableRangeEvent(charge_start));

    public static ResourceLocation charge = new ResourceLocation(BatteryShield.MODID, "battery_charge");
    public static RegistryObject<SoundEvent> CHARGE = SOUND.register("battery_charge", () -> SoundEvent.createVariableRangeEvent(charge));

    public static ResourceLocation charge_small = new ResourceLocation(BatteryShield.MODID, "battery_charge_small");
    public static RegistryObject<SoundEvent> CHARGE_SMALL = SOUND.register("battery_charge_small", () -> SoundEvent.createVariableRangeEvent(charge_small));
    public static ResourceLocation charge_phoenix = new ResourceLocation(BatteryShield.MODID, "battery_charge_phoenix");
    public static RegistryObject<SoundEvent> CHARGE_PHOENIX = SOUND.register("battery_charge_phoenix", () -> SoundEvent.createVariableRangeEvent(charge_phoenix));

    public static ResourceLocation charge_interrupt = new ResourceLocation(BatteryShield.MODID, "battery_charge_interrupt");
    public static RegistryObject<SoundEvent> CHARGE_INTERRUPT = SOUND.register("battery_charge_interrupt", () -> SoundEvent.createVariableRangeEvent(charge_interrupt));

    public static ResourceLocation charge_done = new ResourceLocation(BatteryShield.MODID, "battery_charge_done");
    public static RegistryObject<SoundEvent> CHARGE_DONE = SOUND.register("battery_charge_done", () -> SoundEvent.createVariableRangeEvent(charge_done));

    public static ResourceLocation shield_break = new ResourceLocation(BatteryShield.MODID, "battery_shield_break");
    public static RegistryObject<SoundEvent> SHIELD_BREAK = SOUND.register("shield_break", () -> SoundEvent.createVariableRangeEvent(shield_break));
}
