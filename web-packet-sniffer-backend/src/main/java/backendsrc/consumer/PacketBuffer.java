package backendsrc.consumer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import backendsrc.domain.CaptureSession;
import backendsrc.domain.CaptureState;
import backendsrc.domain.PacketSummary;

public class PacketBuffer implements PacketConsumer {
    private final ArrayDeque<PacketSummary> packetBuffer;
    private int maxBufferSize = 1000;
    private final CaptureSession sessionOwner;
    
    public PacketBuffer(int bufferSize, CaptureSession session) {
        packetBuffer = new ArrayDeque<>();
        maxBufferSize = bufferSize;
        sessionOwner = session;
    }
    public PacketBuffer(CaptureSession session) {
        packetBuffer = new ArrayDeque<>();
        sessionOwner = session;
    }
    public synchronized List<PacketSummary> getSnapshot(){
        return new ArrayList<>(packetBuffer); 
    }
    public synchronized void clear() {
        packetBuffer.clear();
    }
    public synchronized void consumePacket(PacketSummary inPacket) {
        if (sessionOwner.state == CaptureState.PAUSED) return;
        while (packetBuffer.size() >= maxBufferSize) {
            packetBuffer.removeFirst();
        }
        packetBuffer.addLast(inPacket);
    }
}
