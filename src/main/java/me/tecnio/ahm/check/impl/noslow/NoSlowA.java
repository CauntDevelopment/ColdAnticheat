package me.tecnio.ahm.check.impl.noslow;

import ac.artemis.packet.spigot.wrappers.GPacket;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientPosition;
import me.tecnio.ahm.check.Check;
import me.tecnio.ahm.check.api.annotations.CheckManifest;
import me.tecnio.ahm.check.type.PacketCheck;
import me.tecnio.ahm.data.PlayerData;

@CheckManifest(name = "NoSlow", type = "A", description = "Detects moving while blocking, eating")
public class NoSlowA extends Check implements PacketCheck {
    public NoSlowA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(GPacket packet) {
        if(packet instanceof GPacketPlayClientPosition) {
            /*if(!data.getEmulationTracker().isSprint()) return;

            if(data.getActionTracker().isBlocking() || data.getEmulationTracker().isUsing()) {
                if(this.buffer.increase() > 3) {
                    this.failNoBan("");
                }
            } else {
                this.buffer.setBuffer(0);
            }*/
        }
    }
}
