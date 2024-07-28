package me.tecnio.ahm.check.impl.badpackets;

import ac.artemis.packet.spigot.wrappers.GPacket;
import ac.artemis.packet.wrapper.client.PacketPlayClientFlying;
import ac.artemis.packet.wrapper.client.PacketPlayClientKeepAlive;
import ac.artemis.packet.wrapper.client.PacketPlayClientTransaction;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientKeepAlive;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientTransaction;
import me.tecnio.ahm.check.Check;
import me.tecnio.ahm.check.api.annotations.CheckManifest;
import me.tecnio.ahm.check.type.PacketCheck;
import me.tecnio.ahm.data.PlayerData;

@CheckManifest(name = "BadPackets", type = "D", description = "Checks if the user is lost connection")
public class BadPacketsD extends Check implements PacketCheck {

    private long lastTransaction, lastKeepAlive;

    public BadPacketsD(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(GPacket packet) {
        if(packet instanceof PacketPlayClientTransaction) {
            lastTransaction = packet.getTimestamp();
        }

        if(packet instanceof PacketPlayClientKeepAlive) {
            lastKeepAlive = packet.getTimestamp();
        }

        if(packet instanceof PacketPlayClientFlying) {
            boolean keepAliveTimeOut = packet.getTimestamp() - lastKeepAlive >= 20000;
            boolean transactionTimeOut = packet.getTimestamp() - lastTransaction >= 20000;

            if(transactionTimeOut || keepAliveTimeOut) {
                data.getPlayer().kickPlayer("timed out. (Connection problems?)");
            }
        }
    }
}
