package me.tecnio.ahm.check.impl.velocity;

import ac.artemis.packet.spigot.wrappers.GPacket;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientFlying;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientTransaction;
import cc.ghast.packet.wrapper.packet.play.server.GPacketPlayServerEntityVelocity;
import cc.ghast.packet.wrapper.packet.play.server.GPacketPlayServerTransaction;
import me.tecnio.ahm.check.Check;
import me.tecnio.ahm.check.api.annotations.CheckManifest;
import me.tecnio.ahm.check.type.PacketCheck;
import me.tecnio.ahm.data.PlayerData;
import me.tecnio.ahm.data.tracker.impl.PositionTracker;
import me.tecnio.ahm.data.tracker.impl.VelocityTracker;
import me.tecnio.ahm.exempt.ExemptType;

@CheckManifest(name = "Velocity", type = "B", description = "Checks if the player has moved horizontaly")
public class VelocityB extends Check implements PacketCheck {

    public boolean receivedTransaction;

    public VelocityB(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(GPacket packet) {
        if(packet instanceof GPacketPlayServerEntityVelocity) {
            data.send(new GPacketPlayServerTransaction((byte) 0,
                    data.getConnectionTracker().getTransactionId(), false));
        }

        if(packet instanceof GPacketPlayClientTransaction) {
            receivedTransaction = true;
        }

        if(packet instanceof GPacketPlayClientFlying) {
            if(receivedTransaction) {

            }
            receivedTransaction = false;
        }
    }
}
