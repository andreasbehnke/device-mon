import {NetworkDeviceListItem} from "./model/NetworkDeviceListItem";
import {Paper, Table, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";
import React from "react";
import {KnownDeviceRow} from "./KnownDeviceRow";
import {NewDeviceRow} from "./NewDeviceRow";

export function DeviceList(props: { deviceList: Array<NetworkDeviceListItem> }) {
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
                props.deviceList.map((device) => {
                    if (device.approved) return <KnownDeviceRow device={device}/>;
                    else return <NewDeviceRow device={device}/>;
                })
            }
        </Table>
    </TableContainer>;
}
