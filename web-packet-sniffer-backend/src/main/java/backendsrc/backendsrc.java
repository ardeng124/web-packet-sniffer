package backendsrc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.pcap4j.core.PcapAddress;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;

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
        CaptureEngine captureEngine = new CaptureEngine();
        List<PcapNetworkInterface> allDevs = captureEngine.getNetworkInterfacesWithIP();
        for (PcapNetworkInterface netInt : allDevs) {
            System.out.println(netInt.getName() +" "+netInt.getAddresses());
        }
        try {
            captureEngine.startCapture(allDevs.get(0));
        } catch (PcapNativeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //PcapNetworkInterface device = getNetworkDevice();    
        //System.out.println("You chose: " + device);
    }
}
