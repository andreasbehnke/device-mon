package org.network.devicemon;

import java.util.Random;

public class MacAddressFixture {

    public static String createRandomMacAddress() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            byte b = (byte)random.nextInt(256);
            if (sb.length() > 0) {
                sb.append(':');
            }
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
