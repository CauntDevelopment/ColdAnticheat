package me.tecnio.ahm.check.impl.scaffold;

import ac.artemis.packet.spigot.wrappers.GPacket;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientBlockPlace;
import me.tecnio.ahm.check.Check;
import me.tecnio.ahm.check.api.annotations.CheckManifest;
import me.tecnio.ahm.check.type.PacketCheck;
import me.tecnio.ahm.data.PlayerData;
import me.tecnio.ahm.data.tracker.impl.PositionTracker;
import me.tecnio.ahm.exempt.ExemptType;
import org.bukkit.GameMode;

@CheckManifest(name = "Scaffold", type = "C", description = "Checks if the player is going in a straight line")
public class ScaffoldC extends Check implements PacketCheck {

    public ScaffoldC(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(GPacket packet) {
        if(packet instanceof GPacketPlayClientBlockPlace) {
            GPacketPlayClientBlockPlace wrapper = (GPacketPlayClientBlockPlace) packet;

            PositionTracker positionTracker = data.getPositionTracker();
            boolean exempt = this.isExempt(ExemptType.FLIGHT, ExemptType.WALL, ExemptType.LIQUID, ExemptType.WEB) ||
                    data.getPlayer().getGameMode() != GameMode.SURVIVAL || wrapper.getPosition().getY() >= positionTracker.getY();

            if(exempt) {
                this.buffer.setBuffer(0);
                return;
            }

            wrapper.getDirection().ifPresent(enumDirection -> {
                PositionTracker tracker = data.getPositionTracker();

                double XZDiff = Math.abs(tracker.getDeltaXZ() - tracker.getLastDeltaXZ());

                if(wrapper.getPosition().getY() >= tracker.getY() || !data.isScaffolding()) {
                    return;
                }

                int toFlag = XZDiff == 0 && data.getPositionTracker().getY() == data.getPositionTracker().getLastY() ? 9 : 12;

                if(XZDiff == 0 && data.getPositionTracker().getY() == data.getPositionTracker().getLastY() || data.getPositionTracker().getY() == data.getPositionTracker().getLastLastY()) {
                    if(this.buffer.increase() > toFlag) {
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
