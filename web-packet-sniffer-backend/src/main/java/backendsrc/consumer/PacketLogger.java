package backendsrc.consumer;

import backendsrc.domain.PacketSummary;

public class PacketLogger implements PacketConsumer{

    public void consumePacket(PacketSummary inPacket) {
        System.out.println(inPacket.toString());
    }
}
