package cc.xypp.battery_shield.items;

import cc.xypp.battery_shield.Config;
import cc.xypp.battery_shield.api.ILivingEntityA;
import cc.xypp.battery_shield.data.UsageEvent;
import cc.xypp.battery_shield.helper.UsageEventManager;
import cc.xypp.battery_shield.utils.MiscUtil;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;


public class PhoenixBattery extends Item {
    private static final int USE_DURATION = 180;

    public PhoenixBattery() {
        super(new Properties().stacksTo(2));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack p_41409_, Level p_41410_, LivingEntity p_41411_) {
        ILivingEntityA iLivingEntityA = (ILivingEntityA) p_41411_;
        if (p_41411_ instanceof ServerPlayer sp) {
            sp.getCooldowns().addCooldown(this, Config.sheild_cooldown);
            sp.getCooldowns().addCooldown(Register.smallBattery.get(), Config.sheild_cooldown);
            sp.getCooldowns().addCooldown(Register.phoenixBattery.get(), Config.sheild_cooldown);
        }
        iLivingEntityA.battery_shield$setShield(Math.min((iLivingEntityA).battery_shield$getShield() + Config.battery_value, (iLivingEntityA).battery_shield$getMaxShield()));
        return super.finishUsingItem(p_41409_, p_41410_, p_41411_);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack p_41454_) {
        return USE_DURATION;
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        super.onStopUsing(stack, entity, count);
        if (count == 0) {
            if (entity instanceof ServerPlayer sp) {
                UsageEventManager.getInstance().send(sp, UsageEvent.CHARGE_DONE);
                sp.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, 50));
                stack.setCount(stack.getCount() - 1);
            }
        } else if (count > 0) {
            if (entity instanceof ServerPlayer sp) {
                UsageEventManager.getInstance().send(sp, UsageEvent.CHARGE_INTERRUPT);
            }
        }
    }

    @Override
    public void onUseTick(@NotNull Level p_41428_, @NotNull LivingEntity p_41429_, @NotNull ItemStack p_41430_, int p_41431_) {
        if (p_41431_ == USE_DURATION - 10) {
            if (p_41429_ instanceof ServerPlayer sp) {
                UsageEventManager.getInstance().send(sp, UsageEvent.CHARGE_DURATION_PHOENIX);
            }
        }
        super.onUseTick(p_41428_, p_41429_, p_41430_, p_41431_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level p_41432_, @NotNull Player p_41433_, @NotNull InteractionHand p_41434_) {
        if (p_41433_ instanceof ServerPlayer sp) {
            UsageEventManager.getInstance().send(sp, UsageEvent.CHARGE_START_PHOENIX);
        }else if(p_41433_ instanceof  LocalPlayer lp){
            lp.connection.sendChat(MiscUtil.getMessagePhoenix());
        }
        p_41433_.startUsingItem(p_41434_);
        return InteractionResultHolder.consume(p_41433_.getItemInHand(p_41434_));
    }
}
