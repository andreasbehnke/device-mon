import React from 'react';
import {Box, createTheme, CssBaseline, ThemeProvider, Typography} from "@mui/material";
import '@fontsource/roboto/300.css';
import '@fontsource/roboto/400.css';
import '@fontsource/roboto/500.css';
import '@fontsource/roboto/700.css';
import {DeviceListLoader} from "./DeviceListLoader";
import {SnackbarProvider} from "notistack";

class App extends React.Component {
    theme = createTheme({palette: {mode: "dark"}});
    render() {
        return (
            <ThemeProvider theme={this.theme}>
                <SnackbarProvider maxSnack={2} anchorOrigin={{vertical: "top", horizontal: "right"}}>
                    <CssBaseline />
                    <Box sx={{m: 3}}>
                        <Typography variant={"h4"} gutterBottom>Network Device Monitor</Typography>
                        <DeviceListLoader/>
                    </Box>
                </SnackbarProvider>
            </ThemeProvider>
        );
    }
}

export default App;
