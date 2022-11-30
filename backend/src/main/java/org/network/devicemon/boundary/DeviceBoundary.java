package org.network.devicemon.boundary;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.network.devicemon.entity.NetworkDevice;
import org.network.devicemon.model.*;
import org.network.devicemon.service.DeviceService;
import org.network.devicemon.service.LeaseService;
import org.network.devicemon.service.MacVendorService;
import org.network.devicemon.validation.MacAddress;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/device")
@Validated
public class DeviceBoundary {

    private final LeaseService leaseService;

    private final DeviceService deviceService;

    private final MacVendorService macVendorService;

    private final ObjectMapper objectMapper;

    public DeviceBoundary(LeaseService leaseService, DeviceService deviceService, MacVendorService macVendorService, ObjectMapper objectMapper) {
        this.leaseService = leaseService;
        this.deviceService = deviceService;
        this.macVendorService = macVendorService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public List<NetworkDeviceListItem> getAllDevices() {
        return deviceService.findAllOrderByActivity().stream()
                .map(networkDevice -> new NetworkDeviceListItem(networkDevice, macVendorService.getVendorInformation(networkDevice.getMacAddress())))
                .collect(Collectors.toList());
    }

    @GetMapping("/backup")
    public List<NetworkDeviceBackupItem> getDevicesBackup(HttpServletResponse response) {
        response.setHeader("Content-Disposition","attachment; filename=\"hostnames_backup.json\"");
        return deviceService.findAllOrderByHostname().stream().map(NetworkDeviceBackupItem::new).collect(Collectors.toList());
    }

    @PostMapping("/restore")
    public void restoreDevicesBackup(@RequestParam MultipartFile file) throws IOException {
        List<NetworkDevice> backup = objectMapper.readValue(file.getInputStream(), new TypeReference<>(){});
        deviceService.restore(backup);
    }

    @PutMapping("/sign-on")
    public String signOn(@Valid @RequestBody SignOnInformation signOnInformation) {
         return leaseService.startLease(signOnInformation);
    }

    @PutMapping("/sign-off/{macAddress}")
    public void signOff(@NotEmpty @MacAddress @PathVariable(name = "macAddress") String macAddress) {
        leaseService.endLease(macAddress);
    }

    @PutMapping("/synchronize-leases")
    public void synchronizeLeases(@NotEmpty @RequestBody List<@Valid DhcpLease> leases) {
        leaseService.synchronizeLeases(leases);
    }

    @PutMapping("/{macAddress}/approve")
    public NetworkDeviceListItem approve(@NotEmpty @PathVariable(name = "macAddress") String macAddress, @Valid @RequestBody ApproveDevice approveDevice) {
        NetworkDevice networkDevice = deviceService.approve(macAddress, approveDevice.getHostname());
        return new NetworkDeviceListItem(networkDevice, macVendorService.getVendorInformation(networkDevice.getMacAddress()));
    }

    @DeleteMapping("/{macAddress}")
    public void forgetDevice(@NotEmpty @MacAddress @PathVariable(name = "macAddress") String macAddress) {
        deviceService.delete(macAddress);
    }

}
