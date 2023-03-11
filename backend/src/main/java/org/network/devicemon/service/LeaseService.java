package org.network.devicemon.service;

import jakarta.transaction.Transactional;
import org.network.devicemon.entity.NetworkDevice;
import org.network.devicemon.entity.NetworkDeviceLease;
import org.network.devicemon.model.DhcpLease;
import org.network.devicemon.model.SignOnInformation;
import org.network.devicemon.repository.NetworkDeviceLeaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Validated
public class LeaseService {

    private final DeviceService deviceService;

    private final NetworkDeviceLeaseRepository leaseRepository;

    private final List<LeaseListener> leaseListeners;

    public LeaseService(DeviceService deviceService, NetworkDeviceLeaseRepository leaseRepository, List<LeaseListener> leaseListeners) {
        this.deviceService = deviceService;
        this.leaseRepository = leaseRepository;
        this.leaseListeners = leaseListeners;
    }

    private void endLeases(ZonedDateTime now, String macAddress) {
        List<NetworkDeviceLease> openLeases = leaseRepository.findAllByMacAddressAndLeaseEndIsNull(macAddress);
        for (NetworkDeviceLease openLease: openLeases) {
            openLease.setLeaseEnd(now);
            openLease.setLastSeen(now);
        }
        leaseRepository.saveAll(openLeases);
    }

    private NetworkDevice startLease(SignOnInformation signOnInformation, NetworkDevice device, ZonedDateTime lastSeen) {
        if (device == null) {
            device = deviceService.create(signOnInformation);
        }

        // Check if there are open-ended leases for this device. Stop these, because one device can only have one lease at any one time.
        endLeases(lastSeen, device.getMacAddress());
        NetworkDeviceLease startedLease = new NetworkDeviceLease();
        startedLease.setMacAddress(device.getMacAddress());
        startedLease.setHostname(device.getHostname());
        startedLease.setLeaseStart(lastSeen);
        startedLease.setLastSeen(lastSeen);
        startedLease.setDhcpServerName(signOnInformation.getDhcpServerName());
        startedLease.setInet4Address(signOnInformation.getInet4Address());
        startedLease = leaseRepository.save(startedLease);
        return deviceService.updateActualLease(device, startedLease);
    }

    @Transactional
    public String startLease(SignOnInformation signOnInformation) {
        NetworkDevice device = deviceService.find(signOnInformation.getMacAddress());
        final NetworkDevice startedDevice = startLease(signOnInformation, device, ZonedDateTime.now());
        leaseListeners.forEach(leaseListener -> leaseListener.onLeaseStart(startedDevice));
        return startedDevice.getHostname();
    }

    @Transactional
    public void endLease(String macAddress) {
        // Check if there are open-ended leases for this device. Stop these, because one device can only have one lease at any one time.
        endLeases(ZonedDateTime.now(), macAddress);
        if (!leaseListeners.isEmpty()) {
            NetworkDevice device = deviceService.find(macAddress);
            leaseListeners.forEach(leaseListener -> leaseListener.onLeaseEnd(device));
        }
    }

    @Transactional
    public void synchronizeLeases(List<DhcpLease> leases) {
        final ZonedDateTime now = ZonedDateTime.now();

        List<String> activeMacAddresses = leases.stream().map(SignOnInformation::getMacAddress).collect(Collectors.toList());

        // Find all devices marked as online in database but which are not contained in active leases list.
        // These database entries are outdated, end their leases.
        List<NetworkDevice> offlineDevices = deviceService.findAllOnlineDevicesNotInList(activeMacAddresses);
        offlineDevices.forEach(networkDevice -> {
                    String macAddr = networkDevice.getMacAddress();
                    endLeases(now, macAddr);
                });

        List<NetworkDevice> onlineDevices = deviceService.findAllInList(activeMacAddresses);
        Map<String, NetworkDevice> activeDeviceMap = onlineDevices.stream()
                .collect(Collectors.toMap(NetworkDevice::getMacAddress, Function.identity()));

        for (DhcpLease lease: leases) {
            ZonedDateTime lastSeen = ZonedDateTime.now().minusSeconds(DurationUtil.durationToSeconds(lease.getLastSeenAsString()));
            NetworkDevice actualDevice = activeDeviceMap.get(lease.getMacAddress());
            if (
                    // If no device exists: Create new unapproved device and lease.
                    actualDevice == null
                    // If an inactive device found in database: End old leases and start new lease.
                    || actualDevice.getActualLease() == null
                    || actualDevice.getActualLease().getLeaseEnd() != null
                    // If existing device found with started lease, but IP and DHCP server name do not match: End old leases and start new lease.
                    || !actualDevice.getActualLease().getDhcpServerName().equals(lease.getDhcpServerName())
                    || !actualDevice.getActualLease().getInet4Address().equals(lease.getInet4Address())) {
                startLease(lease, actualDevice, lastSeen);
            } else {
                // Found existing device and started lease, just upgrade last seen.
                NetworkDeviceLease networkDeviceLease = actualDevice.getActualLease();
                networkDeviceLease.setLastSeen(lastSeen);
                leaseRepository.save(networkDeviceLease);
            }
        }
        leaseListeners.forEach(leaseListener -> leaseListener.onLeasesSynchronize(offlineDevices, onlineDevices));
    }
}
