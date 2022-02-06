package org.network.devicemon.service;

import org.network.devicemon.model.SignOnInformation;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

import static org.network.devicemon.service.MacAddressUtil.toValidDnsHostName;
import static org.springframework.util.StringUtils.hasText;

@Service
@Validated
public class DeviceService {

    public String signOn(@Valid SignOnInformation signOnInformation) {
        return hasText(signOnInformation.getClientHostname()) ? signOnInformation.getClientHostname() : toValidDnsHostName(signOnInformation.getMacAddress());
    }

}
