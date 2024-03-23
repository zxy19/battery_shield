package cc.xypp.battery_shield.items;

import cc.xypp.battery_shield.api.ILivingEntityA;
import cc.xypp.battery_shield.data.UsageEvent;
import cc.xypp.battery_shield.helper.UsageEventManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class SmallBattery extends Item {

    private static final int USE_DURATION = 30;
    private static final int MAX_SHIELD = 20;
    private static final int ADD_SHIELD = 10;
    public SmallBattery() {
        super(new Properties().stacksTo(2));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack p_41409_, Level p_41410_, LivingEntity p_41411_) {
        ILivingEntityA iLivingEntityA = (ILivingEntityA) p_41411_;
        if(iLivingEntityA.effect_test$getMaxShield() < MAX_SHIELD){
            iLivingEntityA.effect_test$setMaxShield(MAX_SHIELD);
        }
        if(p_41411_ instanceof ServerPlayer sp){
            sp.getCooldowns().addCooldown(this,40);
            sp.getCooldowns().addCooldown(Register.battery.get(),40);
        }
        iLivingEntityA.effect_test$setShield(Math.min((iLivingEntityA).effect_test$getShield() + ADD_SHIELD, (iLivingEntityA).effect_test$getMaxShield()));
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
        if(count == 0){
            this.finishUsingItem(stack,entity.level(),entity);
            if(entity instanceof ServerPlayer sp){
                UsageEventManager.getInstance().send(sp,UsageEvent.CHARGE_DONE);
            }
        }else if(count > 0){
            if(entity instanceof ServerPlayer sp){
                UsageEventManager.getInstance().send(sp,UsageEvent.CHARGE_INTERRUPT);
            }
        }
    }

    @Override
    public void onUseTick(@NotNull Level p_41428_, @NotNull LivingEntity p_41429_, @NotNull ItemStack p_41430_, int p_41431_) {
        if(p_41431_ == USE_DURATION - 10) {
            if (p_41429_ instanceof ServerPlayer sp) {
                UsageEventManager.getInstance().send(sp, UsageEvent.CHARGE_DURATION_SMALL);
            }
        }
        super.onUseTick(p_41428_, p_41429_, p_41430_, p_41431_);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if(context.getPlayer()==null)return InteractionResult.FAIL;
        ILivingEntityA iLivingEntityA = (ILivingEntityA) context.getPlayer();
        float max = Math.max(iLivingEntityA.effect_test$getMaxShield(),MAX_SHIELD);
        if(max == iLivingEntityA.effect_test$getShield()){
            return InteractionResult.FAIL;
        }
        return super.onItemUseFirst(stack, context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level p_41432_, @NotNull Player p_41433_, @NotNull InteractionHand p_41434_) {
        if(p_41433_ instanceof ServerPlayer sp){
            UsageEventManager.getInstance().send(sp,UsageEvent.CHARGE_START_SMALL);
        }
        p_41433_.startUsingItem(p_41434_);
        return InteractionResultHolder.consume(p_41433_.getItemInHand(p_41434_));
    }
}
