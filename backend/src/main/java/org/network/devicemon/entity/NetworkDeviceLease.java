package org.network.devicemon.entity;

import org.network.devicemon.validation.Inet4Address;
import org.network.devicemon.validation.MacAddress;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Entity
@Table(indexes = {
        @Index(name = "idx_leaseStart", columnList = "leaseStart"),
        @Index(name = "idx_lastSeen", columnList = "lastSeen"),
        @Index(name = "idx_leaseEnd", columnList = "leaseEnd"),
        @Index(name = "idx_dhcpServerName", columnList = "dhcpServerName"),
        @Index(name = "idx_inet4Address", columnList = "inet4Address")
})
public class NetworkDeviceLease extends EntityBase {

    @NotNull
    @MacAddress
    private String macAddress;

    @NotNull
    private String hostname;

    @NotNull
    private ZonedDateTime leaseStart;

    @NotNull
    private ZonedDateTime lastSeen;

    private ZonedDateTime leaseEnd;

    @NotBlank
    private String dhcpServerName;

    @NotNull
    @Inet4Address
    private String inet4Address;

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

    public ZonedDateTime getLeaseStart() {
        return leaseStart;
    }

    public void setLeaseStart(ZonedDateTime leaseStart) {
        this.leaseStart = leaseStart;
    }

    public ZonedDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(ZonedDateTime lastSeen) {
        this.lastSeen = lastSeen;
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
