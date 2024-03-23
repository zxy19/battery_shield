package cc.xypp.battery_shield.helper;

import cc.xypp.battery_shield.data.UsageEvent;
import cc.xypp.battery_shield.items.SoundRegistry;
import cc.xypp.battery_shield.packet.UsagePackat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class UsageEventManager {
    public static UsageEventManager instance;

    public static UsageEventManager getInstance() {
        return instance != null ? instance : (instance = new UsageEventManager());
    }

    public final SimpleChannel INSTANCE;

    public UsageEventManager() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation("shield_usage", "shield_usage"),
                () -> "1.0.0",
                (v) -> v.equals("1.0.0"),
                (v) -> v.equals("1.0.0")
        );
        INSTANCE.registerMessage(0, UsagePackat.class, UsagePackat::encode, UsagePackat::new, this::handle);
    }

    public void handle(UsagePackat packet, Supplier<NetworkEvent.Context> ctx) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) return;
        ctx.get().enqueueWork(() -> {
            LocalPlayer player = Minecraft.getInstance().player;
            Level level = Minecraft.getInstance().level;
            if(player == null || level == null) return;
            UsageEvent event = packet.getEvent();
            onEvent(level, player, event);
        });
        ctx.get().setPacketHandled(true);
    }

    public void send(ServerPlayer player, UsageEvent usageEvent) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new UsagePackat(usageEvent.ordinal()));
    }

    public void onEvent(Level level, LocalPlayer player, UsageEvent event){
        if(event == UsageEvent.CHARGE_START) {
            level.playSound(player,player.blockPosition(), SoundRegistry.CHARGE_START.get(), SoundSource.PLAYERS,1,1);
        }else if(event == UsageEvent.CHARGE_START_SMALL) {
            level.playSound(player,player.blockPosition(), SoundRegistry.CHARGE_START.get(), SoundSource.PLAYERS,1,1);
        }else if(event == UsageEvent.CHARGE_INTERRUPT){
             Minecraft.getInstance().getSoundManager().stop(SoundRegistry.charge, SoundSource.PLAYERS);
            level.playSound(player,player.blockPosition(), SoundRegistry.CHARGE_INTERRUPT.get(), SoundSource.PLAYERS,1,1);
        }else if(event == UsageEvent.CHARGE_DURATION){
            level.playSound(player,player.blockPosition(), SoundRegistry.CHARGE.get(), SoundSource.PLAYERS,1,1);
        }else if(event == UsageEvent.CHARGE_DURATION_SMALL){
            level.playSound(player,player.blockPosition(), SoundRegistry.CHARGE_SMALL.get(), SoundSource.PLAYERS,1,1);
        }else if(event == UsageEvent.CHARGE_DONE){
            level.playSound(player,player.blockPosition(), SoundRegistry.CHARGE_DONE.get(), SoundSource.PLAYERS,1,1);
        }
    }
}
