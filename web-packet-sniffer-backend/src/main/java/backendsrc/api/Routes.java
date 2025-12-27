package backendsrc.api;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import backendsrc.service.CaptureService;
import backendsrc.service.NetworkInterfaceInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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

   @PostMapping(value = "/api/capture/start", consumes = "text/plain")
    public CaptureStatusResponse startCapture(@RequestBody String body) {
        String interfaceName = body;
        service.startCapture(interfaceName);
        CaptureStatusResponse res = new CaptureStatusResponse(service.getSessionID(),service.getSessionState(), "Capture started successfully");
        return res;
    }

    @PostMapping(value = "/api/capture/stop")
    public CaptureStatusResponse stopCapture() {
        service.stopCapture();
        CaptureStatusResponse res = new CaptureStatusResponse(service.getSessionID(),service.getSessionState(), "Capture stopped successfully");
        return res;
    }

}