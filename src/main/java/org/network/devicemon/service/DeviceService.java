package org.network.devicemon.service;

import org.network.devicemon.entity.NetworkDevice;
import org.network.devicemon.model.SignOnInformation;
import org.network.devicemon.repository.NetworkDeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

import static org.network.devicemon.service.MacAddressUtil.toValidDnsHostName;
import static org.springframework.util.StringUtils.hasText;

@Service
@Validated
public class DeviceService {

    private final NetworkDeviceRepository deviceRepository;

    public DeviceService(NetworkDeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public String signOn(@Valid SignOnInformation signOnInformation) {
        NetworkDevice networkDevice = deviceRepository.findFirstByMacAddress(signOnInformation.getMacAddress());
        if (networkDevice == null) {
            // string temporally hostname
            String hostname = hasText(signOnInformation.getClientHostname()) ? signOnInformation.getClientHostname() : toValidDnsHostName(signOnInformation.getMacAddress());

            // create new network device
            networkDevice = new NetworkDevice();
            networkDevice.setMacAddress(signOnInformation.getMacAddress());
            networkDevice.setHostname(hostname);
            networkDevice.setApproved(false);
            networkDevice = deviceRepository.save(networkDevice);
        }
        return networkDevice.getHostname();
    }

}
