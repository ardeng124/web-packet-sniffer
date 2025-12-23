package backendsrc;

import java.net.InetAddress;
import java.time.LocalDateTime;

public final class PacketSummary {
    public final LocalDateTime timestamp;
    public final String protocol;
    public final InetAddress sourceAddress;
    public final int port;
    public final InetAddress destinationAddress;
    public final int packetLength;
    public final int destPort;

    public PacketSummary(LocalDateTime timestamp, String protocol, InetAddress sourceAddress, int port, InetAddress destinationAddress, int packetLength, int destPort) {
        this.timestamp = timestamp;
        this.protocol = protocol;
        this.sourceAddress = sourceAddress;
        this.port = port;
        this.destinationAddress = destinationAddress;
        this.packetLength = packetLength;
        this.destPort = destPort;
    }
    
    public String toString() {
        String ret = (this.timestamp+"  protocol"+this.protocol+  "   src:"+ this.sourceAddress+ "   port:"+this.port+"   dest:"+this.destinationAddress+"   destPort:"+this.destPort);
        return ret;
    }
}
