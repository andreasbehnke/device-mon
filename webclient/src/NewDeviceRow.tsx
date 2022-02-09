import {IconButton, TableCell, TableRow, TextField} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import React from "react";

export function NewDeviceRow() {
    return <TableRow>
        <TableCell>64:A2:F9:74:62:BF</TableCell>
        <TableCell>192.168.50.29</TableCell>
        <TableCell>dhcp-untrusted</TableCell>
        <TableCell>2022-02-07 20:20:29.018+00</TableCell>
        <TableCell><TextField variant={"standard"} value={"Galaxy-Tab-S5e"}></TextField></TableCell>
        <TableCell>
            <IconButton aria-label="approve device" title={"approve device"}>
                <AddIcon color={"primary"}/>
            </IconButton>
        </TableCell>
    </TableRow>;
}
