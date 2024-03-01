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
import ch.bildspur.artnet.NodeReportCode;
import ch.bildspur.artnet.NodeStyle;
import ch.bildspur.artnet.PortDescriptor;

public class ArtPollReplyPacket extends ArtNetPacket {

    protected static int MIN_LENGTH = HEADER_LENGTH+195;
    protected static int MAX_LENGTH = HEADER_LENGTH+216;

    private InetAddress ip;

    private int versionInfo;
    private int netSwitch;
    private int subSwitch;
    private int oemCode;
    private int nodeStatus;
    private int ubeaVersion;
    private int estaManufacturerCode;

    private String portName;
    private String longName;

    private int numPorts;
    private PortDescriptor[] ports = new PortDescriptor[numPorts];

    private NodeStyle nodeStyle = NodeStyle.ST_NODE;
    private int reportCount = 0;
    private NodeReportCode reportCode = NodeReportCode.RcDefault;

    private byte[] goodInput = new byte[4];
    private byte[] goodOutputA = new byte[4];

    private byte[] dmxIns = new byte[4];
    private byte[] dmxOuts = new byte[4];

    private int acnPriority;
    private int swMacro;
    private int swRemote;

    private byte[] macAddress = new byte[6];
    private InetAddress bindIp;
    private int bindIndex;
    private int status2;
    private byte[] goodOutputB = new byte[4];
    private int status3;

    private byte[] defaultRespUID = new byte[6];
    private int user;
    private int refreshRate;

    public ArtPollReplyPacket() {
        super(PacketType.ART_POLL_REPLY);
    }

    public InetAddress getIPAddress() {
        InetAddress ipClone = null;
        try {
            ipClone = InetAddress.getByAddress(ip.getAddress());
        } catch (UnknownHostException e) {
        }
        return ipClone;
    }

    public void setIPAddress(InetAddress ip) {
        this.ip = ip;
    }

    private void setIPAddress(byte[] address) {
        try {
            ip = InetAddress.getByAddress(address);
            logger.fine("setting ip address: " + ip);
        } catch (UnknownHostException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    public int getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(int versionInfo) {
        this.versionInfo = versionInfo;
    }

    public int getNetSwitch() {
        return netSwitch;
    }

    public void setNetSwitch(int netSwitch) {
		this.netSwitch = netSwitch;
	}

    public int getSubSwitch() {
        return subSwitch;
    }

    public void setSubSwitch(int subSwitch) {
        this.subSwitch = subSwitch;
    }

    public int getOemCode() {
        return oemCode;
    }

    public void setOemCode(int oemCode) {
        this.oemCode = oemCode;
    }

    public int getNodeStatus() {
        return nodeStatus;
    }

    public void setNodeStatus(int nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    public int getUbeaVersion() {
        return ubeaVersion;
    }

    public void setUbeaVersion(int ubeaVersion) {
        this.ubeaVersion = ubeaVersion;
    }

    public int getEstaManufacturerCode() {
        return estaManufacturerCode;
    }

    public void setEstaManufacturerCode(int estaManufacturerCode) {
        this.estaManufacturerCode = estaManufacturerCode;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public int getNumPorts() {
        return numPorts;
    }

    public void setNumPorts(int numPorts) {
        this.numPorts = numPorts;
    }

    public PortDescriptor[] getPorts() {
        return ports;
    }

    public void setPorts(PortDescriptor[] ports) {
        this.ports = ports;
    }

    public NodeStyle getNodeStyle() {
        return nodeStyle;
    }

    public void setNodeStyle(NodeStyle nodeStyle) {
        this.nodeStyle = nodeStyle;
    }

    public int getReportCount() {
		return reportCount;
	}

    // This is used for the controller to detect node event changes
    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
    }

    /**
     * @return the reportCode
     */
    public NodeReportCode getReportCode() {
        return reportCode;
    }

    /**
     * @param reportCode
     *            the reportCode to set
     */
    public void setReportCode(NodeReportCode reportCode) {
        this.reportCode = reportCode;
    }

    public byte[] getGoodInput() {
        return goodInput;
    }

    public void setGoodInput(byte[] goodInput) {
        this.goodInput = goodInput;
    }

    public byte[] getGoodOutputA() {
        return goodOutputA;
    }

    public void setGoodOutputA(byte[] goodOutputA) {
        this.goodOutputA = goodOutputA;
    }

    /**
     * @return the dmxIns
     */
    public byte[] getDmxIns() {
        return dmxIns;
    }

    /**
     * @param dmxIns
     *            the dmxIns to set
     */
    public void setDmxIns(byte[] dmxIns) {
        this.dmxIns = dmxIns;
    }

    /**
     * @return the dmxOuts
     */
    public byte[] getDmxOuts() {
        return dmxOuts;
    }

    /**
     * @param dmxOuts
     *            the dmxOuts to set
     */
    public void setDmxOuts(byte[] dmxOuts) {
        this.dmxOuts = dmxOuts;
    }

    public int getAcnPriority() {
        return acnPriority;
    }

    public void setAcnPriority(int acnPriority) {
        this.acnPriority = acnPriority;
    }

    public int getSwMacro() {
		return swMacro;
	}

    public void setSwMacro(int swMacro) {
		this.swMacro = swMacro;
	}

    public int getSwRemote() {
		return swRemote;
	}

    public void setSwRemote(int swRemote) {
		this.swRemote = swRemote;
	}

    public byte[] getMacAddress() {
		return macAddress;
	}

    public void setMacAddress(byte[] macAddress) {
        this.macAddress = macAddress;
    }

    public InetAddress getBindIp() {
        InetAddress ipClone = null;
        try {
            ipClone = InetAddress.getByAddress(bindIp.getAddress());
        } catch (UnknownHostException e) {
        }
        return ipClone;
	}

	public void setBindIp(InetAddress bindIp) {
		this.bindIp = bindIp;
	}

    private void setBindIp(byte[] address) {
        try {
            bindIp = InetAddress.getByAddress(address);
            logger.fine("setting bindip: " + ip);
        } catch (UnknownHostException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    public int getBindIndex() {
		return bindIndex;
	}

    public void setBindIndex(int bindIndex) {
		this.bindIndex = bindIndex;
	}

    public int getStatus2() {
		return status2;
	}

    public void setStatus2(int status2) {
		this.status2 = status2;
	}

    public byte[] getGoodOutputB() {
		return goodOutputB;
	}

    public void setGoodOutputB(byte[] goodOutputB) {
		this.goodOutputB = goodOutputB;
	}

    public int getStatus3() {
		return status3;
	}

    public void setStatus3(int status3) {
		this.status3 = status3;
	}

    public byte[] getDefaultRespUID() {
		return defaultRespUID;
	}

    public void setDefaultRespUID(byte[] defaultRespUID) {
		this.defaultRespUID = defaultRespUID;
	}

    public int getUser() {
		return user;
	}

    public void setUser(int user) {
		this.user = user;
	}

	public int getRefreshRate() {
		return refreshRate;
	}

	public void setRefreshRate(int refreshRate) {
		this.refreshRate = refreshRate;
	}
    
    @Override
    public boolean parse(byte[] raw) {
        setData(raw, MAX_LENGTH); // Set buffer size to MAX_LENGTH to fill extra fields with zeros

        setIPAddress(data.getByteChunk(null, 10, 4));
        versionInfo = data.getInt16(16);
        netSwitch = data.getInt8(18);
        subSwitch = data.getInt8(19);
        oemCode = data.getInt16(20);
        ubeaVersion = data.getInt8(22);
        nodeStatus = data.getInt8(23);
        estaManufacturerCode = data.getInt16LE(24);
        portName = new String(data.getByteChunk(null, 26, 17));
        longName = new String(data.getByteChunk(null, 44, 63));
        String nodeReport = new String(data.getByteChunk(null, 108, 63));
        reportCode = NodeReportCode.RcDefault; // Default
        reportCount = 0; // Default to 0
        if (nodeReport.length() >= 5) {
            reportCode = NodeReportCode.getForID(nodeReport.substring(0, 5));
            // Try and parse reportCount if it is there
            if (nodeReport.length() >= 7 && nodeReport.indexOf(" ", 6) > 0) {
                String possibleReportCountStr = nodeReport.substring(nodeReport.indexOf(" ", 6));
                try {
                    reportCount = Integer.parseInt(possibleReportCountStr);
                } catch (NumberFormatException e) {}
            }
        }
        numPorts = data.getInt16(172);
        ports = new PortDescriptor[numPorts];
        for (int i = 0; i < numPorts; i++) {
            ports[i] = new PortDescriptor(data.getInt8(174 + i));
        }
        goodInput = data.getByteChunk(null, 178, 4);
        goodOutputA = data.getByteChunk(null, 182, 4);
        dmxIns = data.getByteChunk(null, 186, 4);
        dmxOuts = data.getByteChunk(null, 190, 4);
        for (int i = 0; i < 4; i++) {
            dmxIns[i] &= 0x0f;
            dmxOuts[i] &= 0x0f;
        }
        acnPriority = data.getInt8(194);
        swMacro = data.getInt8(195);
        swRemote = data.getInt8(196);
        int styleID = data.getInt8(200);
        for (NodeStyle s : NodeStyle.values()) {
            if (styleID == s.getStyleID()) {
                nodeStyle = s;
            }
        }
        macAddress = data.getByteChunk(null, 201, 6);

        // The following fields are optional
        // However, the buffer is large enough so they are all just null bytes

        setBindIp(data.getByteChunk(null, 207, 4));
        bindIndex = data.getInt8(211);
        status2 = data.getInt8(212);
        goodOutputB = data.getByteChunk(null, 213, 4);
        status3 = data.getInt8(217);
        defaultRespUID = data.getByteChunk(null, 218, 6);
        user = data.getInt16(224);
        refreshRate = data.getInt16(226);

        return true;
    }

    @Override
    public void serializeData() {
        // Don't call parent serializeData as header format is different
        assert MAX_LENGTH > 0 : "MAX_LENGTH not set";

        data = new ByteUtils(new byte[MAX_LENGTH]);

        // header
        data.setByteChunk(HEADER, 0, HEADER.length);

        // opcode
        data.setInt16LE(PacketType.ART_POLL_REPLY.getOpCode(), 8);

        // ip address
        data.setByteChunk(ip.getAddress(), 10, Math.min(4, ip.getAddress().length));

        // port
        data.setInt16LE(ArtNetServer.DEFAULT_PORT, 14);

        // versinfo
        data.setInt16(versionInfo, 16);

        // netSwitch
        data.setInt8(netSwitch, 18);

        // subSwitch
        data.setInt8(subSwitch, 19);

        // oem
        data.setInt16(oemCode, 20);

        // ubeaVersion
        data.setInt8(ubeaVersion, 22);

        // status1
        data.setInt8(nodeStatus, 23);

        // estaMan code
        data.setInt16LE(estaManufacturerCode, 24);

        // port name
        data.setByteChunk(portName.getBytes(), 26, Math.min(17, portName.getBytes().length));

        // long name
        data.setByteChunk(longName.getBytes(), 44, Math.min(63, longName.getBytes().length));

        // node report
        if (reportCode != null) {
            // id
            byte[] reportCodeData = reportCode.getID().getBytes();
            data.setByteChunk(reportCodeData, 108, reportCodeData.length);

            // description (with counter, modulo 10,000 so its always a 4 digit number)
            String reportDescription = String.format(" %04d ", reportCount % 10000) + reportCode.getDescription();
            byte[] reportDescriptionData = reportDescription.getBytes();
            data.setByteChunk(reportDescriptionData, 113, Math.min(59, reportDescriptionData.length));
        }

        // num ports
        data.setInt16(numPorts, 172);

        // ports
        for (int i = 0; i < numPorts; i++) {
            data.setInt8(ports[i].getData(), 174 + i);
        }

        // goodinput
        data.setByteChunk(goodInput, 178, Math.min(4, goodInput.length));

        // goodoutputa
        data.setByteChunk(goodOutputA, 182, Math.min(4, goodOutputA.length));

        // dmx ins
        data.setByteChunk(dmxIns, 186, Math.min(4, dmxIns.length));

        // dmx outs
        data.setByteChunk(dmxIns, 190, Math.min(4, dmxIns.length));

        // acn priority
        data.setInt8(acnPriority, 194);

        // swmacro
        data.setInt8(swMacro, 195);

        // swremote
        data.setInt8(swRemote, 196);

        // style
        data.setInt8(nodeStyle.getStyleID(), 200);

        // macaddress
        data.setByteChunk(macAddress, 201, Math.min(6, macAddress.length));

        // bindip
        data.setByteChunk(bindIp.getAddress(), 207, Math.min(4, bindIp.getAddress().length));

        // bindindex
        data.setInt8(bindIndex, 211);

        // status2
        data.setInt8(status2, 212);

        // goodoutputb
        data.setByteChunk(goodOutputB, 213, Math.min(4, goodOutputB.length));

        // status 3
        data.setInt8(status3, 217);

        // default resp uid
        data.setByteChunk(defaultRespUID, 218, Math.min(6, defaultRespUID.length));

        // user
        data.setInt16(user, 224);

        // refreshrate
        data.setInt16(refreshRate, 226);
    }
}
