import {IconButton, TableCell, TableRow, TextField} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import React, {useState} from "react";
import moment from "moment";
import {DeviceRowProps} from "./DeviceRowProps";

export function NewDeviceRow({ device: { macAddress, inet4Address, dhcpServerName, lastSeen, hostname : initialHostname } } : DeviceRowProps) {

    const [hostname, setHostname] = useState<string>(initialHostname);

    return <TableRow key={macAddress}>
        <TableCell sx={{ display: ["none", "none", "table-cell"]}}>{ macAddress }</TableCell>
        <TableCell>{ inet4Address }</TableCell>
        <TableCell sx={{ display: ["none", "none", "none", "table-cell"]}}>{ dhcpServerName }</TableCell>
        <TableCell sx={{ display: ["none", "table-cell"]}}>{moment(lastSeen).fromNow()}</TableCell>
        <TableCell><TextField variant={"standard"} value={hostname} onChange={event => setHostname(event.target.value)} /></TableCell>
        <TableCell>
            <IconButton aria-label="Approve device" title={"Approve device"}>
                <AddIcon color={"primary"}/>
            </IconButton>
        </TableCell>
    </TableRow>;
}
