package org.network.devicemon.model;

import com.sun.istack.NotNull;
import org.network.devicemon.validation.Inet4Address;
import org.network.devicemon.validation.MacAddress;

import javax.validation.constraints.NotBlank;

public class SignOnInformation extends LeaseBase {

    private String clientHostname;

    public String getClientHostname() {
        return clientHostname;
    }

    public void setClientHostname(String clientHostname) {
        this.clientHostname = clientHostname;
    }
}
