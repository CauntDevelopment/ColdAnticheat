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

@CheckManifest(name = "Flight", type = "B", description = "Invalid motion y")
public class FlightB extends Check implements PositionCheck {

    public FlightB(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PositionUpdate update) {
        double motionY = data.getPositionTracker().getLastDeltaY();

        boolean exempt = this.isExempt(ExemptType.FLIGHT, ExemptType.CHUNK, ExemptType.JOIN,
                ExemptType.TELEPORT, ExemptType.EXPLOSION, ExemptType.VELOCITY,
                ExemptType.WEB, ExemptType.STEP, ExemptType.SLIME, ExemptType.CLIMBABLE,
                ExemptType.LIQUID);

        if(motionY > 0.1D && !update.isOnGround() && !exempt) {
            if(this.buffer.increase() > 10) {
                this.failNoBan("");
                this.executeSetback();
            }
        } else {
            this.buffer.setBuffer(0);
        }
    }
}
