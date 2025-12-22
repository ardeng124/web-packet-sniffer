package backendsrc;

import java.io.IOException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.util.NifSelector;

public class App 
{  
    static PcapNetworkInterface getNetworkDevice() {
        PcapNetworkInterface device = null;
        try {
            device = new NifSelector().selectNetworkInterface();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return device;
    }

    public static void main(String[] args) {
        PcapNetworkInterface device = getNetworkDevice();
        System.out.println("You chose: " + device);
    }
}
