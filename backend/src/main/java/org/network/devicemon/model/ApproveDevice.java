package org.network.devicemon.model;

import jakarta.validation.constraints.NotEmpty;

public class ApproveDevice {

    @NotEmpty
    private String hostname;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
