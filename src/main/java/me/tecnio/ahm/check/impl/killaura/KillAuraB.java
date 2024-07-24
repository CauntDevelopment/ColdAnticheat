package me.tecnio.ahm.check.impl.killaura;

import ac.artemis.packet.spigot.wrappers.GPacket;
import ac.artemis.packet.wrapper.client.PacketPlayClientFlying;
import ac.artemis.packet.wrapper.client.PacketPlayClientUseEntity;
import cc.ghast.packet.wrapper.mc.PlayerEnums;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientUseEntity;
import me.tecnio.ahm.check.Check;
import me.tecnio.ahm.check.api.annotations.CheckManifest;
import me.tecnio.ahm.check.type.PacketCheck;
import me.tecnio.ahm.check.type.PositionCheck;
import me.tecnio.ahm.data.PlayerData;
import me.tecnio.ahm.data.tracker.impl.ActionTracker;
import me.tecnio.ahm.data.tracker.impl.EmulationTracker;
import me.tecnio.ahm.data.tracker.impl.PositionTracker;

@CheckManifest(name = "KillAura", type = "B", description = "Checks if player is not slowed down after attack")
public class KillAuraB extends Check implements PacketCheck {

    public KillAuraB(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(GPacket packet) {
        if(packet instanceof PacketPlayClientFlying) {
            ActionTracker actionTracker = data.getActionTracker();
            EmulationTracker emulationTracker = data.getEmulationTracker();
            PositionTracker positionTracker = data.getPositionTracker();

            boolean attacked = actionTracker.getAttackTimer().getTick() == 1;

            if(attacked) {
                boolean isHitSlow = emulationTracker.isHitSlowdown() && !emulationTracker.isSprint() && !positionTracker.isOnGround() && !positionTracker.isLastOnGround();

                if(!isHitSlow) {
                    if(this.buffer.increase() > 2) {
                        this.failNoBan("");
                    }
                } else {
                    this.buffer.decrease();
                }
            }
        }
    }
}
