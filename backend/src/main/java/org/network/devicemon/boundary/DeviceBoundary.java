package org.network.devicemon.boundary;

import org.network.devicemon.entity.NetworkDevice;
import org.network.devicemon.model.ApproveDevice;
import org.network.devicemon.model.NetworkDeviceBackupItem;
import org.network.devicemon.model.NetworkDeviceListItem;
import org.network.devicemon.model.SignOnInformation;
import org.network.devicemon.service.DeviceService;
import org.network.devicemon.service.LeaseService;
import org.network.devicemon.service.MacVendorService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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

    private final MacVendorService macVendorService;

    public DeviceBoundary(LeaseService leaseService, DeviceService deviceService, MacVendorService macVendorService) {
        this.leaseService = leaseService;
        this.deviceService = deviceService;
        this.macVendorService = macVendorService;
    }

    @GetMapping
    public List<NetworkDeviceListItem> getAllDevices() {
        return deviceService.findAll().stream()
                .map(networkDevice -> new NetworkDeviceListItem(networkDevice, macVendorService.getVendorInformation(networkDevice.getMacAddress())))
                .collect(Collectors.toList());
    }

    @GetMapping("/backup")
    public List<NetworkDeviceBackupItem> getDevicesBackup(HttpServletResponse response) {
        response.setHeader("Content-Disposition","attachment; filename=\"hostnames_backup.json\"");
        return deviceService.findAllOrderByHostname().stream().map(NetworkDeviceBackupItem::new).collect(Collectors.toList());
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
        NetworkDevice networkDevice = deviceService.approve(macAddress, approveDevice.getHostname());
        return new NetworkDeviceListItem(networkDevice, macVendorService.getVendorInformation(networkDevice.getMacAddress()));
    }

    @DeleteMapping("/{macAddress}")
    public void forgetDevice(@NotEmpty @PathVariable(name = "macAddress") String macAddress) {
        deviceService.delete(macAddress);
    }

}
