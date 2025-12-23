package backendsrc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;

public class backendsrc 
{ 
    public static void main(String[] args) {
        CaptureEngine captureEngine = new CaptureEngine();
        Scanner scanner = new Scanner(System.in);
        List<PcapNetworkInterface> allDevs = captureEngine.getNetworkInterfacesWithIP();
        int i = 0;
        for (PcapNetworkInterface netInt : allDevs) {
            System.out.println(i+":   "+netInt.getName() +" "+netInt.getAddresses());
            i = i+1;
        }
        String line = scanner.nextLine();
        int index = Integer.parseInt(line);
        try {
            captureEngine.startCapture(allDevs.get(index));
            scanner.nextLine();
            captureEngine.stopCapture();
            scanner.close();
        } catch (PcapNativeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //PcapNetworkInterface device = getNetworkDevice();    
        //System.out.println("You chose: " + device);
        catch (NotOpenException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
        }
    }
}
