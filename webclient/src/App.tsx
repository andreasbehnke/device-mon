import React from 'react';
import {Paper, Table, TableCell, TableContainer, TableHead, TableRow, Typography} from "@mui/material";
import '@fontsource/roboto/300.css';
import '@fontsource/roboto/400.css';
import '@fontsource/roboto/500.css';
import '@fontsource/roboto/700.css';
import {KnownDeviceRow} from "./KnownDeviceRow";
import {NewDeviceRow} from "./NewDeviceRow";

class App extends React.Component {
    render() {
        return (
            <>
                <Typography variant={"h5"} gutterBottom>Network Device Monitor</Typography>
                <TableContainer component={Paper} sx={{maxWidth: "1600px"}}>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell sx={{ display: ["none", "none", "table-cell"]}}>MAC</TableCell>
                                <TableCell>IPv4</TableCell>
                                <TableCell sx={{ display: ["none", "none", "none", "table-cell"]}}>DHCP Server</TableCell>
                                <TableCell sx={{ display: ["none", "table-cell"]}}>Last seen</TableCell>
                                <TableCell>Host name</TableCell>
                                <TableCell/>
                            </TableRow>
                        </TableHead>
                        <NewDeviceRow/>
                        <KnownDeviceRow/>
                    </Table>
                </TableContainer>
            </>
        );
    }
}

export default App;
