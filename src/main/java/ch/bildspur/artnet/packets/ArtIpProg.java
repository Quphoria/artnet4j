/*
 * This file is part of artnet4j.
 * 
 * Copyright 2009 Karsten Schmidt (PostSpectacular Ltd.)
 * 
 * artnet4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * artnet4j is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with artnet4j. If not, see <http://www.gnu.org/licenses/>.
 */

package ch.bildspur.artnet.packets;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;

public class ArtIpProg extends ArtNetPacket {

    protected static int MAX_LENGTH = HEADER_LENGTH+22;

    private boolean enableAnyProgramming = false;
    private boolean enableDHCP = false;
    private boolean programDefaultGateway = false;
    private boolean programDefaults = false;
    private boolean programIPAddress = false;
    private boolean programSubnetMask = false;
    private boolean programPort = false;

    private InetAddress ip;
    private InetAddress subnetMask;
    private int port = 0x1936;
    private InetAddress defaultGateway;

    public ArtIpProg() {
        super(PacketType.ART_IP_PROG);
    }

    public boolean isProgrammingEnabled() {
        return enableAnyProgramming;
    }

    public boolean isProgramEnableDHCP() {
        if (!enableAnyProgramming) return false;
        return enableDHCP;
    }

    public boolean isProgramDefaults() {
        if (!enableAnyProgramming) return false;
        if (enableDHCP) return false;
        return programDefaults;
    }

    public boolean isProgramDefaultGateway() {
        if (!enableAnyProgramming) return false;
        if (enableDHCP || programDefaults) return false;
        return programDefaultGateway;
    }

    public boolean isProgramIPAddress() {
        if (!enableAnyProgramming) return false;
        if (enableDHCP || programDefaults) return false;
        return programIPAddress;
    }

    public boolean isProgramSubnetMask() {
        if (!enableAnyProgramming) return false;
        if (enableDHCP || programDefaults) return false;
        return programSubnetMask;
    }

    @Deprecated
    public boolean isProgramPort() {
        if (!enableAnyProgramming) return false;
        if (enableDHCP || programDefaults) return false;
        return programPort;
    }

    public void setProgrammingEnabled(boolean enable) {
        enableAnyProgramming = enable;
    }

    public void setProgramEnableDHCP(boolean enable) {
        enableDHCP = enable;
    }

    public void setProgramDefaults(boolean program) {
        programDefaults = program;
    }

    public void setProgramDefaultGateway(boolean program) {
        programDefaultGateway = program;
    }

    public void setProgramIPAddress(boolean program) {
        programIPAddress = program;
    }

    public void setProgramSubnetMask(boolean program) {
        programSubnetMask = program;
    }

    @Deprecated
    public void setProgramPort(boolean program) {
        programPort = program;
    }

    public InetAddress getIp() {
        InetAddress ipClone = null;
        try {
            ipClone = InetAddress.getByAddress(ip.getAddress());
        } catch (UnknownHostException e) {
        }
        return ipClone;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    private void setIp(byte[] address) {
        try {
            ip = InetAddress.getByAddress(address);
        } catch (UnknownHostException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    public InetAddress getDefaultGateway() {
        InetAddress ipClone = null;
        try {
            ipClone = InetAddress.getByAddress(defaultGateway.getAddress());
        } catch (UnknownHostException e) {
        }
        return ipClone;
    }

    public void setDefaultGateway(InetAddress defaultGateway) {
        this.defaultGateway = defaultGateway;
    }

    private void setDefaultGateway(byte[] address) {
        try {
            defaultGateway = InetAddress.getByAddress(address);
        } catch (UnknownHostException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    public InetAddress getSubnetMask() {
        InetAddress ipClone = null;
        try {
            ipClone = InetAddress.getByAddress(subnetMask.getAddress());
        } catch (UnknownHostException e) {
        }
        return ipClone;
    }

    public void setSubnetMask(InetAddress subnetMask) {
        this.subnetMask = subnetMask;
    }

    private void setSubnetMask(byte[] address) {
        try {
            subnetMask = InetAddress.getByAddress(address);
        } catch (UnknownHostException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    @Deprecated
    public int getPort() {
        return port;
    }

    @Deprecated
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean parse(byte[] raw) {
        if (raw.length < MAX_LENGTH) return false;
        setData(raw, MAX_LENGTH); // Set buffer size to MAX_LENGTH to fill extra fields with zeros
        int flags = data.getInt8(14);
        enableAnyProgramming    = (flags & (1<<7)) != 0;
        enableDHCP              = (flags & (1<<6)) != 0;
        programDefaultGateway   = (flags & (1<<4)) != 0;
        programDefaults         = (flags & (1<<3)) != 0;
        programIPAddress        = (flags & (1<<2)) != 0;
        programSubnetMask       = (flags & (1<<1)) != 0;
        programPort             = (flags & (1<<0)) != 0;

        setIp(data.getByteChunk(null, 16, 4));
        setSubnetMask(data.getByteChunk(null, 20, 4));
        port = data.getInt16(24);
        setDefaultGateway(data.getByteChunk(null, 26, 4));
        
        return true;
    }

    @Override
    public void serializeData() {
        super.serializeData();
        data.setInt8(
            (enableAnyProgramming ?     (1<<7) : 0) |
            (enableDHCP ?               (1<<6) : 0) |
            (programDefaultGateway ?    (1<<4) : 0) |
            (programDefaults ?          (1<<3) : 0) |
            (programIPAddress ?         (1<<2) : 0) |
            (programSubnetMask ?        (1<<1) : 0) |
            (programPort ?              (1<<0) : 0),
        14);

        data.setByteChunk(ip.getAddress(), 16, Math.min(4, ip.getAddress().length));
        data.setByteChunk(subnetMask.getAddress(), 20, Math.min(4, subnetMask.getAddress().length));
        data.setInt16(port, 24);
        data.setByteChunk(defaultGateway.getAddress(), 26, Math.min(4, defaultGateway.getAddress().length));
    }
}
