import {IconButton, TableCell, TableRow} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import React from "react";

export function KnownDeviceRow() {
    return <TableRow>
        <TableCell>64:A2:F9:74:62:BF</TableCell>
        <TableCell>192.168.50.29</TableCell>
        <TableCell>dhcp-untrusted</TableCell>
        <TableCell>2022-02-07 20:20:29.018+00</TableCell>
        <TableCell>Galaxy-Tab-S5e</TableCell>
        <TableCell>
            <IconButton aria-label="Forget device" title={"Forget device"}>
                <DeleteIcon/>
            </IconButton>
        </TableCell>
    </TableRow>;
}
