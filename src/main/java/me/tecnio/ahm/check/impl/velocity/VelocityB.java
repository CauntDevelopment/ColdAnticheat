package me.tecnio.ahm.check.impl.velocity;

import ac.artemis.packet.spigot.wrappers.GPacket;
import ac.artemis.packet.wrapper.client.PacketPlayClientFlying;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientFlying;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientTransaction;
import cc.ghast.packet.wrapper.packet.play.server.GPacketPlayServerEntityVelocity;
import cc.ghast.packet.wrapper.packet.play.server.GPacketPlayServerTransaction;
import me.tecnio.ahm.check.Check;
import me.tecnio.ahm.check.api.annotations.CheckManifest;
import me.tecnio.ahm.check.type.PacketCheck;
import me.tecnio.ahm.check.type.PositionCheck;
import me.tecnio.ahm.data.PlayerData;
import me.tecnio.ahm.data.tracker.impl.EmulationTracker;
import me.tecnio.ahm.data.tracker.impl.PositionTracker;
import me.tecnio.ahm.data.tracker.impl.VelocityTracker;
import me.tecnio.ahm.exempt.ExemptType;
import me.tecnio.ahm.update.PositionUpdate;

@CheckManifest(name = "Velocity", type = "B", description = "Checks if the player has moved horizontaly")
public class VelocityB extends Check implements PositionCheck {

    public VelocityB(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PositionUpdate update) {
        VelocityTracker velocityTracker = data.getVelocityTracker();
        EmulationTracker emulationTracker = data.getEmulationTracker();
        PositionTracker positionTracker = data.getPositionTracker();

        boolean exemptions = this.isExempt(ExemptType.FLIGHT, ExemptType.BOAT, ExemptType.VEHICLE, ExemptType.WEB,
                ExemptType.TELEPORTED_RECENTLY, ExemptType.LIQUID, ExemptType.CLIMBABLE,
                ExemptType.SOUL_SAND, ExemptType.SLIME, ExemptType.UNDER_BLOCK, ExemptType.WALL);

        if(velocityTracker.getTicksSinceVelocity() > 6 || exemptions) {
            return;
        }

        double invalidMove = this.isExempt(ExemptType.RETARD) ? 0.03D * positionTracker.getDelayedFlyingTicks() : 0.0000001;

        double movement = emulationTracker.getDistance();

        if(movement > invalidMove) {
            if(this.buffer.increase() > 3) {
                this.failNoBan("");
                this.executeSetbackToPosition(positionTracker.getLastX(), positionTracker.getDeltaY(), positionTracker.getLastZ());
            }
        } else {
            this.buffer.decreaseBy(1.2);
        }
    }
}
