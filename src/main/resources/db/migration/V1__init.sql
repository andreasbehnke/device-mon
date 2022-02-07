CREATE TABLE network_device
(
    id            UUID NOT NULL,
    created       TIMESTAMP WITHOUT TIME ZONE,
    last_modified TIMESTAMP WITHOUT TIME ZONE,
    mac_address   VARCHAR(255),
    hostname      VARCHAR(255),
    approved      BOOLEAN DEFAULT FALSE,
    CONSTRAINT pk_networkdevice PRIMARY KEY (id)
);

CREATE INDEX idx_approved ON network_device (approved);

CREATE INDEX unique_macAddress ON network_device (mac_address);

CREATE TABLE network_device_lease
(
    id                UUID NOT NULL,
    created           TIMESTAMP WITHOUT TIME ZONE,
    last_modified     TIMESTAMP WITHOUT TIME ZONE,
    network_device_id UUID NOT NULL,
    lease_start       TIMESTAMP with time zone,
    lease_end         TIMESTAMP with time zone,
    dhcp_server_name  VARCHAR(255),
    inet4address      VARCHAR(255),
    CONSTRAINT pk_networkdevicelease PRIMARY KEY (id)
);

CREATE INDEX idx_dhcpServerName ON network_device_lease (dhcp_server_name);

CREATE INDEX idx_inet4Address ON network_device_lease (inet4address);

CREATE INDEX idx_leaseEnd ON network_device_lease (lease_end);

CREATE INDEX idx_leaseStart ON network_device_lease (lease_start);

ALTER TABLE network_device_lease
    ADD CONSTRAINT FK_NETWORKDEVICELEASE_ON_NETWORKDEVICE FOREIGN KEY (network_device_id) REFERENCES network_device (id);
