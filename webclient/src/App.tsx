import React from 'react';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import {
    IconButton,
    Paper,
    Table,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    TextField,
    Typography
} from "@mui/material";
import '@fontsource/roboto/300.css';
import '@fontsource/roboto/400.css';
import '@fontsource/roboto/500.css';
import '@fontsource/roboto/700.css';

function App() {
  return (
      <>
          <Typography variant={"h5"} gutterBottom>Network Device Monitor</Typography>
          <TableContainer component={Paper}>
              <Table>
                  <TableHead>
                      <TableRow>
                          <TableCell>MAC</TableCell>
                          <TableCell>IPv4</TableCell>
                          <TableCell>DHCP Server</TableCell>
                          <TableCell>last seen</TableCell>
                          <TableCell>host name</TableCell>
                          <TableCell />
                      </TableRow>
                  </TableHead>
                  <TableRow>
                      <TableCell>64:A2:F9:74:62:BF</TableCell>
                      <TableCell>192.168.50.29</TableCell>
                      <TableCell>dhcp-untrusted</TableCell>
                      <TableCell>2022-02-07 20:20:29.018+00</TableCell>
                      <TableCell><TextField variant={"standard"} value={"Galaxy-Tab-S5e"}></TextField></TableCell>
                      <TableCell>
                          <IconButton aria-label="approve">
                            <AddIcon />
                          </IconButton>
                      </TableCell>
                  </TableRow>
                  <TableRow>
                      <TableCell>64:A2:F9:74:62:BF</TableCell>
                      <TableCell>192.168.50.29</TableCell>
                      <TableCell>dhcp-untrusted</TableCell>
                      <TableCell>2022-02-07 20:20:29.018+00</TableCell>
                      <TableCell>Galaxy-Tab-S5e</TableCell>
                      <TableCell>
                          <IconButton aria-label="forget">
                              <DeleteIcon />
                          </IconButton>
                      </TableCell>
                  </TableRow>
              </Table>
          </TableContainer>
      </>
  );
}

export default App;
