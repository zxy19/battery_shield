package cc.xypp.battery_shield.recipes;

import com.google.gson.JsonObject;
import io.netty.util.SuppressForbidden;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class ShieldCoreUpgrade  extends SmithingTransformRecipe implements SmithingRecipe {

    public ShieldCoreUpgrade(SmithingTransformRecipe smithingTransformRecipe) {
        super(smithingTransformRecipe.getId(),
                smithingTransformRecipe.template,
                smithingTransformRecipe.base,
                smithingTransformRecipe.addition,
                smithingTransformRecipe.result);
    }


    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ItemStack assemble(Container inv, RegistryAccess registryAccess) {
        ItemStack upgradedBackpack = result.copy();
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
            ItemStack item = inv.getItem(1);
            if (item.getTag() != null) {
                item.getTag().putString("battery_shield_upgrade", tag.getString("battery_shield_upgrade"));
            }
            return item;
        }
        return upgradedBackpack;
    }



    public static class Serializer implements RecipeSerializer<SmithingTransformRecipe> {

        @Override
        public SmithingTransformRecipe fromJson(ResourceLocation p_44103_, JsonObject p_44104_) {
            return new ShieldCoreUpgrade(RecipeSerializer.SMITHING_TRANSFORM.fromJson(p_44103_, p_44104_));
        }

        @Override
        public @Nullable SmithingTransformRecipe fromNetwork(ResourceLocation p_44105_, FriendlyByteBuf p_44106_) {
            @Nullable SmithingTransformRecipe compose = RecipeSerializer.SMITHING_TRANSFORM.fromNetwork(p_44105_, p_44106_);
            return compose == null ? null : new ShieldCoreUpgrade(compose);
        }

        @Override
        public void toNetwork(FriendlyByteBuf p_44101_, SmithingTransformRecipe p_44102_) {
            RecipeSerializer.SMITHING_TRANSFORM.toNetwork(p_44101_, p_44102_);
        }
    }
}