package org.network.devicemon.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MacVendorServiceTest {

    private final MacVendorService macVendorService = new MacVendorService();

    @Test
    public void testVirtualMacAddress() {
        Assertions.assertEquals("virtual", macVendorService.getVendorInformation("02:AA:AA:AA:AA:BB"));
        Assertions.assertEquals("virtual", macVendorService.getVendorInformation("06:AA:AA:AA:AA:BB"));
        Assertions.assertEquals("virtual", macVendorService.getVendorInformation("0A:AA:AA:AA:AA:BB"));
        Assertions.assertEquals("virtual", macVendorService.getVendorInformation("0E:AA:AA:AA:AA:BB"));
        Assertions.assertEquals("virtual", macVendorService.getVendorInformation("03:AA:AA:AA:AA:BB"));
        Assertions.assertEquals("virtual", macVendorService.getVendorInformation("07:AA:AA:AA:AA:BB"));
        Assertions.assertEquals("virtual", macVendorService.getVendorInformation("0B:AA:AA:AA:AA:BB"));
        Assertions.assertEquals("virtual", macVendorService.getVendorInformation("0F:AA:AA:AA:AA:BB"));
    }

    @Test
    public void testUnknownMacAddress() {
        Assertions.assertEquals("unknown", macVendorService.getVendorInformation("08:AA:AA:AA:AA:BB"));
    }

    @Test
    public void testMALMacAddress() {
        Assertions.assertEquals("Samsung Electronics (UK) Ltd", macVendorService.getVendorInformation("FA:63:E1:AA:AA:BB"));
    }

    @Test
    public void testMAMMacAddress() {
        Assertions.assertEquals("Honeywell GNO", macVendorService.getVendorInformation("00:50:C2:0A:CA:BB"));
    }

    @Test
    public void testMASMacAddress() {
        Assertions.assertEquals("Mitsubishi Electric India Pvt. Ltd.", macVendorService.getVendorInformation("70:B3:D5:79:71:23"));
    }
}
