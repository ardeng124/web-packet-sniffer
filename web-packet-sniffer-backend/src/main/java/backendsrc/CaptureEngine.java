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
import org.pcap4j.packet.IpV4Packet.IpV4Header;
import org.pcap4j.packet.Packet.Header;
import org.pcap4j.packet.TcpPacket.TcpHeader;
import org.pcap4j.packet.UdpPacket.UdpHeader;
import org.pcap4j.util.ByteArrays;
import org.pcap4j.packet.IpV6Packet.IpV6Header;
import org.pcap4j.packet.*;

import java.net.Inet4Address;
import java.sql.Timestamp;
import java.time.LocalDateTime;


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
    
    public PacketSummary extractPacketSummary(Packet packet, PcapHandle captureHandle) {
        Timestamp timestamp = captureHandle.getTimestamp();
        Packet ipPacket = null;
        Packet ethernetPacket = packet.get(EthernetPacket.class);
        if (ethernetPacket != null) {
            ipPacket = ethernetPacket.getPayload().get(IpV4Packet.class);
        } else {
            ipPacket = packet.get(IpV4Packet.class);
        }
        Inet4Address srcAddress = null;
        Inet4Address dstAddress = null;
        if (ipPacket instanceof IpV4Packet){
            IpV4Packet ipv4 = (IpV4Packet) ipPacket;
            IpV4Header ipHeader = ipv4.getHeader();
            srcAddress = ipHeader.getSrcAddr();
            dstAddress = ipHeader.getDstAddr();
        } else if (ipPacket instanceof IpV6Packet){
            // TODO: implement ipv6 support
            // IpV6Packet ipv4 = (IpV6Packet) ipPacket;
            // IpV6Header ipHeader = ipv4.getHeader();
            // srcAddress = ipHeader.getSrcAddr();
            // dstAddress = ipHeader.getDstAddr();
        }
        TcpPacket tcpPacket = packet.get(TcpPacket.class);
        UdpPacket udpPacket = packet.get(UdpPacket.class);
        String protocol = "OTHER";
        int srcPort = 0, dstPort = 0;
        if (tcpPacket != null) {
            protocol = "TCP";
            TcpHeader tcpHeader = tcpPacket.getHeader();
            srcPort = tcpHeader.getSrcPort().valueAsInt();
            dstPort = tcpHeader.getDstPort().valueAsInt();
        } else if (udpPacket != null) {
            protocol = "UDP";
            UdpHeader udpHeader = udpPacket.getHeader();
            srcPort = udpHeader.getSrcPort().valueAsInt();
            dstPort = udpHeader.getDstPort().valueAsInt();
        }
    return new PacketSummary(
            timestamp, 
            protocol, 
            srcAddress, 
            srcPort, 
            dstAddress, 
            packet.length(), 
            dstPort
        );
    }

    public void startCapture(PcapNetworkInterface selectedInterface, PacketConsumer consumer) throws PcapNativeException {
        if (!running) {
            captureHandle = selectedInterface.openLive(snapshotLength, PromiscuousMode.PROMISCUOUS, readTimeout);
            running = true;

            captureThread = new Thread(() -> {
                PacketListener listener = new PacketListener() {
                    @Override
                    public void gotPacket(Packet packet) {
                        PacketSummary packetSummary = extractPacketSummary(packet, captureHandle);
                        consumer.consumePacket(packetSummary);
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
