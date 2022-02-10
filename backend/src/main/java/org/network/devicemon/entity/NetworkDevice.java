package org.network.devicemon.entity;

import com.sun.istack.NotNull;
import org.network.devicemon.validation.MacAddress;

import javax.persistence.*;

@Entity
@Table(indexes = {
        @Index(name = "unique_macAddress", columnList = "macAddress"),
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
