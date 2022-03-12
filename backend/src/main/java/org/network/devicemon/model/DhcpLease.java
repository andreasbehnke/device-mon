package org.network.devicemon.model;

import javax.validation.constraints.NotEmpty;

public class DhcpLease extends SignOnInformation {

    @NotEmpty
    private String lastSeenAsString;

    public String getLastSeenAsString() {
        return lastSeenAsString;
    }

    public void setLastSeenAsString(String lastSeenAsString) {
        this.lastSeenAsString = lastSeenAsString;
    }
}
