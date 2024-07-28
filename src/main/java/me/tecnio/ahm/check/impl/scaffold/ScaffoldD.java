package me.tecnio.ahm.check.impl.scaffold;

import ac.artemis.packet.spigot.wrappers.GPacket;
import ac.artemis.packet.wrapper.client.PacketPlayClientArmAnimation;
import ac.artemis.packet.wrapper.client.PacketPlayClientFlying;
import cc.ghast.packet.nms.EnumDirection;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientBlockPlace;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientFlying;
import me.tecnio.ahm.check.Check;
import me.tecnio.ahm.check.api.annotations.CheckManifest;
import me.tecnio.ahm.check.type.PacketCheck;
import me.tecnio.ahm.data.PlayerData;
import me.tecnio.ahm.data.tracker.impl.PositionTracker;
import me.tecnio.ahm.exempt.ExemptType;
import org.bukkit.GameMode;

@CheckManifest(name = "Scaffold", type = "D", description = "Checks if the player only clicks once per block")
public class ScaffoldD extends Check implements PacketCheck {

    private int clicks;

    public ScaffoldD(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(GPacket packet) {
        if(packet instanceof GPacketPlayClientBlockPlace) {
            clicks = 0;

            GPacketPlayClientBlockPlace wrapper = (GPacketPlayClientBlockPlace) packet;
            PositionTracker positionTracker = data.getPositionTracker();

            boolean exempt = this.isExempt(ExemptType.FLIGHT, ExemptType.WALL, ExemptType.LIQUID, ExemptType.WEB) ||
                    data.getPlayer().getGameMode() != GameMode.SURVIVAL || wrapper.getPosition().getY() >= positionTracker.getY();

            if(exempt) {
                this.buffer.setBuffer(0);
                return;
            }

            wrapper.getDirection().ifPresent(enumDirection -> {
                if(enumDirection.equals(EnumDirection.UP) || enumDirection.equals(EnumDirection.DOWN)) return;

                if(clicks > 1 && this.buffer.increase() > 6) {
                    this.failNoBan("");
                } else {
                    this.buffer.decrease();
                }
            });
        }

        if(packet instanceof PacketPlayClientArmAnimation) {
            clicks++;
        }
    }
}
