package org.network.devicemon.model;

import org.network.devicemon.entity.NetworkDevice;
import org.network.devicemon.entity.NetworkDeviceLease;

import java.time.ZonedDateTime;

public class NetworkDeviceListItem {

    private String macAddress;

    private String inet4Address;

    private String dhcpServerName;

    private ZonedDateTime lastSeen;

    private String hostname;

    private boolean approved;

    public NetworkDeviceListItem(NetworkDevice networkDevice) {
        macAddress = networkDevice.getMacAddress();
        hostname = networkDevice.getHostname();
        approved = networkDevice.isApproved();
        NetworkDeviceLease networkDeviceLease = networkDevice.getActualLease();
        if (networkDeviceLease != null) {
            inet4Address = networkDeviceLease.getInet4Address();
            dhcpServerName = networkDeviceLease.getDhcpServerName();
            lastSeen = networkDeviceLease.getLeaseEnd();
        }
        lastSeen = lastSeen == null ? ZonedDateTime.now() : lastSeen;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getInet4Address() {
        return inet4Address;
    }

    public String getDhcpServerName() {
        return dhcpServerName;
    }

    public ZonedDateTime getLastSeen() {
        return lastSeen;
    }

    public String getHostname() {
        return hostname;
    }

    public boolean isApproved() {
        return approved;
    }
}
