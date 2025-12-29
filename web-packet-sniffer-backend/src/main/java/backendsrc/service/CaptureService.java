package backendsrc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import backendsrc.api.CaptureStatusResponse;
import backendsrc.domain.CaptureSession;
import backendsrc.domain.CaptureState;
import backendsrc.domain.PacketSummary;
import backendsrc.engine.CaptureEngine;
import backendsrc.engine.exception.CaptureEngineException;
import backendsrc.engine.exception.InterfaceNotFoundException;
import backendsrc.service.exception.CaptureOperationFailedException;
import backendsrc.service.exception.CaptureSessionStateInvalidException;
import backendsrc.service.exception.InterfaceEnumerationFailedException;

public class CaptureService {
    private CaptureSession currentSession;
    private CaptureEngine engine;
    private final int defaultBufferSize;

    public CaptureService(int bufferSize) {
        defaultBufferSize = bufferSize;
        engine = new CaptureEngine();

    }
 
    public UUID getSessionID(){
        return currentSession.sessionID;
    }

    public synchronized CaptureState getSessionState(){
        return currentSession.state;
    }

    public synchronized CaptureStatusResponse currentStatus() {
        if (currentSession == null) {
            return new CaptureStatusResponse(null, null, "Session not initialised");
        } else {
            String statusMessage;
            
            switch (currentSession.state) {
                case RUNNING:
                    statusMessage = "running";
                    break;
                case STOPPED:
                    statusMessage = "session stopped";
                    break;
                case NEW:
                    statusMessage = "starting";
                    break;
                default:
                    statusMessage = "unknown";
            }
            
            return new CaptureStatusResponse(getSessionID(), getSessionState(), statusMessage);
        }
    }

    public List<NetworkInterfaceInfo> getNetworkInterfaces() {
        List<NetworkInterfaceInfo> interfaceSummary = new ArrayList<>();
        try {
        interfaceSummary = engine.getNetworkInterfaces();
        } catch (CaptureEngineException e) {
            throw new InterfaceEnumerationFailedException("No interfaces found / Unable to enumerate interfaces");
        }
        return interfaceSummary;
    }

    public String toUserFriendlyMessage(CaptureEngineException e, String operation) {
        String cause = e.getMessage();
        cause = cause.toLowerCase();

        if (cause.contains("permission denied") || cause.contains("don't have permission")) {
            return "Permission denied: the application does not have enough privileges to capture packets. " + "Try running with the required capabilities or as an administrator.";
        }
        if (cause.contains("no such device") || cause.contains("no such interface") || cause.contains("interface missing")) {
            return "The selected network interface could not be found. " + "Please check the interface name and try again.";
        }   
        if (cause.contains("not open")){
            return "Capture handle is not open. The capture session may have stopped or failed to start.";
        }
        return "Unknown capture error. Failed to "+operation+" capture";
    }

    public synchronized void startCapture(String interfaceName) {
        if (currentSession != null
                && currentSession.state == CaptureState.RUNNING) {
            throw new CaptureSessionStateInvalidException("Capture session already running");
        }        currentSession = new CaptureSession(defaultBufferSize);
        try {
            engine.startCapture(interfaceName, currentSession.consumer);
        } catch (CaptureEngineException e) {
            String message = toUserFriendlyMessage(e, "start");  
            throw new CaptureOperationFailedException(message);
        } catch (InterfaceNotFoundException e) {
            throw new CaptureOperationFailedException("Capture failed to start: "+e.getMessage());
        }
        currentSession.beginSession();
    }

    public synchronized void stopCapture() {
        if (currentSession == null
                || currentSession.state != CaptureState.RUNNING) {
            throw new CaptureSessionStateInvalidException("Capture session not running or not initialised");
        }  
        try {
            engine.stopCapture();
        } catch (CaptureEngineException e) {
            String message = toUserFriendlyMessage(e, "stop");  
            throw new CaptureOperationFailedException(message);
        }
        currentSession.stopSession();
    }

    public synchronized void pauseCapture() {
        if (currentSession == null
                || currentSession.state != CaptureState.RUNNING) {
            throw new CaptureSessionStateInvalidException("Capture session not running or not initialised");
        }  
        currentSession.pauseSession();
    }

    public synchronized void resumeCapture() {
        if (currentSession == null
                || currentSession.state != CaptureState.PAUSED) {
            throw new CaptureSessionStateInvalidException("Capture session state invalid: Already running or not initialised");
        }  
        currentSession.resumeSession();
    }

    public synchronized void clearPackets() {
        if (currentSession == null) throw new CaptureSessionStateInvalidException("Capture session not running or not initialised"); 
        currentSession.clearPackets();
    }

    public List<PacketSummary> getPacketSnapshot(){
        if (currentSession == null) return new ArrayList<PacketSummary>();
        return currentSession.getBufferSnapshot();
    }

    
}
