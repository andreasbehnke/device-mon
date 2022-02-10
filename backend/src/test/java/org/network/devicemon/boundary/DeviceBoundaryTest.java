package org.network.devicemon.boundary;

import org.junit.jupiter.api.Test;
import org.network.devicemon.MacAddressFixture;
import org.network.devicemon.model.SignOnInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class DeviceBoundaryTest {

    @Autowired
    private DeviceBoundary deviceBoundary;

    @Test
    public void testSignOnInvalidMacAddress() {
        SignOnInformation information = new SignOnInformation();
        information.setDhcpServerName("dhcp-server-wlan");
        information.setInet4Address("1.1.1.1");
        information.setMacAddress("B8:27:EB:33:EF:7x");
        ConstraintViolationException violationException = assertThrows(ConstraintViolationException.class, () -> deviceBoundary.signOn(information));
        assertEquals("signOn.signOnInformation.macAddress: must be a valid MAC address", violationException.getMessage());
    }

    @Test
    public void testSignOnInvalidInet4Address() {
        SignOnInformation information = new SignOnInformation();
        information.setDhcpServerName("dhcp-server-wlan");
        information.setInet4Address("1.1.1.999");
        information.setMacAddress("B8:27:EB:33:EF:76");
        ConstraintViolationException violationException = assertThrows(ConstraintViolationException.class, () -> deviceBoundary.signOn(information));
        assertEquals("signOn.signOnInformation.inet4Address: must be a valid inet 4 address", violationException.getMessage());
    }

    @Test
    public void testSignOnWithoutClientHostname() {
        SignOnInformation information = new SignOnInformation();
        information.setDhcpServerName("dhcp-server-wlan");
        information.setInet4Address("1.1.1.9");
        String macAddress = MacAddressFixture.createRandomMacAddress();
        information.setMacAddress(macAddress);
        String expectedHostName = macAddress.replace(':', '-');
        assertEquals(expectedHostName, deviceBoundary.signOn(information));
    }

    @Test
    public void testSignOnWithClientHostname() {
        SignOnInformation information = new SignOnInformation();
        information.setDhcpServerName("dhcp-server-wlan");
        information.setInet4Address("1.1.1.9");
        information.setMacAddress(MacAddressFixture.createRandomMacAddress());
        information.setClientHostname("ClientHostName");
        assertEquals("ClientHostName", deviceBoundary.signOn(information));
    }
}
