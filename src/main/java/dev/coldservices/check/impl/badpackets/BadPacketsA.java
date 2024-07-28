package dev.coldservices.check.impl.badpackets;

import ac.artemis.packet.spigot.wrappers.GPacket;
import ac.artemis.packet.wrapper.client.PacketPlayClientUseEntity;
import cc.ghast.packet.wrapper.mc.PlayerEnums;
import cc.ghast.packet.wrapper.packet.play.client.*;
import dev.coldservices.check.Check;
import dev.coldservices.check.api.annotations.CheckManifest;
import dev.coldservices.check.type.PacketCheck;
import dev.coldservices.data.PlayerData;
import dev.coldservices.exempt.ExemptType;

@CheckManifest(name = "BadPackets", type = "A", description = "Blocking while attacking / autoblock")
public class BadPacketsA extends Check implements PacketCheck {

    public BadPacketsA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(GPacket packet) {
        if(packet instanceof GPacketPlayClientUseEntity) {
            GPacketPlayClientUseEntity wrapper = (GPacketPlayClientUseEntity) packet;

            if(wrapper.getType() == PlayerEnums.UseType.ATTACK && data.getActionTracker().isBlocking()) {
                this.failNoBan("");
                data.getPlayer().setHealth(data.getPlayer().getHealth() - 2);
            }
        }
    }
}
