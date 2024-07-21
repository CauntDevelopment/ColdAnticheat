package me.tecnio.ahm.check.impl.badpackets;

import ac.artemis.packet.spigot.wrappers.GPacket;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientFlying;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientKeepAlive;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientPosition;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientTransaction;
import me.tecnio.ahm.check.Check;
import me.tecnio.ahm.check.api.annotations.CheckManifest;
import me.tecnio.ahm.check.type.PacketCheck;
import me.tecnio.ahm.data.PlayerData;
import me.tecnio.ahm.exempt.ExemptType;

@CheckManifest(name = "BadPackets", type = "A", description = "Detects transaction cancels.")
public class BadPacketsA extends Check implements PacketCheck {

    private long lastPosition, lastTransaction;
    private boolean transactionSent;

    public BadPacketsA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(GPacket packet) {
        if(packet instanceof GPacketPlayClientTransaction) {
            lastTransaction = System.currentTimeMillis();
        } else if(packet instanceof GPacketPlayClientKeepAlive) {
            transactionSent = System.currentTimeMillis() - lastTransaction < 25000;
        } else if(packet instanceof GPacketPlayClientPosition) {
            boolean exempt = this.isExempt(ExemptType.CHUNK, ExemptType.TELEPORT, ExemptType.JOIN);
            if(!transactionSent && !exempt) {
                this.failNoBan("no transaction");
            }
        }
    }
}
