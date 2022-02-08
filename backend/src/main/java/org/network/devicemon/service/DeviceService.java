package org.network.devicemon.service;

import org.network.devicemon.entity.NetworkDevice;
import org.network.devicemon.model.SignOnInformation;
import org.network.devicemon.repository.NetworkDeviceRepository;
import org.springframework.stereotype.Service;

import static org.network.devicemon.service.MacAddressUtil.toValidDnsHostName;
import static org.springframework.util.StringUtils.hasText;

@Service
public class DeviceService {

    private final NetworkDeviceRepository deviceRepository;

    public DeviceService(NetworkDeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public NetworkDevice find(String macAddress) {
        return deviceRepository.findByMacAddress(macAddress);
    }

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
}
