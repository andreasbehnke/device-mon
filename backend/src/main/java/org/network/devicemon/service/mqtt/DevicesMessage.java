package org.network.devicemon.service.mqtt;

import org.network.devicemon.model.NetworkDeviceListItem;

import java.util.Map;

public class DevicesMessage {

    private final Map<String, NetworkDeviceListItem> devices;

    public DevicesMessage(Map<String, NetworkDeviceListItem> devices) {
        this.devices = devices;
    }

    public Map<String, NetworkDeviceListItem> getDevices() {
        return devices;
    }
}
