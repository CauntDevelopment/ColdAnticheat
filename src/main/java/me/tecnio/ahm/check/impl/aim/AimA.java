package me.tecnio.ahm.check.impl.aim;

import me.tecnio.ahm.check.Check;
import me.tecnio.ahm.check.api.annotations.CheckManifest;
import me.tecnio.ahm.check.type.RotationCheck;
import me.tecnio.ahm.data.PlayerData;
import me.tecnio.ahm.exempt.ExemptType;
import me.tecnio.ahm.update.RotationUpdate;

@CheckManifest(name = "Aim", type = "A", description = "Detects static speed aim assists")
public class AimA extends Check implements RotationCheck {

    private float lastYawSpeed, lastPitchSpeed, oldYawDist, oldPitchDist;

    public AimA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(RotationUpdate update) {
        if(this.isExempt(ExemptType.TELEPORT, ExemptType.JOIN, ExemptType.TELEPORTED_RECENTLY, ExemptType.BOAT,
                ExemptType.VEHICLE)) {
            return;
        }

        float currentYaw = update.getYaw();
        float currentPitch = update.getPitch();

        float yawDist = Math.abs(currentYaw - update.getLastYaw());
        float pitchDist = Math.abs(currentPitch - update.getLastPitch());

        float yawSpeed = Math.abs(yawDist - oldYawDist);
        float pitchSpeed = Math.abs(pitchDist - oldPitchDist);

        if(currentYaw != update.getLastYaw()) {
            if(yawDist > 1) {
                boolean invalid = yawSpeed == 0.0D || yawDist == oldYawDist || pitchDist == oldPitchDist && pitchDist > 1;
                if(invalid && this.buffer.increase() > 6) {
                    this.failNoBan("static speed: " + yawSpeed + " " + pitchSpeed);
                }
            }
        } else {
            this.buffer.decrease();
        }

        lastYawSpeed = yawSpeed;
        lastPitchSpeed = pitchSpeed;

        oldYawDist = yawDist;
        oldPitchDist = pitchDist;
    }
}
