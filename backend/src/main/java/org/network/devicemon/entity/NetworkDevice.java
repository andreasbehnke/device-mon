package org.network.devicemon.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.network.devicemon.validation.MacAddress;

@Entity
@Table(indexes = {
        @Index(name = "unique_macAddress", columnList = "macAddress", unique = true),
        @Index(name = "idx_approved", columnList = "approved")
})
public class NetworkDevice extends EntityBase {

    @NotNull
    @MacAddress
    private String macAddress;

    @NotNull
    private String hostname;

    @Column(columnDefinition = "boolean default false")
    private boolean approved;

    @OneToOne(fetch = FetchType.EAGER)
    private NetworkDeviceLease actualLease;

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

    public NetworkDeviceLease getActualLease() {
        return actualLease;
    }

    public void setActualLease(NetworkDeviceLease actualLease) {
        this.actualLease = actualLease;
    }
}
