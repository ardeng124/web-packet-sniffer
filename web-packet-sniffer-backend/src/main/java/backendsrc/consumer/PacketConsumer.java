package backendsrc.consumer;

import backendsrc.domain.PacketSummary;

public interface PacketConsumer {
    public void consumePacket(PacketSummary inPacket);
}
