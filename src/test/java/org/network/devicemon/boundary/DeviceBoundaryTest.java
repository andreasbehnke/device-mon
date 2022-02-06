package org.network.devicemon.boundary;

import org.junit.jupiter.api.Test;
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
        assertEquals("signOn.signOnInformation.maxAddress: must be a valid MAC address", violationException.getMessage());
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
        information.setMacAddress("B8:27:EB:33:EF:76");
        assertEquals("B8-27-EB-33-EF-76", deviceBoundary.signOn(information));
    }

    @Test
    public void testSignOnWithClientHostname() {
        SignOnInformation information = new SignOnInformation();
        information.setDhcpServerName("dhcp-server-wlan");
        information.setInet4Address("1.1.1.9");
        information.setMacAddress("B8:27:EB:33:EF:76");
        information.setClientHostname("ClientHostName");
        assertEquals("ClientHostName", deviceBoundary.signOn(information));
    }
}
