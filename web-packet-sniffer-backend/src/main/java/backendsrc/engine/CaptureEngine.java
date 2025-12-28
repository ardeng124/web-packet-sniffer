package backendsrc.engine;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.packet.IpV4Packet.IpV4Header;
import org.pcap4j.packet.TcpPacket.TcpHeader;
import org.pcap4j.packet.UdpPacket.UdpHeader;

import backendsrc.consumer.PacketConsumer;
import backendsrc.domain.PacketSummary;
import backendsrc.engine.exception.CaptureEngineException;

import org.pcap4j.packet.*;

import java.net.Inet4Address;
import java.sql.Timestamp;


public class CaptureEngine {
    public PcapNetworkInterface selectedInterface;
    private int snapshotLength = 65536; // bytes
    private int readTimeout = 50; // milliseconds
    private PcapHandle captureHandle;
    public Thread captureThread;
    private volatile Boolean running = false;

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

    public void startCapture(PcapNetworkInterface selectedInterface, PacketConsumer consumer) throws CaptureEngineException {
        if (!running) {
            try {
                captureHandle = selectedInterface.openLive(snapshotLength, PromiscuousMode.PROMISCUOUS, readTimeout);
            } catch (PcapNativeException e) {
                throw new CaptureEngineException(e.getMessage(), e);
            }
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
                } catch (PcapNativeException | NotOpenException e) {
                    running = false;
                    throw new CaptureEngineException(e.getMessage(), e);
                } catch (InterruptedException e) {
                    //this is when the handle gets stopped
                    Thread.currentThread().interrupt();
                    //e.printStackTrace();
                } finally {
                    running = false;
                }
            });
            captureThread.start();

        }
    }

    public void stopCapture() throws CaptureEngineException {
        if (running && captureHandle != null) {
            try {
                captureHandle.breakLoop();
            } catch (NotOpenException e) {
                throw new CaptureEngineException(e.getMessage(), e);
            }
            captureHandle.close();
            running=false;
        }
    }
}
