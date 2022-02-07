package org.network.devicemon.boundary;

import org.network.devicemon.model.SignOnInformation;
import org.network.devicemon.service.DeviceService;
import org.network.devicemon.service.LeaseService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/device")
public class DeviceBoundary {

    private final DeviceService deviceService;

    private final LeaseService leaseService;

    public DeviceBoundary(DeviceService deviceService, LeaseService leaseService) {
        this.deviceService = deviceService;
        this.leaseService = leaseService;
    }

    @PutMapping("/sign-on")
    public String signOn(@RequestBody SignOnInformation signOnInformation) {
         return deviceService.signOn(signOnInformation);
    }

    @PutMapping("/sign-off/{macAddress}")
    public void signOff(@PathVariable(name = "macAddress") String macAddress) {
        leaseService.endLease(macAddress);
    }
}
