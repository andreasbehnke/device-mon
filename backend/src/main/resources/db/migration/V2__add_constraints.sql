DROP INDEX unique_macaddress;
CREATE UNIQUE INDEX unique_macAddress ON network_device (mac_address);
ALTER TABLE network_device ALTER COLUMN hostname SET NOT NULL;
ALTER TABLE network_device ALTER COLUMN mac_address SET NOT NULL;

ALTER TABLE network_device_lease ALTER COLUMN mac_address SET NOT NULL;
ALTER TABLE network_device_lease ALTER COLUMN hostname SET NOT NULL;
ALTER TABLE network_device_lease ALTER COLUMN lease_start SET NOT NULL;
ALTER TABLE network_device_lease ALTER COLUMN dhcp_server_name SET NOT NULL;
ALTER TABLE network_device_lease ALTER COLUMN inet4address SET NOT NULL;
