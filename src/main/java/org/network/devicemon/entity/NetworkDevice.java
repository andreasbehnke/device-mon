package org.network.devicemon.entity;

import com.sun.istack.NotNull;
import org.network.devicemon.validation.MacAddress;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class NetworkDevice extends EntityBase {

    @NotNull
    @MacAddress
    private String macAddress;

    @NotNull
    private String hostname;

    @Column(columnDefinition = "boolean default false")
    private boolean approved;

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
