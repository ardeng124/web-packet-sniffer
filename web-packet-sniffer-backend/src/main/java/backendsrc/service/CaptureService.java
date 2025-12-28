package backendsrc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapAddress;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;

import backendsrc.api.CaptureStatusResponse;
import backendsrc.domain.CaptureSession;
import backendsrc.domain.CaptureState;
import backendsrc.domain.PacketSummary;
import backendsrc.engine.CaptureEngine;
import backendsrc.engine.exception.CaptureEngineException;
import backendsrc.service.exception.CaptureOperationFailedException;
import backendsrc.service.exception.CaptureSessionStateInvalidException;
import backendsrc.service.exception.InterfaceNotFoundException;

public class CaptureService {
    private CaptureSession currentSession;
    private CaptureEngine engine;
    private final int defaultBufferSize;

    public CaptureService(int bufferSize) {
        defaultBufferSize = bufferSize;
        engine = new CaptureEngine();

    }

    private List<PcapNetworkInterface> getNetworkInterfacesPcap() {
        List<PcapNetworkInterface> allDevs = null;
        try {
            allDevs = Pcaps.findAllDevs();
        } catch (PcapNativeException e) {
            //TODO: Add exception handling
            // throw new IOException(e.getMessage()); todo: fix
        }

        if (allDevs == null || allDevs.isEmpty()) {
            //TODO: Add exception handling

            // throw new IOException("No NIF to capture."); todo: fix
        }
        List<PcapNetworkInterface> interfacesWithIp = new ArrayList<>();
        for (PcapNetworkInterface netInterface : allDevs) {
            for (PcapAddress inetAddress : netInterface.getAddresses()) {
                if (inetAddress != null) {
                    interfacesWithIp.add(netInterface);
                    break;
                }
            }
        }
        return interfacesWithIp;
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
        List<PcapNetworkInterface> allDevs = null;
        try {
            allDevs = Pcaps.findAllDevs();
        } catch (PcapNativeException e) {
            //TODO: Add exception handling
            // throw new IOException(e.getMessage());
        }

        if (allDevs == null || allDevs.isEmpty()) {
            //TODO: Add exception handling
            //throw new IOException("No NIF to capture.");
        }
        List<PcapNetworkInterface> interfacesWithIp = new ArrayList<>();
        for (PcapNetworkInterface netInterface : allDevs) {
            for (PcapAddress inetAddress : netInterface.getAddresses()) {
                if (inetAddress != null) {
                    interfacesWithIp.add(netInterface);
                    break;
                }
            }
        }
        List<NetworkInterfaceInfo> interfaceSummary = new ArrayList<>();
        for (PcapNetworkInterface netInterface : interfacesWithIp) {
            List<String> addresses = new ArrayList<>();
            for (PcapAddress inetAddress : netInterface.getAddresses()) {
                addresses.add(inetAddress.toString());
            }
            NetworkInterfaceInfo netInt = new NetworkInterfaceInfo(netInterface.getName(), netInterface.getDescription(), addresses);
            interfaceSummary.add(netInt);
        }
 
        return interfaceSummary;
    }

    private PcapNetworkInterface resolve (String interfaceName) {
        PcapNetworkInterface foundInterface = null;
        try {
            List<PcapNetworkInterface> allDevs = Pcaps.findAllDevs();

            for (PcapNetworkInterface nif : allDevs) {
                if (nif.getName().equals(interfaceName)) {
                    foundInterface = nif;
                    break;
                }
            }
        } catch (PcapNativeException e) {
            e.printStackTrace();
            return null;
        } 

        if (foundInterface != null) {
            return foundInterface;
        } else {
            throw new InterfaceNotFoundException(interfaceName);
        }
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

        PcapNetworkInterface iface = resolve(interfaceName);
        try {
            engine.startCapture(iface, currentSession.consumer);
        } catch (CaptureEngineException e) {
            String message = toUserFriendlyMessage(e, "start");  
            throw new CaptureOperationFailedException(message);
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

    public synchronized void clearPackets() {
        if (currentSession == null) throw new CaptureSessionStateInvalidException("Capture session not running or not initialised"); 
        currentSession.clearPackets();
    }

    public List<PacketSummary> getPacketSnapshot(){
        if (currentSession == null) return new ArrayList<PacketSummary>();
        return currentSession.getBufferSnapshot();
    }

    
}
