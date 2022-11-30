package org.network.devicemon.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.network.devicemon.validation.Inet4Address;
import org.network.devicemon.validation.MacAddress;

public class SignOnInformation {

    private String clientHostname;

    @NotBlank
    private String dhcpServerName;

    @NotNull
    @MacAddress
    private String macAddress;

    @NotNull
    @Inet4Address
    private String inet4Address;

    public String getClientHostname() {
        return clientHostname;
    }

    public void setClientHostname(String clientHostname) {
        this.clientHostname = clientHostname;
    }

    public String getDhcpServerName() {
        return dhcpServerName;
    }

    public void setDhcpServerName(String dhcpServerName) {
        this.dhcpServerName = dhcpServerName;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getInet4Address() {
        return inet4Address;
    }

    public void setInet4Address(String inet4Address) {
        this.inet4Address = inet4Address;
    }
}
