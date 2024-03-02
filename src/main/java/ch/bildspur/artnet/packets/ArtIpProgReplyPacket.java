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

import ch.bildspur.artnet.ArtNetServer;

public class ArtIpProgReplyPacket extends ArtNetPacket {

    protected static int MAX_LENGTH = HEADER_LENGTH+22;

    private boolean dhcpEnabled = false;

    private InetAddress ip;
    private InetAddress subnetMask;
    private int port = ArtNetServer.DEFAULT_PORT;
    private InetAddress defaultGateway;

    public ArtIpProgReplyPacket() {
        super(PacketType.ART_IP_PROG_REPLY);
    }

    public boolean isDHCPEnabled() {
        return dhcpEnabled;
    }

    public void setDHCPEnabled(boolean enabled) {
        dhcpEnabled = enabled;
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
        
        setIp(data.getByteChunk(null, 16, 4));
        setSubnetMask(data.getByteChunk(null, 20, 4));
        port = data.getInt16(24);
        
        int status = data.getInt8(26);
        dhcpEnabled = (status & (1<<6)) != 0;

        setDefaultGateway(data.getByteChunk(null, 28, 4));

        return true;
    }

    @Override
    public void serializeData() {
        super.serializeData();

        data.setByteChunk(ip.getAddress(), 16, Math.min(4, ip.getAddress().length));
        data.setByteChunk(subnetMask.getAddress(), 20, Math.min(4, subnetMask.getAddress().length));
        data.setInt16(port, 24);

        data.setInt8(
            (dhcpEnabled ? (1<<6) : 0),
        26);

        data.setByteChunk(defaultGateway.getAddress(), 28, Math.min(4, defaultGateway.getAddress().length));
    }
}
