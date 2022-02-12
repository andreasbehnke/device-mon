package org.network.devicemon.boundary;

import org.network.devicemon.model.ApproveDevice;
import org.network.devicemon.model.NetworkDeviceListItem;
import org.network.devicemon.model.SignOnInformation;
import org.network.devicemon.service.DeviceService;
import org.network.devicemon.service.LeaseService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
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
    public void signOff(@NotEmpty @PathVariable(name = "macAddress") String macAddress) {
        leaseService.endLease(macAddress);
    }

    @PutMapping("/{macAddress}/approve")
    public NetworkDeviceListItem approve(@NotEmpty @PathVariable(name = "macAddress") String macAddress, @Valid @RequestBody ApproveDevice approveDevice) {
        return new NetworkDeviceListItem(deviceService.approve(macAddress, approveDevice.getHostname()));
    }

    @DeleteMapping("/{macAddress}")
    public void forgetDevice(@NotEmpty @PathVariable(name = "macAddress") String macAddress) {
        deviceService.delete(macAddress);
    }

}
