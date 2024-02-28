package ch.bildspur.artnet.packets;

import java.util.Arrays;

public class ArtDmxPacket extends ArtNetPacket {

    protected static int MIN_LENGTH = HEADER_LENGTH+6+2;
    protected static int MAX_LENGTH = HEADER_LENGTH+6+512;

    private int sequenceID;
    private int portAddress = 0;
    private int physicalPort = 0;
    private int numChannels;
    private byte[] dmxData;

    public ArtDmxPacket() {
        super(PacketType.ART_DMX);
    }
    
    /**
     * @return the sequenceID
     */
    public int getSequenceID() {
        return sequenceID;
    }

    public void setSequenceID(int id) {
        sequenceID = id % 0xff;
    }

    public int getPortAddress() {
        return portAddress;
    }

    public void setPortAddress(int portAddress) {
        this.portAddress = portAddress & 0x7fff;
        logger.finer("Port Address set to: " + this.portAddress);
    }

    public int getPhysicalPort() {
        return physicalPort;
    }

    public void setPhysicalPort(int physicalPort) {
        this.physicalPort = physicalPort;
    }

    /**
     * @return the number of DMX channels
     */
    public int getNumChannels() {
        return numChannels;
    }
    
    public byte[] getDmxData() {
        return dmxData;
    }

    public void setDMX(byte[] dmxData, int numChannels) {
        logger.finer("setting DMX data for: " + numChannels + " channels");
        this.dmxData = Arrays.copyOf(dmxData, numChannels);
        this.numChannels = numChannels;
    }

    @Override
    public boolean parse(byte[] raw) {
        if (raw.length < MIN_LENGTH) return false;
        setData(raw);
        sequenceID = data.getInt8(12);
        physicalPort = data.getInt8(13);
        portAddress = data.getInt16LE(14) & 0x7fff; // Abstract 15-bit port address, instead of treating separately
        numChannels = data.getInt16(16);
        if (18+numChannels > raw.length) return false; // Not enough data
        dmxData = data.getByteChunk(dmxData, 18, numChannels);
        return true;
    }

    @Override
    public void serializeData() {
        super.serializeData();
        data.setInt8(sequenceID, 12);
        data.setInt8(physicalPort, 13);
        data.setInt16LE(portAddress, 14);

        int adjusted_channels = numChannels;
        if (adjusted_channels % 2 == 0) adjusted_channels++; // Must be a multiple of 2
        adjusted_channels = Math.max(2, Math.min(512, adjusted_channels)); // Must be 2-512

        data.setInt16(adjusted_channels, 16);
        data.setByteChunk(dmxData, 18, Math.min(numChannels, adjusted_channels)); // Only copy channels we have, extra are zero
        data.setLength(18+adjusted_channels);
    }
}