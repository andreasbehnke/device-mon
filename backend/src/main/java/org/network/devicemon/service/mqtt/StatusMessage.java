package org.network.devicemon.service.mqtt;

import java.util.Map;

public class StatusMessage {

    private final Map<String, Long> dhcpServer;

    public StatusMessage(Map<String, Long> dhcpServer) {
        this.dhcpServer = dhcpServer;
    }

    public Map<String, Long> getDhcpServer() {
        return dhcpServer;
    }
}
