package me.tecnio.ahm.check.impl.reach;

import ac.artemis.packet.spigot.wrappers.GPacket;
import ac.artemis.packet.wrapper.client.PacketPlayClientUseEntity;
import cc.ghast.packet.wrapper.bukkit.Vector3D;
import cc.ghast.packet.wrapper.mc.PlayerEnums;
import cc.ghast.packet.wrapper.packet.play.client.*;
import me.tecnio.ahm.check.Check;
import me.tecnio.ahm.check.api.annotations.CheckManifest;
import me.tecnio.ahm.check.type.PacketCheck;
import me.tecnio.ahm.data.PlayerData;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

@CheckManifest(name = "Reach", type = "A", description = "The best reach check no cap.")
public class ReachA extends Check implements PacketCheck {

    private boolean receivedTransaction;

    private long lastPosition;


    public ReachA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(GPacket packet) {
        if(packet instanceof GPacketPlayClientUseEntity) {
            GPacketPlayClientUseEntity wrapper = (GPacketPlayClientUseEntity) packet;

            if(wrapper.getType() == PlayerEnums.UseType.ATTACK) {
                Entity attacked = wrapper.getEntity();

                if(attacked == packet.getPlayer()) return;

                Location attackedLoc = attacked.getLocation();
                Location playerLoc = packet.getPlayer().getLocation();

                double distance = getDistanceBetween(new Vector3D((float) playerLoc.getX(),
                                (float) playerLoc.getY(),
                                (float)playerLoc.getZ()),
                                new Vector3D((float) attackedLoc.getX(), (float) attackedLoc.getY(), (float) attackedLoc.getZ()));

                if(distance > 3.4 && receivedTransaction) {
                    this.failNoBan("range: " + distance);
                }
            }
        } else if(packet instanceof GPacketPlayClientTransaction) {
            receivedTransaction = true;
        } else if(packet instanceof GPacketPlayClientFlying) {
            receivedTransaction = false;
        } else if(packet instanceof GPacketPlayClientPosition) {
            lastPosition = System.currentTimeMillis();
        }
    }

    public double getDistanceBetween(Vector3D from, Vector3D to) {
        double xDiff = Math.abs(to.getX() - from.getX());
        double yDiff = Math.abs(to.getY() - from.getY());
        double zDiff = Math.abs(to.getZ() - from.getZ());

        return Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
    }
}
