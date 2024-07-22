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
import me.tecnio.ahm.data.tracker.impl.ConnectionTracker;
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
                Location playerLoc = data.getPositionTracker().getLocation();

                double distance = getDistanceBetween(
                        new Vector3D((float) playerLoc.getX(), (float) playerLoc.getY(), (float)playerLoc.getZ()),
                        new Vector3D((float) attackedLoc.getX(), (float) attackedLoc.getY(), (float) attackedLoc.getZ())
                );

                if(distance > (calculateReachWithPing() + 0.03)) {
                    this.failNoBan("range: " + distance);
                }
            }
        } else if(packet instanceof GPacketPlayClientPosition) {
            lastPosition = System.currentTimeMillis();
        }
    }

    public double getDistanceBetween(Vector3D from, Vector3D to) {
        double xDiff = Math.abs(from.getX() - to.getX());
        double yDiff = Math.abs(from.getY() - to.getY());
        double zDiff = Math.abs(from.getZ() - to.getZ());

        return Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff) - 1;
    }

    private double calculateReachWithPing() {
        double ping = data.getConnectionTracker().getKeepAlivePing();

        double baseDistance = 3.0;
        double extraDistance = (10 / ping) * 0.1;

        return baseDistance + extraDistance;
    }

}
