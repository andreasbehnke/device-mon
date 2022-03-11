package org.network.devicemon.repository;

import org.network.devicemon.entity.NetworkDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface NetworkDeviceRepository extends JpaRepository<NetworkDevice, UUID> {

    NetworkDevice findByMacAddress(String macAddress);

    List<NetworkDevice> findAllByOrderByApprovedAscActualLeaseLastSeenDesc();

    List<NetworkDevice> findAllByOrderByHostname();

    void deleteByMacAddress(String macAddress);
}
