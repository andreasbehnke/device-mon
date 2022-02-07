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

    private final LeaseService leaseService;

    public DeviceService(NetworkDeviceRepository deviceRepository, LeaseService leaseService) {
        this.deviceRepository = deviceRepository;
        this.leaseService = leaseService;
    }

    public String signOn(SignOnInformation signOnInformation) {
        NetworkDevice networkDevice = deviceRepository.findByMacAddress(signOnInformation.getMacAddress());
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
        leaseService.startLease(networkDevice, signOnInformation);
        return networkDevice.getHostname();
    }

}
