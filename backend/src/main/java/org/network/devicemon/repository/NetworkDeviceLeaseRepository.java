package org.network.devicemon.repository;

import org.network.devicemon.entity.NetworkDevice;
import org.network.devicemon.entity.NetworkDeviceLease;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NetworkDeviceLeaseRepository extends JpaRepository<NetworkDeviceLease, UUID> {

    List<NetworkDeviceLease> findAllByMacAddressAndLeaseEndIsNull(String macAddress);
}
