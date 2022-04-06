# Synchronize all leases with device monitoring tool

:local deviceServiceUrl;
:local payload;
:local firstLease;

# Configure device service
:set deviceServiceUrl "http://[YOUR SERVICE IP]/api/device/synchronize-leases";

# Collect all leases and build json payload
:set payload ("[");
:set firstLease (true);

/ip dhcp-server lease
:foreach lease in=[print as-value where status=bound] do={
   :if ($firstLease = false) do={ :set payload ( $payload .  ",")};
   :set firstLease (false);
   :set payload ( $payload .  "{\"macAddress\":\"" . $lease->"mac-address" . "\",\"clientHostname\":\"" . $lease->"host-name" . "\",\"dhcpServerName\":\"" . $lease->"server" . "\",\"inet4Address\":\"" . $lease->"address" . "\",\"lastSeenAsString\":\"" . $lease->"last-seen" . "\"}" );
}
:set payload ($payload . "]");
:do {
   /tool fetch http-method=put check-certificate=no http-header-field="Content-Type: application/json" url=$deviceServiceUrl http-data=$payload;
   :log info ("synchronized leases with device monitor");
} on-error={
   :log error ("device monitor not reachable");
}
