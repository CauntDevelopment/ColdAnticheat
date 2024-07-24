package me.tecnio.ahm.check.impl.aim;

import me.tecnio.ahm.check.Check;
import me.tecnio.ahm.check.api.annotations.CheckManifest;
import me.tecnio.ahm.check.type.RotationCheck;
import me.tecnio.ahm.data.PlayerData;
import me.tecnio.ahm.update.RotationUpdate;

@CheckManifest(name = "Aim", type = "B", description = "Checks if yaw moved but pitch is still same")
public class AimB extends Check implements RotationCheck {

    public AimB(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(RotationUpdate update) {
        float currentYaw = update.getYaw();
        float lastYaw = update.getLastYaw();

        float currentPitch = update.getPitch();
        float lastPitch = update.getLastPitch();

        float deltaYaw = update.getDeltaYaw();
        float deltaPitch = update.getDeltaPitch();

        if(currentYaw != lastYaw) {
            if(deltaYaw > 2 && deltaPitch <= 0) {
                if(this.buffer.increase() > 15) {
                    this.failNoBan("deltaYaw: " + deltaYaw + " deltaPitch: " + deltaPitch);
                }
            } else {
                this.buffer.decrease();
            }
        } else {
            this.buffer.setBuffer(0);
        }
    }
}
