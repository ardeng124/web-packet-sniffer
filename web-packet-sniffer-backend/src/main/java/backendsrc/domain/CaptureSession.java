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
        consumer = new PacketBuffer();
    }

    public CaptureSession(int maxBufferSize) {
        sessionID = UUID.randomUUID();
        state = CaptureState.NEW;
        consumer = new PacketBuffer(maxBufferSize);
        bufferSize = maxBufferSize;
    }

    public synchronized void beginSession() {
        if (state != CaptureState.NEW) throw new IllegalStateException("Session state invalid");
        state = CaptureState.RUNNING;
        startTime = Instant.now();
    }

    public synchronized void stopSession() {
        if (state != CaptureState.RUNNING) throw new IllegalStateException("Session state invalid");
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



