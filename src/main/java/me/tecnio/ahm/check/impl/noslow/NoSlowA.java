package me.tecnio.ahm.check.impl.noslow;

import ac.artemis.packet.spigot.wrappers.GPacket;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientPosition;
import me.tecnio.ahm.check.Check;
import me.tecnio.ahm.check.api.annotations.CheckManifest;
import me.tecnio.ahm.check.type.PacketCheck;
import me.tecnio.ahm.check.type.PositionCheck;
import me.tecnio.ahm.data.PlayerData;
import me.tecnio.ahm.update.PositionUpdate;
import me.tecnio.ahm.util.math.MathUtil;
import me.tecnio.ahm.util.player.PlayerUtil;

@CheckManifest(name = "NoSlow", type = "A", description = "Detects moving while blocking, eating")
public class NoSlowA extends Check implements PositionCheck {

    private double motionX, motionZ;

    public NoSlowA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PositionUpdate update) {
        if(data.getActionTracker().isBlocking() || data.getEmulationTracker().isUsing()) {
            final boolean onGround = data.getPositionTracker().isOnGround();
            final boolean lastOnGround = data.getPositionTracker().isLastOnGround();

            final double deltaX = data.getPositionTracker().getDeltaX();
            final double deltaY = data.getPositionTracker().getDeltaY();
            final double deltaZ = data.getPositionTracker().getDeltaZ();

            final double deltaXZ = data.getPositionTracker().getDeltaXZ();

            float friction = 0.91F;
            if (lastOnGround) friction *= data.getPositionTracker().getSlipperiness();

            double movementSpeed = PlayerUtil.getAttributeSpeed(data, true);

            if (lastOnGround) {

                movementSpeed *= 0.16277136F / (friction * friction * friction);
                if (!onGround && deltaY >= 0.0D) {
                    movementSpeed += 0.2D;
                }
            } else {
                movementSpeed = (float) ((double) 0.02F + (double) 0.02F * 0.3D);
            }

            if (data.getVelocityTracker().getTicksSinceVelocity() == 1) {
                this.motionX = data.getVelocityTracker().getVelocity().getX();
                this.motionZ = data.getVelocityTracker().getVelocity().getZ();
            }

            final double acceleration = (deltaXZ - (MathUtil.hypot(this.motionX, this.motionZ))) / movementSpeed;
            double fallDistance = data.getPlayer().getFallDistance();

            if(data.getActionTracker().isSprinting() && deltaXZ >= 0.4 && movementSpeed >= 0.3D && fallDistance < 1) {
                this.failNoBan("speed: " + movementSpeed + " xz: " + deltaXZ);
            }
        }
    }
}
