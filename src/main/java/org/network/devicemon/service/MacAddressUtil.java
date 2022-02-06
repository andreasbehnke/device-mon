package org.network.devicemon.service;

import static org.springframework.util.Assert.notNull;

public final class MacAddressUtil {

    private MacAddressUtil() { }

    /**
     * @return A valid DNS host name based on macAddress by replacing all : with -
     */
    public static String toValidDnsHostName(String macAddress) {
        notNull(macAddress, "macAddress must not be null");
        return macAddress.replace(':','-');
    }

}
