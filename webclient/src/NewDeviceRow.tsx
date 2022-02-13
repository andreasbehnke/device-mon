import {IconButton, TableCell, TableRow, TextField} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import React, {useState} from "react";
import moment from "moment";
import {NetworkDeviceListItem} from "./model/NetworkDeviceListItem";
import DeleteIcon from "@mui/icons-material/Delete";
import CircleIcon from "@mui/icons-material/Circle";

export interface NewDeviceRowProps {
    device: NetworkDeviceListItem,
    onDeviceApprove: (macAddress: string, hostname: string) => void,
    onForgetDevice: (macAddress: string) => void,
}

export function NewDeviceRow({ device: { activeLease, macAddress, vendor, inet4Address, dhcpServerName, lastSeen, hostname : initialHostname }, onDeviceApprove, onForgetDevice } : NewDeviceRowProps) {

    const [hostname, setHostname] = useState<string>(initialHostname);

    return <TableRow>
        <TableCell>
            <CircleIcon sx={{color: (activeLease) ? "darkgreen" : "darkred"}} />
        </TableCell>
        <TableCell sx={{ display: ["none", "none", "table-cell"]}}>{ macAddress }</TableCell>
        <TableCell sx={{display: ["none", "none", "none", "none", "table-cell"]}}>{vendor}</TableCell>
        <TableCell>{ inet4Address }</TableCell>
        <TableCell sx={{ display: ["none", "none", "none", "table-cell"]}}>{ dhcpServerName }</TableCell>
        <TableCell sx={{ display: ["none", "table-cell"]}}>{moment(lastSeen).fromNow()}</TableCell>
        <TableCell><TextField size={"small"} variant={"filled"} hiddenLabel value={hostname} onChange={event => setHostname(event.target.value)} /></TableCell>
        <TableCell align={"right"}>
            <IconButton aria-label="Approve device" title={"Approve device"} onClick={() => onDeviceApprove(macAddress, hostname)} color={"primary"}>
                <AddIcon color={"primary"}/>
            </IconButton>
            <IconButton aria-label="Forget device" title={"Forget device"} onClick={() => onForgetDevice(macAddress)}>
                <DeleteIcon/>
            </IconButton>
        </TableCell>
    </TableRow>;
}
