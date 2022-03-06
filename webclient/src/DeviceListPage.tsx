import React, {ChangeEvent} from "react";
import DownloadIcon from '@mui/icons-material/Download';
import UploadIcon from '@mui/icons-material/Upload';
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

    const { result, loading, error, execute } = useAsync<Array<NetworkDeviceListItem>>(loadDevices, []);

    let deviceList;

    if (error) {
        deviceList = <Alert severity={"error"}>Could not load device list: {error.message}</Alert>;
    } else if (loading || !result) {
        deviceList = <></>;
    } else {
        deviceList = <DeviceList deviceList={result} />;
    }

    const { enqueueSnackbar } = useSnackbar();

    async function onFileChanged(event: ChangeEvent<HTMLInputElement>) {
        if (event.target.files && event.target.files.length > 0) {
            try {
                await DeviceService.restoreBackup(event.target.files[0]);
                await execute();
                enqueueSnackbar("Restored configuration backup", {variant: "success"});
            } catch (e) {
                enqueueSnackbar("Could not restore configuration backup", {variant: "error"});
            }
        }
    }

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
                <Grid item xs={8}><Typography variant={"h5"} gutterBottom>Network Devices</Typography></Grid>
                <Grid item xs={4} container justifyContent={"flex-end"}>
                    <input
                        id="upload-file"
                        type="file"
                        accept={".json"}
                        hidden
                        onChange={onFileChanged}
                    />
                    <label htmlFor="upload-file">
                        <IconButton component={"span"} aria-label="Restore configuration from backup" title={"Restore configuration from backup"} >
                            <UploadIcon />
                        </IconButton>
                    </label>
                    <IconButton aria-label="Download configuration backup" title={"Download configuration backup"} onClick={onDownloadBackup} ><DownloadIcon /></IconButton>
                </Grid>
            </Grid>
            {deviceList}
        </Box>
    );
}
