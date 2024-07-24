package me.tecnio.ahm.check.impl.noslow;

import ac.artemis.packet.spigot.wrappers.GPacket;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientPosition;
import me.tecnio.ahm.check.Check;
import me.tecnio.ahm.check.api.annotations.CheckManifest;
import me.tecnio.ahm.check.type.PacketCheck;
import me.tecnio.ahm.check.type.PositionCheck;
import me.tecnio.ahm.data.PlayerData;
import me.tecnio.ahm.exempt.ExemptType;
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
        boolean exempt = isExempt(ExemptType.TELEPORT, ExemptType.CHUNK, ExemptType.SLIME,
                ExemptType.LIQUID, ExemptType.BOAT, ExemptType.VEHICLE, ExemptType.EXPLOSION,
                ExemptType.JOIN);

        if(exempt) return;

        if(data.getActionTracker().isBlocking() || data.getEmulationTracker().isUsing()) {
            double fallDistance = data.getPlayer().getFallDistance();

            if(data.getActionTracker().isSprinting() && fallDistance < 1) {
                if(this.buffer.increase() > 3) {
                    this.failNoBan("");
                    this.executeSetback(true, false);
                }
            } else {
                this.buffer.decrease();
            }
        }
    }
}
