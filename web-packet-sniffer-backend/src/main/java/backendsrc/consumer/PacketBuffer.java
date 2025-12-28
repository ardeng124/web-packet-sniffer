package backendsrc.consumer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import backendsrc.domain.PacketSummary;

public class PacketBuffer implements PacketConsumer {
    private final ArrayDeque<PacketSummary> packetBuffer;
    private int maxBufferSize = 1000;
    
    public PacketBuffer(int bufferSize) {
        packetBuffer = new ArrayDeque<>();
        maxBufferSize = bufferSize;
    }
    public PacketBuffer() {
        packetBuffer = new ArrayDeque<>();
    }
    public synchronized List<PacketSummary> getSnapshot(){
        synchronized(packetBuffer){
            return new ArrayList<>(packetBuffer); 
        }
    }
    public synchronized void clear() {
        packetBuffer.clear();
    }
    public synchronized void consumePacket(PacketSummary inPacket) {
        while (packetBuffer.size() >= maxBufferSize) {
            packetBuffer.removeFirst();
        }
        packetBuffer.addLast(inPacket);
    }
}
