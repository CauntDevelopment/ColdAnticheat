package dev.coldservices.processor.prechecks;

import ac.artemis.packet.spigot.wrappers.GPacket;

public interface PreProcessorCheck {
    boolean handle(final GPacket packet);
}
