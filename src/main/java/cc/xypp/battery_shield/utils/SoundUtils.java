package cc.xypp.battery_shield.utils;

import cc.xypp.battery_shield.Config;
import cc.xypp.battery_shield.data.UsageEvent;
import cc.xypp.battery_shield.items.SoundRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;

public class SoundUtils {
    public static void onSoundEvent(UsageEvent event) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) return;
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        Level level = mc.level;
        if(level == null || player == null) return;
        if (event == UsageEvent.CHARGE_START) {
            level.playSound(player, player.blockPosition(), SoundRegistry.CHARGE_START.get(), SoundSource.PLAYERS, 1, 1);
        } else if (event == UsageEvent.CHARGE_START_SMALL) {
            level.playSound(player, player.blockPosition(), SoundRegistry.CHARGE_START.get(), SoundSource.PLAYERS, 1, 1);
        } else if (event == UsageEvent.CHARGE_START_PHOENIX) {
            level.playSound(player, player.blockPosition(), SoundRegistry.CHARGE_START.get(), SoundSource.PLAYERS, 1, 1);
        } else if (event == UsageEvent.CHARGE_INTERRUPT) {
            mc.getSoundManager().stop(SoundRegistry.charge, SoundSource.PLAYERS);
            mc.getSoundManager().stop(SoundRegistry.charge_small, SoundSource.PLAYERS);
            mc.getSoundManager().stop(SoundRegistry.charge_phoenix, SoundSource.PLAYERS);
            level.playSound(player, player.blockPosition(), SoundRegistry.CHARGE_INTERRUPT.get(), SoundSource.PLAYERS, 1, 1);
        } else if (event == UsageEvent.CHARGE_DURATION) {
            level.playSound(player, player.blockPosition(), SoundRegistry.CHARGE.get(), SoundSource.PLAYERS, 1, 1);
        } else if (event == UsageEvent.CHARGE_DURATION_SMALL) {
            level.playSound(player, player.blockPosition(), SoundRegistry.CHARGE_SMALL.get(), SoundSource.PLAYERS, 1, 1);
        } else if (event == UsageEvent.CHARGE_DURATION_PHOENIX) {
            level.playSound(player, player.blockPosition(), SoundRegistry.CHARGE_PHOENIX.get(), SoundSource.PLAYERS, 1, 1);
        } else if (event == UsageEvent.CHARGE_DONE) {
            level.playSound(player, player.blockPosition(), SoundRegistry.CHARGE_DONE.get(), SoundSource.PLAYERS, 1, 1);
        } else if(event == UsageEvent.SHIELD_BREAK){
            if(Config.use_glass_break_sound){
                level.playSound(player, player.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1, 1);
            }else{
                level.playSound(player, player.blockPosition(), SoundRegistry.SHIELD_BREAK.get(), SoundSource.PLAYERS, 1, 1);
            }
        }
    }
}
