ALTER TABLE network_device_lease ADD COLUMN last_seen TIMESTAMP WITH TIME ZONE;
UPDATE network_device_lease SET last_seen = lease_start WHERE lease_end IS NULL;
UPDATE network_device_lease SET last_seen = lease_end WHERE lease_end IS NOT NULL;
ALTER TABLE network_device_lease ALTER COLUMN last_seen SET NOT NULL;
CREATE INDEX idx_lastSeen ON network_device_lease (last_seen);
