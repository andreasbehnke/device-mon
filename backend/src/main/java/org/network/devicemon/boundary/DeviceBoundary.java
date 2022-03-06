package org.network.devicemon.boundary;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.network.devicemon.entity.NetworkDevice;
import org.network.devicemon.model.ApproveDevice;
import org.network.devicemon.model.NetworkDeviceBackupItem;
import org.network.devicemon.model.NetworkDeviceListItem;
import org.network.devicemon.model.SignOnInformation;
import org.network.devicemon.service.DeviceService;
import org.network.devicemon.service.LeaseService;
import org.network.devicemon.service.MacVendorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/device")
@Validated
public class DeviceBoundary {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceBoundary.class);

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
        return deviceService.findAll().stream()
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
        List<NetworkDevice> backup = objectMapper.readValue(file.getInputStream(), new TypeReference<List<NetworkDevice>>(){});
        deviceService.restore(backup);
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
