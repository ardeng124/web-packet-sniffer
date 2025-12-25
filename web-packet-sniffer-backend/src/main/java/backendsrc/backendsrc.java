package backendsrc;

import java.io.IOException;
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
        CaptureSession session = new CaptureSession();
        List<PcapNetworkInterface> allDevs = captureEngine.getNetworkInterfacesWithIP();
        int i = 0;
        for (PcapNetworkInterface netInt : allDevs) {
            System.out.println(i+":   "+netInt.getName() +" "+netInt.getAddresses());
            i = i+1;
        }
        String line = scanner.nextLine();
        int index = Integer.parseInt(line);
        try {
            session.beginSession();
            captureEngine.startCapture(allDevs.get(index),session.consumer);
           
        } catch (PcapNativeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        scanner.nextLine();
        List<PacketSummary> packets = session.getBufferSnapshot();
        packets.forEach((p)-> {
            System.out.println(p);
        });

        try {
            scanner.nextLine();
            captureEngine.stopCapture();
            session.stopSession();
            scanner.close();
        } catch (NotOpenException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
