package backendsrc;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.pcap4j.core.PcapAddress;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.util.NifSelector;

public class backendsrc 
{  
    static List<PcapNetworkInterface> getNetworkInterfacesWithIP() {
        List<PcapNetworkInterface> allDevs = null;
        try {
            allDevs = Pcaps.findAllDevs();
        } catch (PcapNativeException e) {
            //throw new IOException(e.getMessage());
        }

        if (allDevs == null || allDevs.isEmpty()) {
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
        return interfacesWithIp;
    }

    public static void main(String[] args) {
        List<PcapNetworkInterface> allDevs = getNetworkInterfacesWithIP();
        for (PcapNetworkInterface netInt : allDevs) {
            System.out.println(netInt.getName() +" "+netInt.getAddresses());
        }
        
        //PcapNetworkInterface device = getNetworkDevice();    
        //System.out.println("You chose: " + device);
    }
}
