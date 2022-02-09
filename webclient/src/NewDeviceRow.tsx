import {IconButton, TableCell, TableRow, TextField} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import React from "react";
import moment from "moment";

export function NewDeviceRow() {
    return <TableRow>
        <TableCell sx={{ display: ["none", "none", "table-cell"]}}>64:A2:F9:74:62:BF</TableCell>
        <TableCell>192.168.50.29</TableCell>
        <TableCell sx={{ display: ["none", "none", "none", "table-cell"]}}>dhcp-untrusted</TableCell>
        <TableCell sx={{ display: ["none", "table-cell"]}}>{moment('2022-02-07 20:20:29.018+00').fromNow()}</TableCell>
        <TableCell><TextField variant={"standard"} value={"Galaxy-Tab-S5e"} /></TableCell>
        <TableCell>
            <IconButton aria-label="Approve device" title={"Approve device"}>
                <AddIcon color={"primary"}/>
            </IconButton>
        </TableCell>
    </TableRow>;
}