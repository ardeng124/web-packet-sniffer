package backendsrc.domain;
import java.util.List;
import java.util.UUID;

import backendsrc.consumer.PacketBuffer;

import java.time.Instant;

public class CaptureSession {
    public UUID sessionID;
    public CaptureState state;
    public final PacketBuffer consumer;
    public Instant startTime;
    public Instant stopTime;
    public int bufferSize;
    
    public CaptureSession() {
        sessionID = UUID.randomUUID();
        state = CaptureState.NEW;
        consumer = new PacketBuffer(this);
    }

    public CaptureSession(int maxBufferSize) {
        sessionID = UUID.randomUUID();
        state = CaptureState.NEW;
        consumer = new PacketBuffer(maxBufferSize, this);
        bufferSize = maxBufferSize;
    }

    public synchronized void beginSession() {
        state = CaptureState.RUNNING;
        startTime = Instant.now();
    }

    public synchronized void pauseSession() {
        state = CaptureState.PAUSED;
    }

    public synchronized void resumeSession() {
        state = CaptureState.RUNNING;
    }

    public synchronized void stopSession() {
        state = CaptureState.STOPPED;
        stopTime = Instant.now();
    }

    public List<PacketSummary> getBufferSnapshot(){
        return consumer.getSnapshot();
    }

    public synchronized boolean isRunning() {
        return state == CaptureState.RUNNING;
    }

    public void clearPackets() {
        consumer.clear();
    }
}



