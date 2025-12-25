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
        PacketBuffer consumer = new PacketBuffer();
        List<PcapNetworkInterface> allDevs = captureEngine.getNetworkInterfacesWithIP();
        int i = 0;
        for (PcapNetworkInterface netInt : allDevs) {
            System.out.println(i+":   "+netInt.getName() +" "+netInt.getAddresses());
            i = i+1;
        }
        String line = scanner.nextLine();
        int index = Integer.parseInt(line);
        try {
            captureEngine.startCapture(allDevs.get(index),consumer);
           
        } catch (PcapNativeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        scanner.nextLine();
        List<PacketSummary> packets = consumer.getSnapshot();
        packets.forEach((p)-> {
            System.out.println(p);
        });

        try {
            scanner.nextLine();
            captureEngine.stopCapture();
            scanner.close();
        } catch (NotOpenException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
