package org.network.devicemon.boundary;

import org.network.devicemon.model.SignOnInformation;
import org.network.devicemon.service.DeviceService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/device")
public class DeviceBoundary {

    private final DeviceService deviceService;

    public DeviceBoundary(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PutMapping("/sign-on")
    public String signOn(@RequestBody SignOnInformation signOnInformation) {
         return deviceService.signOn(signOnInformation);
    }
}
