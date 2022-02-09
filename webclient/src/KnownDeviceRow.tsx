import {IconButton, TableCell, TableRow} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import React from "react";
import moment from "moment";

export function KnownDeviceRow() {
    return <TableRow>
        <TableCell sx={{ display: ["none", "none", "table-cell"]}}>64:A2:F9:74:62:BF</TableCell>
        <TableCell>192.168.50.29</TableCell>
        <TableCell sx={{ display: ["none", "none", "none", "table-cell"]}}>dhcp-untrusted</TableCell>
        <TableCell sx={{ display: ["none", "table-cell"]}}>{moment('2022-02-03 20:20:29.018+00').fromNow()}</TableCell>
        <TableCell>Galaxy-Tab-S5e</TableCell>
        <TableCell>
            <IconButton aria-label="Forget device" title={"Forget device"}>
                <DeleteIcon/>
            </IconButton>
        </TableCell>
    </TableRow>;
}
