/*
 * ZeroTier One - Network Virtualization Everywhere
 * Copyright (C) 2011-2015  ZeroTier, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * --
 *
 * ZeroTier may be used and distributed under the terms of the GPLv3, which
 * are available at: http://www.gnu.org/licenses/gpl-3.0.html
 *
 * If you would like to embed ZeroTier into a commercial application or
 * redistribute it in a modified binary form, please contact ZeroTier Networks
 * LLC. Start here: http://www.zerotier.com/
 */

package com.zerotier.sdk;

import android.util.Log;

import com.zerotier.sdk.util.StringUtils;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Virtual network configuration
 *
 * Defined in ZeroTierOne.h as ZT_VirtualNetworkConfig
 */
public class VirtualNetworkConfig implements Comparable<VirtualNetworkConfig> {

    private final static String TAG = "VirtualNetworkConfig";

    public static final int MAX_MULTICAST_SUBSCRIPTIONS = 4096;
    public static final int ZT_MAX_ZT_ASSIGNED_ADDRESSES = 16;

    private final long nwid;

    private final long mac;

    private final String name;

    private final VirtualNetworkStatus status;

    private final VirtualNetworkType type;

    private final int mtu;

    private final boolean dhcp;

    private final boolean bridge;

    private final boolean broadcastEnabled;

    private final int portError;

    private final long netconfRevision;

    private final InetSocketAddress[] assignedAddresses;

    private final VirtualNetworkRoute[] routes;

    private final VirtualNetworkDNS dns;

    public VirtualNetworkConfig(long nwid, long mac, String name, VirtualNetworkStatus status, VirtualNetworkType type, int mtu, boolean dhcp, boolean bridge, boolean broadcastEnabled, int portError, long netconfRevision, InetSocketAddress[] assignedAddresses, VirtualNetworkRoute[] routes, VirtualNetworkDNS dns) {
        this.nwid = nwid;
        this.mac = mac;
        this.name = name;
        this.status = status;
        this.type = type;
        if (mtu < 0) {
            throw new RuntimeException("mtu < 0: " + mtu);
        }
        this.mtu = mtu;
        this.dhcp = dhcp;
        this.bridge = bridge;
        this.broadcastEnabled = broadcastEnabled;
        this.portError = portError;
        if (netconfRevision < 0) {
            throw new RuntimeException("netconfRevision < 0: " + netconfRevision);
        }
        this.netconfRevision = netconfRevision;
        this.assignedAddresses = assignedAddresses;
        this.routes = routes;
        this.dns = dns;
    }

    @Override
    public String toString() {
        return "VirtualNetworkConfig(" + StringUtils.networkIdToString(nwid) + ", " + StringUtils.macAddressToString(mac) + ", " + name + ", " + status + ", " + type + ", " + mtu + ", " + dhcp + ", " + bridge + ", " + broadcastEnabled + ", " + portError + ", " + netconfRevision + ", " + Arrays.toString(assignedAddresses) + ", " + Arrays.toString(routes) + ", " + dns + ")";
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof VirtualNetworkConfig)) {
            return false;
        }

        VirtualNetworkConfig cfg = (VirtualNetworkConfig) o;

        if (this.nwid != cfg.nwid) {
            Log.i(TAG, "NetworkID Changed. Old: " + StringUtils.networkIdToString(this.nwid) + " (" + this.nwid + "), " +
                    "New: " + StringUtils.networkIdToString(cfg.nwid) + " (" + cfg.nwid + ")");

            return false;
        }

        if (this.mac != cfg.mac) {
            Log.i(TAG, "MAC Changed. Old: " + StringUtils.macAddressToString(this.mac) + ", New: " + StringUtils.macAddressToString(cfg.mac));

            return false;
        }

        if (!this.name.equals(cfg.name)) {
            Log.i(TAG, "Name Changed. Old: " + this.name + ", New: " + cfg.name);

            return false;
        }

        if (this.status != cfg.status) {
            Log.i(TAG, "Status Changed. Old: " + this.status + ", New: " + cfg.status);

            return false;
        }

        if (this.type != cfg.type) {
            Log.i(TAG, "Type changed. Old " + this.type + ", New: " + cfg.type);

            return false;
        }

        if (this.mtu != cfg.mtu) {
            Log.i(TAG, "MTU Changed. Old: " + this.mtu + ", New: " + cfg.mtu);

            return false;
        }

        if (this.dhcp != cfg.dhcp) {
            Log.i(TAG, "DHCP Flag Changed. Old: " + this.dhcp + ", New: " + cfg.dhcp);

            return false;
        }

        if (this.bridge != cfg.bridge) {
            Log.i(TAG, "Bridge Flag Changed. Old: " + this.bridge + ", New: " + cfg.bridge);

            return false;
        }

        if (this.broadcastEnabled != cfg.broadcastEnabled) {
            Log.i(TAG, "Broadcast Flag Changed. Old: "+ this.broadcastEnabled + ", New: " + cfg.broadcastEnabled);

            return false;
        }

        if (this.portError != cfg.portError) {
            Log.i(TAG, "Port Error Changed. Old: " + this.portError + ", New: " + cfg.portError);

            return false;
        }

        if (this.netconfRevision != cfg.netconfRevision) {
            Log.i(TAG, "NetConfRevision Changed. Old: " + this.netconfRevision + ", New: " + cfg.netconfRevision);

            return false;
        }

        if (!Arrays.equals(assignedAddresses, cfg.assignedAddresses)) {

            ArrayList<String> aaCurrent = new ArrayList<>();
            ArrayList<String> aaNew = new ArrayList<>();
            for (InetSocketAddress s : assignedAddresses) {
                aaCurrent.add(s.toString());
            }
            for (InetSocketAddress s : cfg.assignedAddresses) {
                aaNew.add(s.toString());
            }
            Collections.sort(aaCurrent);
            Collections.sort(aaNew);

            Log.i(TAG, "Assigned Addresses Changed");
            Log.i(TAG, "Old:");
            for (String s : aaCurrent) {
                Log.i(TAG, "    " + s);
            }
            Log.i(TAG, "");
            Log.i(TAG, "New:");
            for (String s : aaNew) {
                Log.i(TAG, "    " +s);
            }
            Log.i(TAG, "");

            return false;
        }

        if (!Arrays.equals(routes, cfg.routes)) {

            ArrayList<String> rCurrent = new ArrayList<>();
            ArrayList<String> rNew = new ArrayList<>();
            for (VirtualNetworkRoute r : routes) {
                rCurrent.add(r.toString());
            }
            for (VirtualNetworkRoute r : cfg.routes) {
                rNew.add(r.toString());
            }
            Collections.sort(rCurrent);
            Collections.sort(rNew);

            Log.i(TAG, "Managed Routes Changed");
            Log.i(TAG, "Old:");
            for (String s : rCurrent) {
                Log.i(TAG, "    " + s);
            }
            Log.i(TAG, "");
            Log.i(TAG, "New:");
            for (String s : rNew) {
                Log.i(TAG, "    " + s);
            }
            Log.i(TAG, "");

            return false;
        }

        boolean dnsEquals;
        if (this.dns == null) {
            //noinspection RedundantIfStatement
            if (cfg.dns == null) {
                dnsEquals = true;
            } else {
                dnsEquals = false;
            }
        } else {
            if (cfg.dns == null) {
                dnsEquals = false;
            } else {
                dnsEquals = this.dns.equals(cfg.dns);
            }
        }

        if (!dnsEquals) {
            return false;
        }

        return true;
    }

    @Override
    public int compareTo(VirtualNetworkConfig cfg) {
        return Long.compare(this.nwid, cfg.nwid);
    }

    @Override
    public int hashCode() {

        int result = 17;
        result = 37 * result + (int) (nwid ^ (nwid >>> 32));
        result = 37 * result + (int) (mac ^ (mac >>> 32));
        result = 37 * result + name.hashCode();
        result = 37 * result + status.hashCode();
        result = 37 * result + type.hashCode();
        result = 37 * result + mtu;
        result = 37 * result + (dhcp ? 1 : 0);
        result = 37 * result + (bridge ? 1 : 0);
        result = 37 * result + (broadcastEnabled ? 1 : 0);
        result = 37 * result + portError;
        result = 37 * result + (int) (netconfRevision ^ (netconfRevision >>> 32));
        result = 37 * result + Arrays.hashCode(assignedAddresses);
        result = 37 * result + Arrays.hashCode(routes);
        result = 37 * result + (dns == null ? 0 : dns.hashCode());

        return result;
    }

    /**
     * 64-bit ZeroTier network ID
     */
    public long getNwid() {
        return nwid;
    }

    /**
     * Ethernet MAC (48 bits) that should be assigned to port
     */
    public long getMac() {
        return mac;
    }

    /**
     * Network name (from network configuration master)
     */
    public String getName() {
        return name;
    }

    /**
     * Network configuration request status
     */
    public VirtualNetworkStatus getStatus() {
        return status;
    }

    /**
     * Network type
     */
    public VirtualNetworkType getType() {
        return type;
    }

    /**
     * Maximum interface MTU
     */
    public int getMtu() {
        return mtu;
    }

    /**
     * If the network this port belongs to indicates DHCP availability
     *
     * <p>This is a suggestion. The underlying implementation is free to ignore it
     * for security or other reasons. This is simply a netconf parameter that
     * means 'DHCP is available on this network.'</p>
     */
    public boolean isDhcp() {
        return dhcp;
    }

    /**
     * If this port is allowed to bridge to other networks
     *
     * <p>This is informational. If this is false, bridged packets will simply
     * be dropped and bridging won't work.</p>
     */
    public boolean isBridge() {
        return bridge;
    }

    /**
     * If true, this network supports and allows broadcast (ff:ff:ff:ff:ff:ff) traffic
     */
    public boolean isBroadcastEnabled() {
        return broadcastEnabled;
    }

    /**
     * If the network is in PORT_ERROR state, this is the error most recently returned by the port config callback
     */
    public int getPortError() {
        return portError;
    }

    /**
     * Network config revision as reported by netconf master
     *
     * <p>If this is zero, it means we're still waiting for our netconf.</p>
     */
    public long getNetconfRevision() {
        return netconfRevision;
    }

    /**
     * ZeroTier-assigned addresses (in {@link InetSocketAddress} objects)
     *
     * For IP, the port number of the sockaddr_XX structure contains the number
     * of bits in the address netmask. Only the IP address and port are used.
     * Other fields like interface number can be ignored.
     *
     * This is only used for ZeroTier-managed address assignments sent by the
     * virtual network's configuration master.
     */
    public InetSocketAddress[] getAssignedAddresses() {
        return assignedAddresses;
    }

    /**
     * ZeroTier-assigned routes (in {@link VirtualNetworkRoute} objects)
     */
    public VirtualNetworkRoute[] getRoutes() {
        return routes;
    }

    /**
     * Network specific DNS configuration
     */
    public VirtualNetworkDNS getDns() {
        return dns;
    }
}
