package org.network.devicemon.model;

import org.network.devicemon.entity.NetworkDevice;

public class NetworkDeviceBackupItem {

    private String macAddress;

    private String hostname;

    public NetworkDeviceBackupItem(NetworkDevice networkDevice) {
        macAddress = networkDevice.getMacAddress();
        hostname = networkDevice.getHostname();
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getHostname() {
        return hostname;
    }
}
