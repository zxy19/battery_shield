package cc.xypp.battery_shield;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = BatteryShield.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.ConfigValue<Integer> SHIELD_PRE_LEVEL
            = BUILDER.translation("battery_shield.shield_pre_level").define("shield_pre_level", 10);
    private static final ForgeConfigSpec.ConfigValue<Integer> BATTERY_VALUE
            = BUILDER.translation("battery_shield.battery_value").define("battery_value", 50);
    private static final ForgeConfigSpec.ConfigValue<Integer> SMALL_BATTERY_VALUE
            = BUILDER.translation("battery_shield.small_battery_value").define("small_battery_value", 10);

    private static final ForgeConfigSpec.ConfigValue<Integer> SHIELD_COOLDOWN
            = BUILDER.translation("battery_shield.sheild_cooldown").define("sheild_cooldown", 60);
    private static final ForgeConfigSpec.BooleanValue MOBS_SHIELD
            = BUILDER.translation("battery_shield.mobs_shield").define("mobs_shield", true);
    private static final ForgeConfigSpec.ConfigValue<Float> MOBS_SHIELD_ADD_PERCENT
            = BUILDER.translation("battery_shield.mobs_shield_add_percent").define("mobs_shield_add_percent", 0.5f);

    private static final ForgeConfigSpec.BooleanValue SHOW_SHIELD
            = BUILDER.translation("battery_shield.display.shield").define("display.shield", true);
    private static final ForgeConfigSpec.BooleanValue SHOW_HEALTH
            = BUILDER.translation("battery_shield.display.health").define("display.health", true);
    private static final ForgeConfigSpec.BooleanValue SHOW_NUMBER
            = BUILDER.translation("battery_shield.display.number").define("display.number", true);
    private static final ForgeConfigSpec.BooleanValue SHOW_HUD
            = BUILDER.translation("battery_shield.display.hud").define("display.hud", true);
    private static final ForgeConfigSpec.BooleanValue SEND_CHARGING_MSG
            = BUILDER.translation("battery_shield.send_charging_msg").define("send_charging_msg", true);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> MESSAGES
            = BUILDER.translation("battery_shield.messages").defineList("messages", List.of("正在使用护盾电池","正在给护盾充能"), o -> o instanceof String);
    private static final ForgeConfigSpec.BooleanValue ZERO_DAMAGE_EVENT
            = BUILDER.translation("battery_shield.zero_damage_event")
            .comment("Set damage to 0 when protected by shield.It will cause some mod cannot recognize this damage.").define("func.zero_damage_event", false);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int shield_pre_level;
    public static int small_battery_value;
    public static int battery_value;
    public static int sheild_cooldown;
    public static boolean mobs_shield;
    public static float mobs_shield_add_percent;
    public static boolean display_shield;
    public static boolean display_health;
    public static boolean display_number;
    public static boolean display_hud;
    public static boolean send_charging_msg;
    public static boolean zero_damage_event;
    public static List<String> messages;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        shield_pre_level = SHIELD_PRE_LEVEL.get();
        sheild_cooldown = SHIELD_COOLDOWN.get();
        mobs_shield = MOBS_SHIELD.get();
        mobs_shield_add_percent = MOBS_SHIELD_ADD_PERCENT.get();
        display_shield = SHOW_SHIELD.get();
        display_health = SHOW_HEALTH.get();
        display_number = SHOW_NUMBER.get();
        display_hud = SHOW_HUD.get();
        send_charging_msg = SEND_CHARGING_MSG.get();
        battery_value = BATTERY_VALUE.get();
        small_battery_value = SMALL_BATTERY_VALUE.get();
        messages = new ArrayList<>();
        messages.addAll(MESSAGES.get());
        zero_damage_event = ZERO_DAMAGE_EVENT.get();
    }
}
