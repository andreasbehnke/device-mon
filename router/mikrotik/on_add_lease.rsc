# DNS record for DHCP lease

:local topdomain;
:local LeaseHostName;
:local FullHostName;
:local HostNameFromMAC;
:local NoUpdate false;

:local deviceServiceUrl;
:local currentUrl;
:local payload;
:local result;

# Configure device service
:set deviceServiceUrl "http://[YOUR SERVICE IP]/api";

# Configure your domain
:set topdomain "[YOUR OWN TOP LEVEL DOMAIN]";

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
:set LeaseHostName [$normalizeHostname name=$"lease-hostname"];
:log info ("normalized hostname:". $LeaseHostName);

:if ($LeaseHostName = "") do={
  :for i from=0 to=([:len $"leaseActMAC"] - 1) do={ 
    :local char [:pick $"leaseActMAC" $i];
    :if ($char = ":") do={
      :set $char "-";
    }
    :set HostNameFromMAC ($HostNameFromMAC . $char);
      
  }
  :set FullHostName ($HostNameFromMAC . "." . $topdomain);
} else= {
  :set FullHostName ($LeaseHostName . "." . $topdomain);
}

/ip dns static;

:if ($leaseBound = 1) do={
  # notify device monitor about sign-on and try to retrieve hostname
  :set payload ("{\"dhcpServerName\":\"".$leaseServerName."\",\"macAddress\":\"".$leaseActMAC."\",\"inet4Address\":\"".$leaseActIP."\",\"clientHostname\":\"".$LeaseHostName."\"}");
  :set currentUrl ($deviceServiceUrl."/device/sign-on");
  :do {
   :set result ([/tool fetch http-method=put check-certificate=no http-header-field="Content-Type: application/json" url=$currentUrl http-data=$payload as-value output=user]);
   :log info ("received host name from device monitoring:". $result->"data");
   :set FullHostName ($result->"data" . "." . $topdomain);
  } on-error={
   :log error ("device monitor not reachable");
  }

  :foreach n in [find] do={
    # If a static DNS entry is the same as the lease then leave it and set a flag to avoid DNS Update
    :if (([get $n name] = $"FullHostName") and ([get $n address] = $leaseActIP)) do={
      :set NoUpdate true;
    } else={
      # If some DNS entry with same fully qualified domain name or same address already exist remove it
      :if (([get $n name] = $"FullHostName") or ([get $n address] = $leaseActIP)) do={
        :log info ("Removing from Static DNS : " . [get $n name] .  " @ " . [get $n address]);
        remove $n;
      }
    }
  }
  # Add new Static DNS Entry if necessary
  :if ($NoUpdate = false) do={
    :log info ("Adding to Static DNS : " . $"FullHostName" .  " @ " . $leaseActIP);
    add name=$"FullHostName" address=$leaseActIP ttl=6m;
  }
# remove DNS entry at DHCP release :
} else= {
  :log info ("Clear static DNS entry at DHCP Release : " . $"FullHostName" . " @ " . $leaseActIP);
  :foreach n in [find] do={
    :if (([get $n name] = $"FullHostName") and ([get $n address] = $leaseActIP)) do={
      remove $n;
    }
  }
  # notify device monitor about sign-off
  :do {
    :set currentUrl ($deviceServiceUrl."/device/sign-off/".$leaseActMAC);
    /tool fetch http-method=put check-certificate=no url=$currentUrl output=user;
    :log info ("send sign-off to device monitor");
  } on-error={
    :log error ("device monitor not reachable");
  }
}