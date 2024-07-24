package me.tecnio.ahm.check.impl.scaffold;

import ac.artemis.packet.spigot.wrappers.GPacket;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientBlockPlace;
import me.tecnio.ahm.check.Check;
import me.tecnio.ahm.check.api.annotations.CheckManifest;
import me.tecnio.ahm.check.type.PacketCheck;
import me.tecnio.ahm.data.PlayerData;
import me.tecnio.ahm.data.tracker.impl.PositionTracker;

@CheckManifest(name = "Scaffold", type = "C", description = "Checks if the player is going in a straight line")
public class ScaffoldC extends Check implements PacketCheck {

    public ScaffoldC(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(GPacket packet) {
        if(packet instanceof GPacketPlayClientBlockPlace) {
            GPacketPlayClientBlockPlace wrapper = (GPacketPlayClientBlockPlace) packet;

            wrapper.getDirection().ifPresent(enumDirection -> {
                PositionTracker tracker = data.getPositionTracker();

                double XZDiff = Math.abs(tracker.getDeltaXZ() - tracker.getLastDeltaXZ());

                if(wrapper.getPosition().getY() >= tracker.getY()) {
                    return;
                }

                if(XZDiff == 0 && data.getPositionTracker().getY() == data.getPositionTracker().getLastY()) {
                    if(this.buffer.increase() > 5) {
                        this.failNoBan("deltaXZ: " + data.getPositionTracker().getDeltaXZ());
                        this.executeSetback(true, false);
                    }
                } else {
                    this.buffer.decreaseBy(0.5);
                }
            });
        }
    }
}
