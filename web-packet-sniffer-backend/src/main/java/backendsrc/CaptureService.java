package backendsrc;

import java.util.ArrayList;
import java.util.List;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapAddress;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;

public class CaptureService {
    private CaptureSession currentSession;
    private CaptureEngine engine;
    private final int defaultBufferSize;

    public CaptureService(int bufferSize) {
        defaultBufferSize = bufferSize;
    }

    public List<PcapNetworkInterface> getNetworkInterfacesWithIP() {
        List<PcapNetworkInterface> allDevs = null;
        try {
            allDevs = Pcaps.findAllDevs();
        } catch (PcapNativeException e) {
            // throw new IOException(e.getMessage()); todo: fix
        }

        if (allDevs == null || allDevs.isEmpty()) {
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
