package org.network.devicemon.boundary;

import org.network.devicemon.model.NetworkDeviceListItem;
import org.network.devicemon.model.SignOnInformation;
import org.network.devicemon.service.DeviceService;
import org.network.devicemon.service.LeaseService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/device")
@Validated
public class DeviceBoundary {

    private final LeaseService leaseService;

    private final DeviceService deviceService;

    public DeviceBoundary(LeaseService leaseService, DeviceService deviceService) {
        this.leaseService = leaseService;
        this.deviceService = deviceService;
    }

    @GetMapping
    public List<NetworkDeviceListItem> getAllDevices() {
        return deviceService.findAll().stream().map(NetworkDeviceListItem::new).collect(Collectors.toList());
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
