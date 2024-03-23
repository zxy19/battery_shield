package cc.xypp.battery_shield.packet;

import net.minecraft.network.FriendlyByteBuf;

public class TrackingPackat {
    public int id;
    public float shield;
    public float maxShield;

    public TrackingPackat(int id, float shield,float maxShield) {
        this.id = id;
        this.shield = shield;
        this.maxShield = maxShield;
    }

    public TrackingPackat(FriendlyByteBuf packetBuffer) {
        this.id = packetBuffer.readInt();
        this.shield = packetBuffer.readFloat();
        this.maxShield = packetBuffer.readFloat();
    }

    public void encode(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeInt(id);
        packetBuffer.writeFloat(shield);
        packetBuffer.writeFloat(maxShield);
    }
}
