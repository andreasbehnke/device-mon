package org.network.devicemon.boundary;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.network.devicemon.MacAddressFixture;
import org.network.devicemon.model.NetworkDeviceListItem;
import org.network.devicemon.model.SignOnInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
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

    private int getIndexOf(List<NetworkDeviceListItem> devices, String macAddress) {
        for(int i = 0; i < devices.size(); i++) {
            if (devices.get(i).getMacAddress().equals(macAddress)) {
                return i;
            }
        }
        return -1;
    }

    @Test
    public void testGetAllDevices() {
        SignOnInformation information = new SignOnInformation();
        information.setDhcpServerName("dhcp-server-wlan");
        information.setInet4Address("1.1.1.9");
        String macAddress1 = MacAddressFixture.createRandomMacAddress();
        information.setMacAddress(macAddress1);
        information.setClientHostname("ClientHostName1");
        deviceBoundary.signOn(information);
        deviceBoundary.signOff(macAddress1); // set lease end
        String macAddress2 = MacAddressFixture.createRandomMacAddress();
        information.setMacAddress(macAddress2);
        information.setClientHostname("ClientHostName2");
        deviceBoundary.signOn(information);
        deviceBoundary.signOff(macAddress2); // set lease end
        String macAddress3 = MacAddressFixture.createRandomMacAddress();
        information.setMacAddress(macAddress3);
        information.setClientHostname("ClientHostName3");
        deviceBoundary.signOn(information); // null lease end, should be first!
        List<NetworkDeviceListItem> devices = deviceBoundary.getAllDevices();
        // check order of result list
        int index1 = getIndexOf(devices, macAddress1);
        int index2 = getIndexOf(devices, macAddress2);
        int index3 = getIndexOf(devices, macAddress3);
        assertTrue(index3 < index2);
        assertTrue(index2 < index1);
    }
}
