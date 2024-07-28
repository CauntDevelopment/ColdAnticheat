package me.tecnio.ahm.check.impl.scaffold;

import ac.artemis.packet.spigot.wrappers.GPacket;
import cc.ghast.packet.wrapper.bukkit.BlockPosition;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientBlockPlace;
import me.tecnio.ahm.check.Check;
import me.tecnio.ahm.check.api.annotations.CheckManifest;
import me.tecnio.ahm.check.type.PacketCheck;
import me.tecnio.ahm.data.PlayerData;
import me.tecnio.ahm.data.tracker.impl.PositionTracker;
import me.tecnio.ahm.exempt.ExemptType;
import net.minecraft.server.v1_8_R3.Blocks;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;

@CheckManifest(name = "Scaffold", type = "B", description = "Detects expand scaffolds")
public class ScaffoldB extends Check implements PacketCheck {

    public ScaffoldB(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(GPacket packet) {
        if(packet instanceof GPacketPlayClientBlockPlace) {
            GPacketPlayClientBlockPlace wrapper = (GPacketPlayClientBlockPlace) packet;

            PositionTracker positionTracker = data.getPositionTracker();

            boolean exempt = this.isExempt(ExemptType.FLIGHT) || data.getPlayer().getGameMode() != GameMode.SURVIVAL || wrapper.getPosition().getY() >= positionTracker.getY();

            if(exempt) {
                this.buffer.setBuffer(0);
                return;
            }

            wrapper.getDirection().ifPresent(direction -> {
                BlockPosition pos = wrapper.getPosition();
                World world = packet.getPlayer().getWorld();

                if(world.getBlockAt(new Location(world, pos.getX(), pos.getY(), pos.getZ())).equals(Blocks.AIR)) {
                    this.buffer.setBuffer(0);
                    return;
                }

                double distance = getDistance(pos);

                if(distance >= 5) {
                    if(this.buffer.increase() > 2) {
                        this.failNoBan("dist: " + distance);
                    }
                } else {
                    this.buffer.setBuffer(0);
                }
            });

        }
    }

    public double getDistance(BlockPosition position) {
        double x = Math.abs(position.getX() - data.getPositionTracker().getX());
        double y = Math.abs(position.getY() - data.getPositionTracker().getY());
        double z = Math.abs(position.getZ() - data.getPositionTracker().getZ());

        return Math.sqrt(x * x + y * y + z * z);
    }
}
