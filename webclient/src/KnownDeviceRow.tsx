import {IconButton, TableCell, TableRow} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import React from "react";
import moment from "moment";
import {NetworkDeviceListItem} from "./model/NetworkDeviceListItem";

export interface KnownDeviceRowProps {
    device: NetworkDeviceListItem,
    onForgetDevice: (macAddress: string) => void,
}

export function KnownDeviceRow({ device: { macAddress, inet4Address, dhcpServerName, lastSeen, hostname }, onForgetDevice } : KnownDeviceRowProps) {
    return <TableRow>
        <TableCell sx={{ display: ["none", "none", "table-cell"]}}>{macAddress}</TableCell>
        <TableCell>{inet4Address}</TableCell>
        <TableCell sx={{ display: ["none", "none", "none", "table-cell"]}}>{dhcpServerName}</TableCell>
        <TableCell sx={{ display: ["none", "table-cell"]}}>{moment(lastSeen).fromNow()}</TableCell>
        <TableCell>{hostname}</TableCell>
        <TableCell align={"right"}>
            <IconButton aria-label="Forget device" title={"Forget device"} onClick={() => onForgetDevice(macAddress)}>
                <DeleteIcon/>
            </IconButton>
        </TableCell>
    </TableRow>;
}
