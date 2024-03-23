package cc.xypp.battery_shield.packet;

import cc.xypp.battery_shield.data.UsageEvent;
import net.minecraft.network.FriendlyByteBuf;

public class UsagePackat {
    UsageEvent event;
    public UsagePackat(int event) {
        this.event = UsageEvent.values()[event];
    }
    public UsagePackat(FriendlyByteBuf packetBuffer) {
        this(packetBuffer.readInt());
    }
    public void encode(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeInt(this.event.ordinal());
    }

    public UsageEvent getEvent() {
        return this.event;
    }
}