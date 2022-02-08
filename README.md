# device-mon
This project is in early development.

Monitoring network device data of small networks.
This tool lists all devices which have been registered to your local network by DHCP
and supports providing individual host names to this devices. 

The goal of this service is to identify and name things on your network and give them
meaningful local domain names for easier setup of lots of small clients like IoT devices.
Mikrotik routers are supported by a lease script. 

*This is NOT a security feature - if you need a real secure network setup, you should setup
802.1x and RADIUS server.*

## Start from source

* cd backend
* docker-compose up
* ./mvnw spring-boot:run 

## Use router script
To notify device-mon about device DHCP sign on and sign off, you need to install a script to your router, for instance ths script `/router/mikrotik/on_add_lease.rsc`. 
Upload this script to your mikrotik server and activate it in the DHCP server settings (`/ip dhcp-server set lease-script=your-script.rst`).
You must configure your device-mon service IP address by replacing `[CONFIGURE SERVICE IP]`.  
