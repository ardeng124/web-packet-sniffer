package backendsrc.consumer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import backendsrc.domain.PacketSummary;

public class PacketBuffer implements PacketConsumer {
    private final ArrayDeque<PacketSummary> packetBuffer;
    int maxBufferSize = 1000;
    
    public PacketBuffer(int bufferSize) {
        packetBuffer = new ArrayDeque<>();
        maxBufferSize = bufferSize;
    }
    public PacketBuffer() {
        packetBuffer = new ArrayDeque<>();
    }
    public List<PacketSummary> getSnapshot(){
        synchronized(packetBuffer){
            return new ArrayList<>(packetBuffer); 
        }
    }
    public void consumePacket(PacketSummary inPacket) {
        synchronized (packetBuffer) {

            while (packetBuffer.size() >= maxBufferSize) {
                packetBuffer.removeFirst();
            }
            packetBuffer.addLast(inPacket);
        }
    }
}
