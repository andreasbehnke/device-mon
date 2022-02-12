import {NetworkDeviceListItem} from "./model/NetworkDeviceListItem";
import {Paper, Table, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";
import React, {useState} from "react";
import {KnownDeviceRow} from "./KnownDeviceRow";
import {NewDeviceRow} from "./NewDeviceRow";
import {DeviceService} from "./service/DeviceService";
import {useSnackbar} from "notistack";

interface DeviceListProps {
    deviceList: Array<NetworkDeviceListItem>;
}

export function DeviceList({deviceList : initialDeviceList} : DeviceListProps) {

    const [deviceList, setDeviceList] = useState(initialDeviceList);

    const { enqueueSnackbar } = useSnackbar();

    async function onDeviceApprove(macAddress: string, hostname: string) {
        try {
            const {data: device} = await DeviceService.approveDevice(macAddress, hostname);
            setDeviceList(prevState => {
                return prevState.map(prevDevice => {
                    if (prevDevice.macAddress == device.macAddress) {
                        return device;
                    } else {
                        return prevDevice;
                    }
                });
            });
            enqueueSnackbar("Approved device " + macAddress + " with name " + hostname, {variant: "info"});
        } catch (e) {
            enqueueSnackbar("Could not approve device", {variant: "error"});
        }
    }

    async function onForgetDevice(macAddress: string) {
        try {
            await DeviceService.deleteDevice(macAddress);
            setDeviceList(prevState => {
                return prevState.filter(prevDevice => prevDevice.macAddress != macAddress);
            });
            enqueueSnackbar("Removed device " + macAddress, {variant: "info"});
        } catch (e) {
            enqueueSnackbar("Could not forget device", {variant: "error"});
        }
    }

    return <TableContainer component={Paper} sx={{maxWidth: "1600px"}}>
        <Table>
            <TableHead>
                <TableRow>
                    <TableCell sx={{display: ["none", "none", "table-cell"]}}>MAC</TableCell>
                    <TableCell>IPv4</TableCell>
                    <TableCell sx={{display: ["none", "none", "none", "table-cell"]}}>DHCP Server</TableCell>
                    <TableCell sx={{display: ["none", "table-cell"]}}>Last seen</TableCell>
                    <TableCell>Host name</TableCell>
                    <TableCell/>
                </TableRow>
            </TableHead>
            {
                deviceList.map((device) => {
                    if (device.approved) return <KnownDeviceRow key={device.macAddress} device={device} onForgetDevice={onForgetDevice}/>;
                    else return <NewDeviceRow key={device.macAddress} device={device} onDeviceApprove={onDeviceApprove} onForgetDevice={onForgetDevice}/>;
                })
            }
        </Table>
    </TableContainer>;
}
