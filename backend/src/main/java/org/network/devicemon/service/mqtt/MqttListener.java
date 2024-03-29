package org.network.devicemon.service.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.network.devicemon.entity.NetworkDevice;
import org.network.devicemon.model.NetworkDeviceListItem;
import org.network.devicemon.service.LeaseListener;
import org.network.devicemon.service.MacVendorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@ConditionalOnProperty(prefix = "devicemon.mqtt", name = "broker.address")
public class MqttListener implements LeaseListener {

    private static final Logger LOG = LoggerFactory.getLogger(MqttListener.class);

    private final String prefix;

    private final String brokerAddress;

    private final String clientId;

    private MqttClient client;

    private final MqttConnectOptions options;

    private final ObjectMapper objectMapper;

    private final MacVendorService macVendorService;

    public MqttListener(@Value("${devicemon.mqtt.client.id}") String clientId,
            @Value("${devicemon.mqtt.prefix}") String prefix,
            @Value("${devicemon.mqtt.broker.address}") String brokerAddress,
            @Value("${devicemon.mqtt.broker.user}") String brokerUser,
            @Value("${devicemon.mqtt.broker.password}") String brokerPassword,
            ObjectMapper objectMapper,
            MacVendorService macVendorService) {
        this.options = new MqttConnectOptions();
        this.options.setUserName(brokerUser);
        this.options.setPassword(brokerPassword.toCharArray());
        this.prefix = prefix;
        this.brokerAddress = brokerAddress;
        this.clientId = clientId;
        this.objectMapper = objectMapper;
        this.macVendorService = macVendorService;
        LOG.info("Starting MQTT publishing with client id \"{}\", prefix \"{}\" and user \"{}\" to broker \"{}\"", clientId, prefix, brokerUser, brokerAddress);
        try {
            client = new MqttClient(this.brokerAddress, this.clientId, new MemoryPersistence());
            client.connect(options);
        } catch (MqttException e) {
            LOG.error("Could not start MQTT publishing", e);
        }
    }

    private void publish(Object message) {
        try {
            if (client == null) {
                client = new MqttClient(this.brokerAddress, this.clientId, new MemoryPersistence());
            }
            if (!client.isConnected()) {
                client.connect(options);
            }
            client.publish(prefix, new MqttMessage(objectMapper.writeValueAsBytes(message)));
        } catch (JsonProcessingException | MqttException e) {
            LOG.error("Could not publish MQTT message", e);
        }
    }

    private void publishDeviceChange(NetworkDevice activeDevice) {
        String macAddress = activeDevice.getMacAddress();
        NetworkDeviceListItem networkDeviceListItem = new NetworkDeviceListItem(activeDevice, macVendorService.getVendorInformation(macAddress));
        publish(new DevicesMessage(
                Collections.singletonMap(
                        networkDeviceListItem.getUniqueName(), networkDeviceListItem))
        );
    }

    @Override
    public void onLeaseStart(NetworkDevice activeDevice) {
        publishDeviceChange(activeDevice);
    }

    @Override
    public void onLeaseEnd(NetworkDevice inactiveDevice) {
        publishDeviceChange(inactiveDevice);
    }

    @Override
    public void onLeasesSynchronize(List<NetworkDevice> inactiveDevices, List<NetworkDevice> activeDevices) {
        List<NetworkDeviceListItem> devices = Stream.concat(inactiveDevices.stream(), activeDevices.stream())
                .map(networkDevice -> {
                    String macAddress = networkDevice.getMacAddress();
                    return new NetworkDeviceListItem(networkDevice, macVendorService.getVendorInformation(macAddress));
                })
                .toList();
        Map<String, NetworkDeviceListItem> devicesMap = devices.stream()
                .collect(Collectors.toMap(NetworkDeviceListItem::getUniqueName, Function.identity()));
        publish(new DevicesMessage(devicesMap));
        publish(new StatusMessage(devices.stream()
                .collect(
                        Collectors.groupingBy(NetworkDeviceListItem::getDhcpServerName, Collectors.summingLong(value -> value.isActiveLease() ? 1L : 0L )))));
    }
}
