package me.tecnio.ahm.check.impl.autoclicker;

import ac.artemis.packet.spigot.wrappers.GPacket;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientArmAnimation;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientFlying;
import me.tecnio.ahm.check.Check;
import me.tecnio.ahm.check.api.annotations.CheckManifest;
import me.tecnio.ahm.check.type.PacketCheck;
import me.tecnio.ahm.data.PlayerData;

@CheckManifest(name = "AutoClicker", type = "A", description = "Detects cps over 20")
public class AutoClickerA extends Check implements PacketCheck {

    private int cps, flying;

    public AutoClickerA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(GPacket packet) {
        if(packet instanceof GPacketPlayClientArmAnimation) {
            cps++;
        }

        if(packet instanceof GPacketPlayClientFlying) {
            if(++flying > 20) {
                if(cps > 20) {
                    this.failNoBan("cps: " + cps);
                }

                flying = cps = 0;
            }
        }
    }
}
