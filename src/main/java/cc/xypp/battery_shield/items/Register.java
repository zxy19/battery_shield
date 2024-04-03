package cc.xypp.battery_shield.items;

import cc.xypp.battery_shield.BatteryShield;
import cc.xypp.battery_shield.items.ShieldCore.ShieldCoreB;
import cc.xypp.battery_shield.items.ShieldCore.ShieldCoreR;
import cc.xypp.battery_shield.items.ShieldCore.ShieldCoreW;
import cc.xypp.battery_shield.items.ShieldCore.ShieldCoreP;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Register {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BatteryShield.MODID);
    public static final RegistryObject<Item> smallBattery = ITEMS.register("small_battery", SmallBattery::new);
    public static final RegistryObject<Item> battery = ITEMS.register("battery", Battery::new);
    public static final RegistryObject<Item> steel = ITEMS.register("steel", Steel::new);
    public static final RegistryObject<Item> shield_core_b = ITEMS.register("shield_core_b", ShieldCoreB::new);
    public static final RegistryObject<Item> shield_core_r = ITEMS.register("shield_core_r", ShieldCoreR::new);
    public static final RegistryObject<Item> shield_core_p = ITEMS.register("shield_core_p", ShieldCoreP::new);
    public static final RegistryObject<Item> shield_core_w = ITEMS.register("shield_core_w", ShieldCoreW::new);

    public static final DeferredRegister<CreativeModeTab> TAB_DEFERRED_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BatteryShield.MODID);
    public static final RegistryObject<CreativeModeTab> BATTERY_SHIELD_TAB = TAB_DEFERRED_REGISTER.register("example", () -> CreativeModeTab.builder()
            // Set name of tab to display
            .title(Component.translatable("item_group." + BatteryShield.MODID + ".main"))
            // Set icon of creative tab
            .icon(() -> new ItemStack(battery.get()))
            // Add default items to tab
            .displayItems((params, output) -> {
                output.accept(battery.get());
                output.accept(smallBattery.get());
                output.accept(steel.get());
                output.accept(shield_core_b.get());
                output.accept(shield_core_r.get());
                output.accept(shield_core_p.get());
                output.accept(shield_core_w.get());
            })
            .build()
    );
}
