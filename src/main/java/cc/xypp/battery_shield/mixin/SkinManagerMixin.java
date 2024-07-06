package cc.xypp.battery_shield.mixin;

import cc.xypp.battery_shield.api.IHttpTextureAccessor;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.HttpTexture;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.resources.SkinManager;

/**
 * These codes are copied and modified from dzwdz/chat_heads under license Mozilla Public License 2.0
 * Original repo: https://github.com/dzwdz/chat_heads
 */
@Mixin(SkinManager.class)
public abstract class SkinManagerMixin {
    @Inject(
            method = "registerTexture(Lcom/mojang/authlib/minecraft/MinecraftProfileTexture;Lcom/mojang/authlib/minecraft/MinecraftProfileTexture$Type;Lnet/minecraft/client/resources/SkinManager$SkinTextureCallback;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/texture/TextureManager;register(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/renderer/texture/AbstractTexture;)V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void chatheads$rememberTextureLocation(MinecraftProfileTexture minecraftProfileTexture, MinecraftProfileTexture.Type type, SkinManager.SkinTextureCallback skinTextureCallback, CallbackInfoReturnable<ResourceLocation> cir,
                                                  String string, ResourceLocation id, AbstractTexture abstractTexture, File file, File file2, HttpTexture httpTexture) {
        if (id.getPath().startsWith("skins/")) {
            ((IHttpTextureAccessor) httpTexture).battery_shield$setTextureLocation(id);
        }
    }
}
