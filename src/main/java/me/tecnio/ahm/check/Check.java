package me.tecnio.ahm.check;

import ac.artemis.packet.wrapper.server.PacketPlayServerPosition;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientPositionLook;
import cc.ghast.packet.wrapper.packet.play.server.GPacketPlayServerEntityTeleport;
import cc.ghast.packet.wrapper.packet.play.server.GPacketPlayServerPosition;
import lombok.Getter;
import lombok.Setter;
import me.tecnio.ahm.AHM;
import me.tecnio.ahm.alert.AlertManager;
import me.tecnio.ahm.check.api.Buffer;
import me.tecnio.ahm.check.api.annotations.CheckManifest;
import me.tecnio.ahm.check.api.enums.CheckState;
import me.tecnio.ahm.config.ConfigManager;
import me.tecnio.ahm.data.PlayerData;
import me.tecnio.ahm.data.tracker.impl.PositionTracker;
import me.tecnio.ahm.exempt.ExemptType;
import me.tecnio.ahm.util.string.ChatUtil;
import org.atteo.classindex.IndexSubclasses;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

@Getter
@IndexSubclasses
public abstract class Check {

    protected final PlayerData data;
    protected final Player player;

    private final CheckManifest info;

    private final String name, type, description;
    private final CheckState state;
    private final int decay;

    protected Buffer buffer;

    private final boolean enabled, punishing;
    private final List<String> punishments;
    private final int maxVl;

    @Setter
    private int violations;

    public Check(final PlayerData data) {
        this.data = data;
        this.player = data.getPlayer();

        if (this.getClass().isAnnotationPresent(CheckManifest.class)) {
            this.info = this.getClass().getDeclaredAnnotation(CheckManifest.class);

            this.name = this.info.name();
            this.type = this.info.type();
            this.description = this.info.description();

            this.state = this.info.state();

            this.decay = this.info.decay();

            this.buffer = new Buffer(this.info.maxBuffer());
        } else {
            this.info = null;
            throw new IllegalStateException("The CheckManifest annotation has not been added on " + this.getClass().getName());
        }

        final String name = this.name + "." + this.type;
        final ConfigManager config = AHM.get(ConfigManager.class);

        this.enabled = config.getEnabledMap().get(name);
        this.punishing = config.getPunishMap().get(name);
        this.punishments = config.getCommandsMap().get(name);
        this.maxVl = config.getMaxViolationsMap().get(name);
    }

    protected final void fail() {
        this.fail("No information.");
    }

    protected final void fail(final String debug) {
        ++this.violations;

        AHM.get(AlertManager.class).handleAlert(this, debug);

        if (this.violations >= this.maxVl) {
            AHM.get(AlertManager.class).handlePunishment(this);
        }
    }

    protected final void failNoBan(final String debug) {
        ++this.violations;

        AHM.get(AlertManager.class).handleAlert(this, debug);

    }

    protected final void executeSetbackToPosition(double x, double y, double z) {
        PositionTracker tracker = data.getPositionTracker();

        Location loc = new Location(data.getPlayer().getWorld(), x, y, z);

        data.getPlayer().teleport(loc);
    }

    protected final void executeSetback(boolean brokenYaw, boolean brokenPitch) {
        PositionTracker tracker = data.getPositionTracker();

        Location loc = tracker.getLastLocation();
        if(brokenYaw) {
            loc.setYaw((float) (loc.getYaw() - Math.random() * 180));
        }

        if(brokenPitch) {
            loc.setPitch((float) (0 + Math.random() * 90));
        }

        data.getPlayer().teleport(loc);
    }

    protected final void fail(final String debug, final Object... params) {
        this.fail(String.format(debug, params));
    }

    protected final boolean isExempt(final ExemptType... exemptTypes) {
        return data.getExemptTracker().isExempt(exemptTypes);
    }

    protected final void debug(final Object object, final Object... objects) {
        data.getPlayer().sendMessage(ChatUtil.translate(String.format("&6AntiHaxerman Debug &8> " + ChatColor.WHITE + object.toString(), objects)));
    }

    protected boolean canClick() {
        return !data.getActionTracker().isPlacing();
    }
}
