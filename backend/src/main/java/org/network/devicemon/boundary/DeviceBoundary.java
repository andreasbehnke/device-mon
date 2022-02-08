package org.network.devicemon.boundary;

import org.network.devicemon.model.SignOnInformation;
import org.network.devicemon.service.DeviceService;
import org.network.devicemon.service.LeaseService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/device")
@Validated
public class DeviceBoundary {

    private final LeaseService leaseService;

    public DeviceBoundary(LeaseService leaseService) {
        this.leaseService = leaseService;
    }

    @PutMapping("/sign-on")
    public String signOn(@Valid @RequestBody SignOnInformation signOnInformation) {
         return leaseService.startLease(signOnInformation);
    }

    @PutMapping("/sign-off/{macAddress}")
    public void signOff(@NotNull @PathVariable(name = "macAddress") String macAddress) {
        leaseService.endLease(macAddress);
    }
}
