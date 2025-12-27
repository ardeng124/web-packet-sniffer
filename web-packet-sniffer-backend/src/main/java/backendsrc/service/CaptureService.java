package backendsrc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapAddress;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;

import backendsrc.domain.CaptureSession;
import backendsrc.domain.CaptureState;
import backendsrc.domain.PacketSummary;
import backendsrc.engine.CaptureEngine;

public class CaptureService {
    private CaptureSession currentSession;
    private CaptureEngine engine;
    private final int defaultBufferSize;

    public CaptureService(int bufferSize) {
        defaultBufferSize = bufferSize;
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

    public CaptureState getSessionState(){
        return currentSession.state;
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
            return null;
            //TODO: Fix handling interface not found.
            //throw an exception or something
        }
    }

    public void startCapture(String interfaceName) {
        currentSession = new CaptureSession(defaultBufferSize);
        engine = new CaptureEngine();

        PcapNetworkInterface iface = resolve(interfaceName);
        currentSession.beginSession();
        try {
            engine.startCapture(iface, currentSession.consumer);
        } catch (PcapNativeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void stopCapture() {
        currentSession.stopSession();
        try {
            engine.stopCapture();
        } catch (NotOpenException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public List<PacketSummary> getPacketSnapshot(){
        return currentSession.getBufferSnapshot();
    }

    
}
