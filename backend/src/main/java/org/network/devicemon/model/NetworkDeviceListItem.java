package org.network.devicemon.model;

import org.network.devicemon.entity.NetworkDevice;
import org.network.devicemon.entity.NetworkDeviceLease;

import java.time.ZonedDateTime;

public class NetworkDeviceListItem {

    private boolean activeLease;

    private String macAddress;

    private String vendor;

    private String inet4Address;

    private String dhcpServerName;

    private ZonedDateTime lastSeen;

    private String hostname;

    private boolean approved;

    public NetworkDeviceListItem(NetworkDevice networkDevice, String vendor) {
        macAddress = networkDevice.getMacAddress();
        this.vendor = vendor;
        hostname = networkDevice.getHostname();
        approved = networkDevice.isApproved();
        NetworkDeviceLease networkDeviceLease = networkDevice.getActualLease();
        if (networkDeviceLease != null) {
            inet4Address = networkDeviceLease.getInet4Address();
            dhcpServerName = networkDeviceLease.getDhcpServerName();
            lastSeen = networkDeviceLease.getLeaseEnd();
            activeLease = false;
            if (lastSeen == null) {
                lastSeen = networkDeviceLease.getLeaseStart();
                activeLease = true;
            }
        }
    }

    public boolean isActiveLease() {
        return activeLease;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getVendor() {
        return vendor;
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
