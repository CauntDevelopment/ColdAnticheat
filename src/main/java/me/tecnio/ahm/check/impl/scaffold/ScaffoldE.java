package me.tecnio.ahm.check.impl.scaffold;

import ac.artemis.packet.spigot.wrappers.GPacket;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientBlockPlace;
import me.tecnio.ahm.check.Check;
import me.tecnio.ahm.check.api.annotations.CheckManifest;
import me.tecnio.ahm.check.type.PacketCheck;
import me.tecnio.ahm.data.PlayerData;
import me.tecnio.ahm.data.tracker.impl.ActionTracker;
import me.tecnio.ahm.data.tracker.impl.PositionTracker;
import me.tecnio.ahm.exempt.ExemptType;
import org.bukkit.GameMode;

@CheckManifest(name = "Scaffold", type = "E", description = "Detects keep-Y Scaffolds")
public class ScaffoldE extends Check implements PacketCheck {

    private int lastBlockY;

    public ScaffoldE(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(GPacket packet) {
        if(packet instanceof GPacketPlayClientBlockPlace) {
            GPacketPlayClientBlockPlace wrapper = (GPacketPlayClientBlockPlace) packet;

            PositionTracker tracker = data.getPositionTracker();
            ActionTracker actionTracker = data.getActionTracker();

            boolean exempt = this.isExempt(ExemptType.WALL, ExemptType.FLIGHT, ExemptType.WEB) || data.getPlayer().getGameMode() == GameMode.CREATIVE
                    || wrapper.getPosition().getY() >= tracker.getY() || !data.isScaffolding();

            if(exempt) return;

            wrapper.getDirection().ifPresent(enumDirection -> {
                if(Math.abs(wrapper.getPosition().getY() - lastBlockY) == 0 && !data.getPlayer().isOnGround() && data.getPlayer().isSprinting()) {
                    if(this.buffer.increase() > 4) {
                        this.failNoBan("");
                        this.executeSetbackToPosition(tracker.getLastX(), wrapper.getPosition().getY(), tracker.getLastZ());
                    }
                } else {
                    this.buffer.decrease();
                }
            });

            lastBlockY = wrapper.getPosition().getY();
        }
    }
}
