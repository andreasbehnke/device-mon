import React from 'react';
import {Typography} from "@mui/material";
import '@fontsource/roboto/300.css';
import '@fontsource/roboto/400.css';
import '@fontsource/roboto/500.css';
import '@fontsource/roboto/700.css';
import {DeviceListLoader} from "./DeviceListLoader";

class App extends React.Component {
    render() {
        return (
            <>
                <Typography variant={"h5"} gutterBottom>Network Device Monitor</Typography>
                <DeviceListLoader/>
            </>
        );
    }
}

export default App;
