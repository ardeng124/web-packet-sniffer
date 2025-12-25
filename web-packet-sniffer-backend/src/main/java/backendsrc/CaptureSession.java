package backendsrc;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.time.Instant;

public class CaptureSession {
    UUID sessionID;
    CaptureState state;
    PacketBuffer consumer;
    Instant startTime;
    Instant stopTime;
    public CaptureSession() {
        sessionID = UUID.randomUUID();
        state = CaptureState.NEW;
        consumer = new PacketBuffer();
    }

    public synchronized void beginSession() {
        if (state != CaptureState.NEW) throw new IllegalStateException("Session state invalid");
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

    public boolean isRunning() {
        return state == CaptureState.RUNNING;
    }
}



