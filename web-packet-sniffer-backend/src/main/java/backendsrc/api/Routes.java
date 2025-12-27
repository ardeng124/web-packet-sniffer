package backendsrc.api;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import backendsrc.service.CaptureService;
import backendsrc.service.NetworkInterfaceInfo;

@RestController
public class Routes {
    CaptureService service = new CaptureService(1000);
    @GetMapping("/api/hello")
    public String hello() {
        return "Hello from Spring Boot!";
    }

    @GetMapping(value = "/api/interfaces", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<NetworkInterfaceInfo> interfaces() {
        List<NetworkInterfaceInfo> interfaces = service.getNetworkInterfaces();
        return interfaces;
    }

    @GetMapping(value = "/api/capture/start", produces = MediaType.APPLICATION_JSON_VALUE)
    public CaptureStatusResponse startCapture(String interfaceName) {
        service.startCapture(interfaceName);
        CaptureStatusResponse res = new CaptureStatusResponse(service.getSessionID(),service.getSessionState());
        return res;
    } 

}