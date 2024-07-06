package cc.xypp.battery_shield.packet;

import cc.xypp.battery_shield.data.DamageNumberType;
import net.minecraft.network.FriendlyByteBuf;

public class DamagePacket {
    public boolean isBreakShield;
    public float number;
    public DamageNumberType type;
    public int entityId;
    public DamagePacket(float number, DamageNumberType type,boolean isBreaking, int entityId) {
        this.number = number;
        this.entityId = entityId;
        this.type = type;
        this.isBreakShield = isBreaking;
    }

    public DamagePacket(FriendlyByteBuf packetBuffer) {
        this.number = packetBuffer.readFloat();
        this.type = DamageNumberType.values()[packetBuffer.readInt()];
        this.entityId = packetBuffer.readInt();
        this.isBreakShield = packetBuffer.readBoolean();
    }

    public void encode(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeFloat(number);
        packetBuffer.writeInt(type.ordinal());
        packetBuffer.writeInt(entityId);
        packetBuffer.writeBoolean(isBreakShield);
    }
}
