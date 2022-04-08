package org.network.devicemon.service;

import org.network.devicemon.entity.NetworkDevice;
import org.network.devicemon.entity.NetworkDeviceLease;
import org.network.devicemon.model.SignOnInformation;
import org.network.devicemon.repository.NetworkDeviceRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.network.devicemon.service.MacAddressUtil.toValidDnsHostName;
import static org.springframework.util.StringUtils.hasText;

@Service
public class DeviceService {

    private final NetworkDeviceRepository deviceRepository;

    public DeviceService(NetworkDeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public List<NetworkDevice> findAllOrderByActivity() {
        return deviceRepository.findAllByOrderByApprovedAscActualLeaseLeaseEndDescActualLeaseLastSeenDesc();
    }

    public List<NetworkDevice> findAllOrderByHostname() {
        return deviceRepository.findAllByOrderByHostname();
    }

    public List<NetworkDevice> findAllOnlineDevicesNotInList(List<String> excludedMacAddresses) {
        return deviceRepository.findAllByActualLeaseLeaseEndIsNullAndMacAddressNotIn(excludedMacAddresses);
    }

    public List<NetworkDevice> findAllInList(List<String> macAddresses) {
        return deviceRepository.findAllByMacAddressIn(macAddresses);
    }

    public NetworkDevice find(String macAddress) {
        return deviceRepository.findByMacAddress(macAddress);
    }

    @Transactional
    public void restore(List<NetworkDevice> backup) {
        Map<String, NetworkDevice> existingDevices = deviceRepository.findAll().stream()
                .collect(Collectors.toMap(NetworkDevice::getMacAddress, Function.identity()));
        for (NetworkDevice device: backup) {
            NetworkDevice persistedDevice = existingDevices.getOrDefault(device.getMacAddress(), new NetworkDevice());
            persistedDevice.setMacAddress(device.getMacAddress());
            persistedDevice.setHostname(device.getHostname());
            persistedDevice.setApproved(true);
            deviceRepository.save(persistedDevice);
        }
    }

    @Transactional
    public NetworkDevice create(SignOnInformation signOnInformation) {
        // string temporally hostname
        String hostname = hasText(signOnInformation.getClientHostname()) ? signOnInformation.getClientHostname() : toValidDnsHostName(signOnInformation.getMacAddress());

        // create new network device
        NetworkDevice networkDevice = new NetworkDevice();
        networkDevice.setMacAddress(signOnInformation.getMacAddress());
        networkDevice.setHostname(hostname);
        networkDevice.setApproved(false);
        return deviceRepository.save(networkDevice);
    }

    @Transactional
    public NetworkDevice updateActualLease(NetworkDevice networkDevice, NetworkDeviceLease lease) {
        networkDevice.setActualLease(lease);
        return deviceRepository.save(networkDevice);
    }

    @Transactional
    public NetworkDevice approve(String macAddress, String hostname) {
        NetworkDevice networkDevice = find(macAddress);
        if (networkDevice != null) {
            networkDevice.setHostname(hostname);
            networkDevice.setApproved(true);
            return deviceRepository.save(networkDevice);
        } else {
            throw new EntityNotFoundException("Missing device " + macAddress);
        }
    }

    @Transactional
    public void delete(String macAddress) {
        deviceRepository.deleteByMacAddress(macAddress);
    }
}
