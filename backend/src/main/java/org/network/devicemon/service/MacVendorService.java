package org.network.devicemon.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class MacVendorService {

    private final Map<String, String> macToVendor = new HashMap<>();

    public MacVendorService() {
        try(CSVReader csvReader = new CSVReader(new InputStreamReader(getClass().getResourceAsStream("mac-vendors-export.csv")))) {
            String[] line;
            csvReader.readNext(); // skip header
            while ((line = csvReader.readNext()) != null) {
                String macPrefix = line[0].toUpperCase(Locale.ROOT);
                String vendorName = line[1];
                if (StringUtils.hasText(macPrefix)) {
                    macToVendor.put(macPrefix, vendorName);
                }
            }
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    public String getVendorInformation(String macAddress) {
        Assert.hasText(macAddress, "macAddress must not be empty");
        if (macAddress.length() != 17) {
            throw new IllegalArgumentException("macAddress must be of length 17");
        }
        macAddress = macAddress.toUpperCase(Locale.ROOT);
        String match = macAddress.substring(0, 13);
        String vendor = macToVendor.get(match);
        if (vendor == null) {
            match = macAddress.substring(0, 10);
            vendor = macToVendor.get(match);
        }
        if (vendor == null) {
            match = macAddress.substring(0, 8);
            vendor = macToVendor.get(match);
        }
        if (vendor == null) {
            // vendor not found, distinguish between unknown and virtual
            char c = macAddress.charAt(1);
            if (c == '2' || c == '6' || c == 'A' || c == 'E' || c == '3' || c == '7' || c == 'B' || c == 'F') {
                vendor = "virtual";
            } else {
                vendor = "unknown";
            }
        }
        return vendor;
    }
}
