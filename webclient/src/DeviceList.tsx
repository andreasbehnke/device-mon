import {NetworkDeviceListItem} from "./model/NetworkDeviceListItem";
import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText, IconButton,
    Paper,
    Table,
    TableCell,
    TableContainer,
    TableHead,
    TableRow, Typography
} from "@mui/material";
import React, {useEffect, useState} from "react";
import {KnownDeviceRow} from "./KnownDeviceRow";
import {NewDeviceRow} from "./NewDeviceRow";
import {DeviceService} from "./service/DeviceService";
import {useSnackbar} from "notistack";
import AutorenewIcon from "@mui/icons-material/Autorenew";

interface DeviceListProps {
    deviceList: Array<NetworkDeviceListItem>;
}

export function DeviceList({deviceList : initialDeviceList} : DeviceListProps) {

    const [deviceList, setDeviceList] = useState(initialDeviceList);

    const [forgetMacAddress, setForgetMacAddress] = useState<string|null>(null);

    function handleConfirmForgetClose() {
        setForgetMacAddress(null);
    }

    const { enqueueSnackbar } = useSnackbar();

    async function onDeviceApprove(macAddress: string, hostname: string) {
        try {
            const {data: device} = await DeviceService.approveDevice(macAddress, hostname);
            setDeviceList(prevState => {
                return prevState.map(prevDevice => {
                    if (prevDevice.macAddress === device.macAddress) {
                        return device;
                    } else {
                        return prevDevice;
                    }
                });
            });
            enqueueSnackbar("Approved device " + macAddress + " with name " + hostname, {variant: "info"});
        } catch (e) {
            enqueueSnackbar("Could not approve device", {variant: "error"});
        }
    }

    async function forgetDevice() {
        if (forgetMacAddress != null) {
            try {
                await DeviceService.deleteDevice(forgetMacAddress);
                setDeviceList(prevState => {
                    return prevState.filter(prevDevice => prevDevice.macAddress !== forgetMacAddress);
                });
                enqueueSnackbar("Removed device " + forgetMacAddress, {variant: "info"});
            } catch (e) {
                enqueueSnackbar("Could not forget device", {variant: "error"});
            } finally {
                setForgetMacAddress(null);
            }
        }
    }

    function onForgetDevice(macAddress: string) {
        setForgetMacAddress(macAddress);
    }

    async function onListRefresh() {
        try {
            const {data: list} = await DeviceService.getDeviceList();
            setDeviceList(list);
        } catch (e) {
            enqueueSnackbar("Could not refresh device list", {variant: "error"});
        }
    }

    useEffect(() => {
        const timer = setTimeout(async() => {
            await onListRefresh();
        }, 10000);
        return () => {
            clearTimeout(timer);
        }
    })

    const stats = DeviceService.getDeviceListStats(deviceList);

    return (
        <>
            <Typography variant={"caption"}>{deviceList.length} devices, {stats.activeHosts} active, {stats.inactiveHosts} inactive</Typography>
            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell />
                            <TableCell sx={{display: ["none", "none", "table-cell"]}}>MAC</TableCell>
                            <TableCell sx={{display: ["none", "none", "none", "none", "table-cell"]}}>Vendor</TableCell>
                            <TableCell>IPv4</TableCell>
                            <TableCell sx={{display: ["none", "none", "none", "table-cell"]}}>DHCP Server</TableCell>
                            <TableCell sx={{display: ["none", "table-cell"]}}>Last seen</TableCell>
                            <TableCell>Host name</TableCell>
                            <TableCell  align={"right"}><IconButton aria-label="Refresh list" title={"Refresh list"} onClick={onListRefresh}><AutorenewIcon /></IconButton></TableCell>
                        </TableRow>
                    </TableHead>
                    {
                        deviceList.map((device) => {
                            if (device.approved) return <KnownDeviceRow key={device.macAddress} device={device} onForgetDevice={onForgetDevice}/>;
                            else return <NewDeviceRow key={device.macAddress} device={device} onDeviceApprove={onDeviceApprove} onForgetDevice={onForgetDevice}/>;
                        })
                    }
                </Table>
            </TableContainer>
            <Dialog open={forgetMacAddress != null} onClose={handleConfirmForgetClose}>
                <DialogContent>
                    <DialogContentText>Do you really want to forget device ?</DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleConfirmForgetClose} color={"inherit"}>Cancel</Button>
                    <Button onClick={forgetDevice} autoFocus color={"primary"}>Forget device</Button>
                </DialogActions>
            </Dialog>
        </>
    );
}
