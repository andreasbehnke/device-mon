package org.network.devicemon.service;

import org.network.devicemon.entity.NetworkDevice;

import java.util.List;

public interface LeaseListener {

    void onLeaseStart(NetworkDevice activeDevice);

    void onLeaseEnd(NetworkDevice inactiveDevice);

    void onLeasesSynchronize(List<NetworkDevice> inactiveDevices, List<NetworkDevice> activeDevices);

}
