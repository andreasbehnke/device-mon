package org.network.devicemon.entity;

import org.network.devicemon.validation.Inet4Address;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Entity
@Table(indexes = {
        @Index(name = "idx_leaseStart", columnList = "leaseStart"),
        @Index(name = "idx_leaseEnd", columnList = "leaseEnd"),
        @Index(name = "idx_dhcpServerName", columnList = "dhcpServerName"),
        @Index(name = "idx_inet4Address", columnList = "inet4Address")
})
public class NetworkDeviceLease extends EntityBase {

    @ManyToOne(optional = false)
    private NetworkDevice networkDevice;

    @NotNull
    private ZonedDateTime leaseStart;

    private ZonedDateTime leaseEnd;

    @NotBlank
    private String dhcpServerName;

    @NotNull
    @Inet4Address
    private String inet4Address;

    public NetworkDevice getNetworkDevice() {
        return networkDevice;
    }

    public void setNetworkDevice(NetworkDevice networkDevice) {
        this.networkDevice = networkDevice;
    }

    public ZonedDateTime getLeaseStart() {
        return leaseStart;
    }

    public void setLeaseStart(ZonedDateTime leaseStart) {
        this.leaseStart = leaseStart;
    }

    public ZonedDateTime getLeaseEnd() {
        return leaseEnd;
    }

    public void setLeaseEnd(ZonedDateTime leaseEnd) {
        this.leaseEnd = leaseEnd;
    }

    public String getDhcpServerName() {
        return dhcpServerName;
    }

    public void setDhcpServerName(String dhcpServerName) {
        this.dhcpServerName = dhcpServerName;
    }

    public String getInet4Address() {
        return inet4Address;
    }

    public void setInet4Address(String inet4Address) {
        this.inet4Address = inet4Address;
    }
}
