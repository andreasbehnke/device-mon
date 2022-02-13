import axios, {AxiosResponse} from "axios";
import {NetworkDeviceListItem} from "../model/NetworkDeviceListItem";

export class DeviceService {

    static getDeviceList() : Promise<AxiosResponse<Array<NetworkDeviceListItem>>> {
        return axios.get<Array<NetworkDeviceListItem>>("/api/device");
    }

    static downloadBackup() : void {
        window.location.href = "http://localhost:8080/api/device/backup";
    }

    static approveDevice(macAddress: string, hostname: string) : Promise<AxiosResponse<NetworkDeviceListItem>> {
        return axios.put<NetworkDeviceListItem>("/api/device/" + macAddress + "/approve", { hostname });
    }

    static deleteDevice(macAddress: string) : Promise<AxiosResponse<void, void>> {
        return axios.delete("/api/device/" + macAddress);
    }
}