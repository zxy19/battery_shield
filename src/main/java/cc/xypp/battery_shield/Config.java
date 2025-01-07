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
            = BUILDER.translation("battery_shield.shield_pre_level")
            .defineInRange("shield_pre_level", 10,0,Integer.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<Integer> BATTERY_VALUE
            = BUILDER.translation("battery_shield.battery_value")
            .defineInRange("battery_value", 50,0,Integer.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<Integer> SMALL_BATTERY_VALUE
            = BUILDER.translation("battery_shield.small_battery_value")
            .defineInRange("small_battery_value", 10,0,Integer.MAX_VALUE);
    private static final ForgeConfigSpec.BooleanValue ALLOW_EVOLVE
            = BUILDER.translation("battery_shield.evolve.allow")
            .define("evolve.allow", false);
    private static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> EVOLVE_REQUIREMENT
            = BUILDER.translation("battery_shield.evolve.requirement")
            .defineList("evolve.requirement", List.of(40,120,360), o -> o instanceof Integer);


    private static final ForgeConfigSpec.ConfigValue<Integer> SHIELD_COOLDOWN
            = BUILDER.translation("battery_shield.sheild_cooldown").define("sheild_cooldown", 60);
    private static final ForgeConfigSpec.BooleanValue MOBS_SHIELD
            = BUILDER.translation("battery_shield.mobs_shield").define("mobs_shield", true);
    private static final ForgeConfigSpec.DoubleValue MOBS_SHIELD_ADD_PERCENT
            = BUILDER.translation("battery_shield.mobs_shield_add_percent")
            .defineInRange("mobs_shield_add_percent", 0.5f,0.0f,1.0f);

    private static final ForgeConfigSpec.BooleanValue SHOW_SHIELD
            = BUILDER.translation("battery_shield.display.shield").define("display.shield", true);
    private static final ForgeConfigSpec.BooleanValue SHOW_EVOLVE_POINT
            = BUILDER.translation("battery_shield.display.evolve").define("display.evolve", true);
    private static final ForgeConfigSpec.BooleanValue SHOW_HEALTH
            = BUILDER.translation("battery_shield.display.health").define("display.health", true);
    private static final ForgeConfigSpec.BooleanValue SHOW_NUMBER
            = BUILDER.translation("battery_shield.display.number").define("display.number", true);
    private static final ForgeConfigSpec.BooleanValue SHOW_HUD
            = BUILDER.translation("battery_shield.display.hud").define("display.hud", true);
    private static final ForgeConfigSpec.BooleanValue USE_2D_HEAD
            = BUILDER.translation("battery_shield.use_2d_head")
            .define("misc.use_2d_head", true);
    private static final ForgeConfigSpec.BooleanValue SEND_CHARGING_MSG
            = BUILDER.translation("battery_shield.send_charging_msg").define("send_charging_msg", true);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> MESSAGES
            = BUILDER.translation("battery_shield.messages").defineList("messages", List.of("正在使用护盾电池", "正在给护盾充能"), o -> o instanceof String);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> MESSAGES_PHOENIX
            = BUILDER.translation("battery_shield.messages_phoenix").defineList("messages_phoenix", List.of("正在使用凤凰治疗包"), o -> o instanceof String);

    private static final ForgeConfigSpec.BooleanValue ZERO_DAMAGE_EVENT
            = BUILDER.translation("battery_shield.zero_damage_event")
            .comment("Set damage to 0 when protected by shield.It will cause some mod cannot recognize this damage.")
            .define("func.zero_damage_event", false);
    private static final ForgeConfigSpec.BooleanValue CALC_DAMAGE_WITH_EVENT
            = BUILDER.translation("battery_shield.calc_damage_with_event")
            .comment("Calculate damage on shield with event. All damage on shield will be calculated with our mixin function in LivingEntity.hurt after any other hooks. If enabled, we will use forge event with lowest priority instead.")
            .define("func.calc_damage_with_event", false);

    private static final ForgeConfigSpec.BooleanValue PREVENT_DAMAGE_OVERFLOW
            = BUILDER.translation("battery_shield.prevent_damage_overflow")
            .comment("Prevent damage overflow,i.e. allow shield to clear damage greater than the shield value.")
            .define("func.prevent_damage_overflow", true);
    private static final ForgeConfigSpec.BooleanValue SOUND_GLASS_BREAK_SOUND
            = BUILDER.translation("battery_shield.use_glass_break_sound")
            .define("misc.use_glass_break_sound", false);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int shield_pre_level;
    public static int small_battery_value;
    public static int battery_value;
    public static int sheild_cooldown;
    public static boolean mobs_shield;
    public static double mobs_shield_add_percent;
    public static boolean display_shield;
    public static boolean display_health;
    public static boolean display_number;
    public static boolean display_hud;
    public static boolean send_charging_msg;
    public static boolean zero_damage_event;
    public static boolean calc_damage_with_event;
    public static boolean prevent_damage_overflow;
    public static boolean use_glass_break_sound;
    public static boolean use_2d_head;
    public static List<String> messages;
    public static List<String> messagesPhoenix;

    public static List<Integer> evolve_requirement;
    public static boolean allow_evolve;
    public static boolean display_evolve;

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
        use_2d_head = USE_2D_HEAD.get();
        send_charging_msg = SEND_CHARGING_MSG.get();
        battery_value = BATTERY_VALUE.get();
        small_battery_value = SMALL_BATTERY_VALUE.get();
        messages = new ArrayList<>();
        messages.addAll(MESSAGES.get());
        messagesPhoenix = new ArrayList<>();
        messagesPhoenix.addAll(MESSAGES_PHOENIX.get());
        zero_damage_event = ZERO_DAMAGE_EVENT.get();
        calc_damage_with_event = CALC_DAMAGE_WITH_EVENT.get();
        use_glass_break_sound = SOUND_GLASS_BREAK_SOUND.get();
        evolve_requirement = new ArrayList<>();
        evolve_requirement.addAll(EVOLVE_REQUIREMENT.get());
        allow_evolve = ALLOW_EVOLVE.get();
        display_evolve = SHOW_EVOLVE_POINT.get();
        prevent_damage_overflow = PREVENT_DAMAGE_OVERFLOW.get();
    }
}
