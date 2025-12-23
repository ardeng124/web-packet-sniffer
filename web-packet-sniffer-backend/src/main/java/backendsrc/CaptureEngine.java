package backendsrc;

import java.util.ArrayList;
import java.util.List;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapAddress;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.Packet;

public class CaptureEngine {
    public PcapNetworkInterface selectedInterface;
    private int snapshotLength = 65536; // bytes
    private int readTimeout = 50; // milliseconds
    private PcapHandle captureHandle;
    public Thread captureThread;
    volatile Boolean running = false;

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

    public void startCapture(PcapNetworkInterface selectedInterface) throws PcapNativeException {
        if (!running) {
            captureHandle = selectedInterface.openLive(snapshotLength, PromiscuousMode.PROMISCUOUS, readTimeout);
            running = true;
            captureThread = new Thread(() -> {
                PacketListener listener = new PacketListener() {
                    @Override
                    public void gotPacket(Packet packet) {
                        // Override the default gotPacket() function and process packet
                        System.out.println(captureHandle.getTimestamp());
                        //System.out.println(packet);
                    }
                };
                // int maxPackets = 300;
                try {
                    captureHandle.loop(-1, listener);
                } catch (PcapNativeException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NotOpenException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    //this is when the handle gets stopped
                    e.printStackTrace();
                } finally {
                    running = false;
                }
            });
            captureThread.start();

        }
    }

    public void stopCapture() throws NotOpenException {
        if (running && captureHandle != null) {
            captureHandle.breakLoop();
            captureHandle.close();
            running=false;
        }
    }
}
