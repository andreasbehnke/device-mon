package org.network.devicemon.repository;

import org.network.devicemon.entity.NetworkDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NetworkDeviceRepository extends JpaRepository<NetworkDevice, UUID> {

    NetworkDevice findFirstByMacAddress(String macAddress);
}
