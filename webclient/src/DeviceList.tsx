import {NetworkDeviceListItem} from "./model/NetworkDeviceListItem";
import {Paper, Table, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";
import React, {useState} from "react";
import {KnownDeviceRow} from "./KnownDeviceRow";
import {NewDeviceRow} from "./NewDeviceRow";
import axios from "axios";

interface DeviceListProps {
    deviceList: Array<NetworkDeviceListItem>;
}

export function DeviceList({deviceList : initialDeviceList} : DeviceListProps) {

    const [deviceList, setDeviceList] = useState(initialDeviceList);

    async function onDeviceApprove(macAddress: string, hostname: string) {
        const {data: device} = await axios.put<NetworkDeviceListItem>("/device/" + macAddress + "/approve", { hostname });
        setDeviceList(prevState => {
           return prevState.map(prevDevice => {
              if (prevDevice.macAddress == device.macAddress) {
                  return device;
              } else {
                  return prevDevice;
              }
           });
        });
    }

    async function onForgetDevice(macAddress: string) {
        await axios.delete("/device/" + macAddress);
        setDeviceList(prevState => {
            return prevState.filter(prevDevice => prevDevice.macAddress != macAddress);
        });
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
                    if (device.approved) return <KnownDeviceRow device={device} onForgetDevice={onForgetDevice}/>;
                    else return <NewDeviceRow device={device} onDeviceApprove={onDeviceApprove} onForgetDevice={onForgetDevice}/>;
                })
            }
        </Table>
    </TableContainer>;
}
