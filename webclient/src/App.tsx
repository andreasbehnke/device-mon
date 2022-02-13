import React from 'react';
import {createTheme, CssBaseline, ThemeProvider} from "@mui/material";
import '@fontsource/roboto/300.css';
import '@fontsource/roboto/400.css';
import '@fontsource/roboto/500.css';
import '@fontsource/roboto/700.css';
import {DeviceListPage} from "./DeviceListPage";
import {SnackbarProvider} from "notistack";

class App extends React.Component {
    theme = createTheme({palette: {mode: "dark"}});
    render() {
        return (
            <ThemeProvider theme={this.theme}>
                <SnackbarProvider autoHideDuration={5000}>
                    <CssBaseline />
                    <DeviceListPage />
                </SnackbarProvider>
            </ThemeProvider>
        );
    }
}

export default App;
