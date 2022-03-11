package org.network.devicemon.model;

import com.sun.istack.NotNull;
import org.network.devicemon.validation.Inet4Address;
import org.network.devicemon.validation.MacAddress;

import javax.validation.constraints.NotBlank;

public abstract class LeaseBase {

    @NotBlank
    private String dhcpServerName;

    @NotNull
    @MacAddress
    private String macAddress;

    @NotNull
    @Inet4Address
    private String inet4Address;

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
