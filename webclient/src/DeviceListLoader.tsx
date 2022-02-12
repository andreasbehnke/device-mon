import React from "react";
import {NetworkDeviceListItem} from "./model/NetworkDeviceListItem";
import {useAsync} from "react-async-hook";
import {DeviceList} from "./DeviceList";
import {Alert} from "@mui/material";
import {DeviceService} from "./service/DeviceService";

export function DeviceListLoader() {

    async function loadDevices() : Promise<Array<NetworkDeviceListItem>> {
        const result = await DeviceService.getDeviceList();
        return result.data;
    }

    const { result, loading, error } = useAsync<Array<NetworkDeviceListItem>>(loadDevices, []);
    if (error) {
        return <Alert severity={"error"}>Could not load device list: {error.message}</Alert>
    } else if (loading || !result) {
        return <></>
    } else {
        return <DeviceList deviceList={result} />;
    }
}
