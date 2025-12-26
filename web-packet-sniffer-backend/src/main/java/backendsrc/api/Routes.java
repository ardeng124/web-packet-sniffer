package backendsrc.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import backendsrc.service.CaptureService;
import backendsrc.service.NetworkInterfaceInfo;

@RestController
public class Routes {
    @GetMapping("/api/hello")
    public String hello() {
        return "Hello from Spring Boot!";
    }

     @GetMapping("/api/interfaces")
    public List<String> interfaces() {
        CaptureService service = new CaptureService(0);
        List<NetworkInterfaceInfo> interfaces = service.getNetworkInterfaces();
        List<String> returnedInterfaces = interfaces.stream().map(NetworkInterfaceInfo::toString).collect(Collectors.toList()); 
        return returnedInterfaces;
    }

}
