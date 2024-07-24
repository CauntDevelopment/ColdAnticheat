package me.tecnio.ahm.check.impl.groundspoof;

import cc.ghast.packet.wrapper.packet.play.server.GPacketPlayServerUpdateAttributes;
import me.tecnio.ahm.check.Check;
import me.tecnio.ahm.check.api.annotations.CheckManifest;
import me.tecnio.ahm.check.type.PositionCheck;
import me.tecnio.ahm.data.PlayerData;
import me.tecnio.ahm.update.PositionUpdate;

@CheckManifest(name = "GroundSpoof", type = "A", description = "Detects ground spoofs (some of em)")
public class GroundSpoofA extends Check implements PositionCheck {

    private boolean scheduledDamage;

    public GroundSpoofA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PositionUpdate update) {
        double posY = update.getY();
        boolean grounded = update.isOnGround();

        boolean serverGrounded = data.getPositionTracker().isServerGround();

        if(grounded && !serverGrounded) {
            if(this.buffer.increase() > 2) {
                scheduledDamage = true;

                this.failNoBan("claimed to be on ground");
            }
        } else {
            if(scheduledDamage) {
                data.getPlayer().setHealth(data.getPlayer().getHealth() - 8);
                scheduledDamage = false;
            }
            this.buffer.decrease();
        }
    }
}
