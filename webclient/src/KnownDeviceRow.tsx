import {IconButton, TableCell, TableRow} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import React from "react";
import moment from "moment";
import {NetworkDeviceListItem} from "./model/NetworkDeviceListItem";

export interface KnownDeviceRowProps {
    device: NetworkDeviceListItem,
    onForgetDevice: (macAddress: string) => void,
}

export function KnownDeviceRow({ device: { activeLease, macAddress, vendor, inet4Address, dhcpServerName, lastSeen, hostname }, onForgetDevice } : KnownDeviceRowProps) {

    return <TableRow sx={{backgroundColor: activeLease ? "inherit" : "#333" }}>
        <TableCell sx={{ display: ["none", "none", "table-cell"]}}>{macAddress}</TableCell>
        <TableCell sx={{display: ["none", "none", "none", "none", "table-cell"]}}>{vendor}</TableCell>
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
