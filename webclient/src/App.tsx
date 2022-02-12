import React from 'react';
import {Box, createTheme, CssBaseline, ThemeProvider, Typography} from "@mui/material";
import '@fontsource/roboto/300.css';
import '@fontsource/roboto/400.css';
import '@fontsource/roboto/500.css';
import '@fontsource/roboto/700.css';
import {DeviceListLoader} from "./DeviceListLoader";

class App extends React.Component {
    theme = createTheme({palette: {mode: "dark"}});
    render() {
        return (
            <ThemeProvider theme={this.theme}>
                <CssBaseline />
                <Box sx={{m: 3}}>
                    <Typography variant={"h4"} gutterBottom>Network Device Monitor</Typography>
                    <DeviceListLoader/>
                </Box>
            </ThemeProvider>
        );
    }
}

export default App;
