import axios, {AxiosResponse} from "axios";
import {NetworkDeviceListItem} from "../model/NetworkDeviceListItem";
import moment from "moment";

export class DeviceService {

    static getDeviceList() : Promise<AxiosResponse<Array<NetworkDeviceListItem>>> {
        return axios.get<Array<NetworkDeviceListItem>>("/api/device");
    }

    static downloadBackup() : void {
        // this will not work in development mode!
        window.location.href = "/api/device/backup";
    }

    static restoreBackup(file: File) {
        const formData = new FormData();
        formData.append("file", file);
        return axios.post("/api/device/restore", formData, { headers: {"Content-Type": "multipart/form-data"}});
    }

    static approveDevice(macAddress: string, hostname: string) : Promise<AxiosResponse<NetworkDeviceListItem>> {
        return axios.put<NetworkDeviceListItem>("/api/device/" + macAddress + "/approve", { hostname });
    }

    static deleteDevice(macAddress: string) : Promise<AxiosResponse<void, void>> {
        return axios.delete("/api/device/" + macAddress);
    }

    static lastSeen(timestamp: string) : string {
        if (timestamp === null) {
            return "never";
        }
        return moment(timestamp).fromNow();
    }
}
