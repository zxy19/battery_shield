package cc.xypp.battery_shield.hud;

import cc.xypp.battery_shield.api.ILivingEntityA;
import cc.xypp.battery_shield.helper.AssetsManager;
import cc.xypp.battery_shield.utils.RenderUtils;
import cc.xypp.battery_shield.utils.ShieldUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class PlayerShieldOverlay implements IGuiOverlay {
    PlayerShieldOverlay() {

    }
    protected void renderItem(GuiGraphics guiGraphics,float scale,int x,int y) {
        ItemStack item = new ItemStack(Blocks.PLAYER_HEAD);
        if (Minecraft.getInstance().player != null) {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putUUID("SkullOwner",Minecraft.getInstance().player.getUUID());
            item.setTag(compoundTag);
        }
        guiGraphics.renderItem(item,x,y);
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int width, int height) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null)return;
        ILivingEntityA iliving = ((ILivingEntityA) (player));
        float max = iliving.effect_test$getMaxShield();
        float shield = iliving.effect_test$getShield();

        RenderUtils.renderBar(guiGraphics, 40, height - 25, 96, 6, AssetsManager.SHIELD_BORDER, ShieldUtil.getShieldTypeByValue(max), shield,max);
        RenderUtils.renderHealth(guiGraphics, 40, height - 20, 96, 6, player.getHealth(), player.getMaxHealth());
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(0.8f, 0.8f, 0.8f);
        guiGraphics.drawString(Minecraft.getInstance().font, player.getName(), (int)(45/0.8), (int)((height - 32)/0.8), 0xffffffff);
        guiGraphics.pose().popPose();
        guiGraphics.pose().pushPose();
        this.renderItem(guiGraphics,5f,25/5,(height - 23)/5);
        guiGraphics.pose().popPose();
    }
}