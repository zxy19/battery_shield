package cc.xypp.battery_shield.mixin;

import cc.xypp.battery_shield.BatteryShield;
import cc.xypp.battery_shield.utils.RenderUtils;
import cc.xypp.battery_shield.api.IHttpTextureAccessor;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.HttpTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static cc.xypp.battery_shield.utils.RenderUtils.extractBlendedHead;
/**
 * These codes are copied and modified from dzwdz/chat_heads under license Mozilla Public License 2.0
 * Original repo: https://github.com/dzwdz/chat_heads
 */
@Mixin(HttpTexture.class)
public abstract class HttpTextureMixin implements IHttpTextureAccessor{
    @Unique
    @Nullable
    private ResourceLocation chatheads$textureLocation;

    public void battery_shield$setTextureLocation(ResourceLocation textureLocation) {
        chatheads$textureLocation = textureLocation;
    }

    @Inject(method = "loadCallback", at = @At("HEAD"))
    public void battery_shield$registerBlendedHeadTexture(NativeImage image, CallbackInfo ci) {
        // not a skin texture
        // note: mods like Essential don't use SkinManager and textureLocation is never set
        if (chatheads$textureLocation == null)
            return;

        Minecraft.getInstance().getTextureManager()
                .register(new ResourceLocation(BatteryShield.MODID,chatheads$textureLocation.getPath()),
                        new DynamicTexture(extractBlendedHead(image)));

        RenderUtils.blendedHeadTextures.add(chatheads$textureLocation);
    }
}
