package me.tecnio.ahm.check.impl.flight;

import ac.artemis.packet.spigot.wrappers.GPacket;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientPosition;
import me.tecnio.ahm.check.Check;
import me.tecnio.ahm.check.api.annotations.CheckManifest;
import me.tecnio.ahm.check.type.PacketCheck;
import me.tecnio.ahm.check.type.PositionCheck;
import me.tecnio.ahm.data.PlayerData;
import me.tecnio.ahm.exempt.ExemptType;
import me.tecnio.ahm.update.PositionUpdate;

@CheckManifest(name = "Flight", type = "C", description = "Detects if player is not falling off ground")
public class FlightC extends Check implements PositionCheck {

    public FlightC(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PositionUpdate update) {
        boolean exempt = this.isExempt(ExemptType.FLIGHT, ExemptType.CHUNK, ExemptType.JOIN,
                ExemptType.TELEPORT, ExemptType.EXPLOSION, ExemptType.VELOCITY,
                ExemptType.WEB) || data.getPositionTracker().isWater() ||
                data.getPositionTracker().isLava();

        if(data.getPositionTracker().getY() > data.getPositionTracker().getLastY() && !update.isOnGround() && !exempt) {
            if(this.buffer.increase() >= 15)
                this.failNoBan("");
        } else {
            this.buffer.setBuffer(0);
        }
    }
}
