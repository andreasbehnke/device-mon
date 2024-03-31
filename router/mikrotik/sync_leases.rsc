# Synchronize all leases with device monitoring tool

# Configure device service
:local deviceServiceUrl;
:set deviceServiceUrl "http://[YOUR SERVICE IP]/api/device/synchronize-leases";

:local payload;
:local firstLease;
:local leaseHostName;

# Source: https://gist.github.com/SmartFinn/acc7953eaeb43cece034?permalink_comment_id=3561435#file-dhcp-leases-to-dns-rsc
# Normalize hostname (e.g. "-= My Phone =-" -> "My-Phone")
# - truncate length to 63 chars
# - substitute disallowed chars with a hyphen
# param: name
:local normalizeHostname do={
  :local result;
  :local isInvalidChar true;
  :for i from=0 to=([:len $name]-1) do={
    :local char [:pick $name $i];
    :if ($i < 63) do={
      :if ($char~"[a-zA-Z0-9]") do={
        :set result ($result . $char);
        :set isInvalidChar false;
      } else={
        :if (!$isInvalidChar) do={
          :set result ($result . "-");
          :set isInvalidChar true;
        };
      };
    };
  };
  # delete trailing hyphen
  :if ($isInvalidChar) do={
    :set result [:pick $result 0 ([:len $result]-1)];
  }
  :return $result;
};

# Collect all leases and build json payload
:set payload ("[");
:set firstLease (true);

/ip dhcp-server lease
:foreach lease in=[print as-value where status=bound] do={
   :if ($firstLease = false) do={ :set payload ( $payload .  ",")};
   :set firstLease (false);
   :set leaseHostName [$normalizeHostname name=($lease->"host-name")];
   :set payload ( $payload .  "{\"macAddress\":\"" . $lease->"mac-address" . "\",\"clientHostname\":\"" . $leaseHostName . "\",\"dhcpServerName\":\"" . $lease->"server" . "\",\"inet4Address\":\"" . $lease->"address" . "\",\"lastSeenAsString\":\"" . $lease->"last-seen" . "\"}" );
}
:set payload ($payload . "]");
:do {
   /tool fetch http-method=put check-certificate=no http-header-field="Content-Type: application/json" url=$deviceServiceUrl http-data=$payload;
   :log info ("synchronized leases with device monitor");
} on-error={
   :log error ("device monitor not reachable");
}
