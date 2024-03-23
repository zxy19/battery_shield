package cc.xypp.battery_shield.recipes;

import cc.xypp.battery_shield.BatteryShield;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collections;
import java.util.List;


public class RecipesRegistry {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, BatteryShield.MODID);
    public static final RegistryObject<RecipeSerializer<?>> SHIELD_UPGRADE_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("shield_upgrade", ShieldCoreUpgrade.Serializer::new);
}
