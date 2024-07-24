package me.tecnio.ahm.check.impl.flight;

import ac.artemis.packet.spigot.wrappers.GPacket;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientPosition;
import com.avaje.ebeaninternal.server.type.reflect.CheckImmutable;
import me.tecnio.ahm.check.Check;
import me.tecnio.ahm.check.api.annotations.CheckManifest;
import me.tecnio.ahm.check.type.PacketCheck;
import me.tecnio.ahm.check.type.PositionCheck;
import me.tecnio.ahm.data.PlayerData;
import me.tecnio.ahm.exempt.ExemptType;
import me.tecnio.ahm.update.PositionUpdate;

@CheckManifest(name = "Flight", type = "A", description = "Detects static Y motion")
public class FlightA extends Check implements PositionCheck {

    public FlightA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PositionUpdate update) {
        boolean exempt = this.isExempt(ExemptType.CHUNK, ExemptType.BOAT, ExemptType.FLIGHT,
                ExemptType.JOIN, ExemptType.TELEPORT, ExemptType.UNDER_BLOCK,
                ExemptType.STEP);

        if(data.getPositionTracker().getLastY() == data.getPositionTracker().getLastLastY() && !exempt && !update.isOnGround()) {
            if(this.buffer.increase() > 5) {
                this.failNoBan("y=" + data.getPositionTracker().getY() + "==" + data.getPositionTracker().getLastLastY());
                this.executeSetback(true, false);
            }
        } else {
            this.buffer.setBuffer(0);
        }
    }
}
