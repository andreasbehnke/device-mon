import React from "react";
import DownloadIcon from '@mui/icons-material/Download';
import {NetworkDeviceListItem} from "./model/NetworkDeviceListItem";
import {useAsync} from "react-async-hook";
import {DeviceList} from "./DeviceList";
import {Alert, Box, Grid, IconButton, Typography} from "@mui/material";
import {DeviceService} from "./service/DeviceService";
import {useSnackbar} from "notistack";

export function DeviceListPage() {

    async function loadDevices() : Promise<Array<NetworkDeviceListItem>> {
        const result = await DeviceService.getDeviceList();
        return result.data;
    }

    const { result, loading, error } = useAsync<Array<NetworkDeviceListItem>>(loadDevices, []);

    let deviceList;

    if (error) {
        deviceList = <Alert severity={"error"}>Could not load device list: {error.message}</Alert>;
    } else if (loading || !result) {
        deviceList = <></>;
    } else {
        deviceList = <DeviceList deviceList={result} />;
    }

    const { enqueueSnackbar } = useSnackbar();

    async function onDownloadBackup() {
        try {
            DeviceService.downloadBackup();
            enqueueSnackbar("Downloaded configuration backup", {variant: "success"});
        } catch (e) {
            enqueueSnackbar("Could not download configuration backup", {variant: "error"});
        }
    }

    return (
        <Box sx={{m: 3}}>
            <Grid container sx={{marginBottom: 2}}>
                <Grid item xs={11}><Typography variant={"h5"} gutterBottom>Network Devices</Typography></Grid>
                <Grid item xs={1} container alignItems={"flex-end"} direction="column">
                    <IconButton aria-label="Download configuration backup" title={"Download configuration backup"} onClick={onDownloadBackup} ><DownloadIcon /></IconButton>
                </Grid>
            </Grid>
            {deviceList}
        </Box>
    );
}
