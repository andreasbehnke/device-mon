package org.network.devicemon.service;

import org.network.devicemon.entity.NetworkDevice;
import org.network.devicemon.entity.NetworkDeviceLease;
import org.network.devicemon.model.SignOnInformation;
import org.network.devicemon.repository.NetworkDeviceLeaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Validated
public class LeaseService {

    private final DeviceService deviceService;

    private final NetworkDeviceLeaseRepository leaseRepository;

    public LeaseService(DeviceService deviceService, NetworkDeviceLeaseRepository leaseRepository) {
        this.deviceService = deviceService;
        this.leaseRepository = leaseRepository;
    }

    private void endLeases(ZonedDateTime now, String macAddress) {
        List<NetworkDeviceLease> openLeases = leaseRepository.findAllByMacAddressAndLeaseEndIsNull(macAddress);
        for (NetworkDeviceLease openLease: openLeases) {
            openLease.setLeaseEnd(now);
            openLease.setLastSeen(now);
        }
        leaseRepository.saveAll(openLeases);
    }

    @Transactional
    public String startLease(SignOnInformation signOnInformation) {
        NetworkDevice device = deviceService.find(signOnInformation.getMacAddress());
        if (device == null) {
            device = deviceService.create(signOnInformation);
        }

        ZonedDateTime now = ZonedDateTime.now();
        // Check if there are open-ended leases for this device. Stop these, because one device can only have one lease at any one time.
        endLeases(now, device.getMacAddress());
        NetworkDeviceLease startedLease = new NetworkDeviceLease();
        startedLease.setMacAddress(device.getMacAddress());
        startedLease.setHostname(device.getHostname());
        startedLease.setLeaseStart(now);
        startedLease.setLastSeen(now);
        startedLease.setDhcpServerName(signOnInformation.getDhcpServerName());
        startedLease.setInet4Address(signOnInformation.getInet4Address());
        deviceService.updateActualLease(device, leaseRepository.save(startedLease));
        return device.getHostname();
    }

    @Transactional
    public void endLease(String macAddress) {
        // Check if there are open-ended leases for this device. Stop these, because one device can only have one lease at any one time.
        endLeases(ZonedDateTime.now(), macAddress);
    }
}
