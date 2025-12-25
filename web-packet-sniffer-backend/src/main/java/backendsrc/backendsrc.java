package backendsrc;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;

import backendsrc.domain.PacketSummary;
import backendsrc.service.CaptureService;

public class backendsrc 
{ 
    public static void main(String[] args) {
        CaptureService mainService = new CaptureService(100);

        Scanner scanner = new Scanner(System.in);
        List<PcapNetworkInterface> allDevs = mainService.getNetworkInterfacesWithIP();
        for (PcapNetworkInterface netInt : allDevs) {
            System.out.println(netInt.getName() +" "+netInt.getAddresses());
        }
        String line = scanner.nextLine();

        mainService.startCapture(line);
        scanner.nextLine();

        List<PacketSummary> packets = mainService.getPacketSnapshot();
        packets.forEach((p)-> {
            System.out.println(p);
        });

        scanner.nextLine();
        mainService.stopCapture();
    
    }
}
