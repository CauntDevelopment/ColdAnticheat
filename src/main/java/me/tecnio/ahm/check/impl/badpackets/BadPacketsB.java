package me.tecnio.ahm.check.impl.badpackets;

import me.tecnio.ahm.check.Check;
import me.tecnio.ahm.check.api.annotations.CheckManifest;
import me.tecnio.ahm.check.type.RotationCheck;
import me.tecnio.ahm.data.PlayerData;
import me.tecnio.ahm.exempt.ExemptType;
import me.tecnio.ahm.update.RotationUpdate;

@CheckManifest(name = "BadPackets", type = "B", description = "Detects if the pitch is over or below 90")
public class BadPacketsB extends Check implements RotationCheck {

    public BadPacketsB(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(RotationUpdate update) {
        boolean exempt = this.isExempt(ExemptType.JOIN, ExemptType.BOAT, ExemptType.TELEPORT, ExemptType.TELEPORTED_RECENTLY);

        if(exempt) return;

        float pitch = Math.abs(update.getPitch());

        if(pitch > 90 && this.buffer.increase() > 2) {
            this.failNoBan("over 90 pitch");
            this.executeSetback(false, true);
        } else {
            this.buffer.decrease();
        }
    }
}
