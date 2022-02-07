package org.network.devicemon.service;

import org.network.devicemon.entity.NetworkDevice;
import org.network.devicemon.entity.NetworkDeviceLease;
import org.network.devicemon.model.SignOnInformation;
import org.network.devicemon.repository.NetworkDeviceLeaseRepository;
import org.network.devicemon.validation.MacAddress;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Validated
public class LeaseService {

    private final NetworkDeviceLeaseRepository leaseRepository;

    public LeaseService(NetworkDeviceLeaseRepository leaseRepository) {
        this.leaseRepository = leaseRepository;
    }

    public void startLease(@NotNull NetworkDevice device,@NotNull SignOnInformation signOnInformation) {
        ZonedDateTime now = ZonedDateTime.now();
        // Check if there are open-ended leases for this device. Stop these, because one device can only have one lease at any one time.
        List<NetworkDeviceLease> openLeases = leaseRepository.findAllByNetworkDeviceAndLeaseEndIsNull(device);
        for (NetworkDeviceLease openLease: openLeases) {
            openLease.setLeaseEnd(now);
            leaseRepository.save(openLease);
        }
        NetworkDeviceLease startedLease = new NetworkDeviceLease();
        startedLease.setNetworkDevice(device);
        startedLease.setLeaseStart(now);
        startedLease.setDhcpServerName(signOnInformation.getDhcpServerName());
        startedLease.setInet4Address(signOnInformation.getInet4Address());
        leaseRepository.save(startedLease);
    }

    public void endLease(@NotNull String macAddress) {
        ZonedDateTime now = ZonedDateTime.now();
        // Check if there are open-ended leases for this device. Stop these, because one device can only have one lease at any one time.
        List<NetworkDeviceLease> openLeases = leaseRepository.findAllByNetworkDeviceMacAddressAndLeaseEndIsNull(macAddress);
        for (NetworkDeviceLease openLease: openLeases) {
            openLease.setLeaseEnd(now);
            leaseRepository.save(openLease);
        }
    }
}
