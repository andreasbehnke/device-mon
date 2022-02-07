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
