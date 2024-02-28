package ch.bildspur.artnet.packets;

import java.util.Arrays;

public class ArtNzsPacket extends ArtNetPacket {

    protected static int MIN_LENGTH = HEADER_LENGTH+6+1;
    protected static int MAX_LENGTH = HEADER_LENGTH+6+512;

    private int sequenceID;
    private int startCode;
    private int portAddress = 0;
    private int numChannels;
    private byte[] dmxData;

    public ArtNzsPacket() {
        super(PacketType.ART_NZS);
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

    public int getStartCode() {
        return startCode;
    }

    public void setStartCode(int startCode) {
        assert startCode != 0 : "ArtNzs startCode cannot be zero";
        this.startCode = startCode;
    }

    public int getPortAddress() {
        return portAddress;
    }

    public void setPortAddress(int portAddress) {
        this.portAddress = portAddress & 0x7fff;
        logger.finer("Port Address set to: " + this.portAddress);
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
        startCode = data.getInt8(13);
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
        data.setInt8(startCode, 13);
        data.setInt16LE(portAddress, 14);

        int adjusted_channels = numChannels;
        adjusted_channels = Math.max(1, Math.min(512, adjusted_channels)); // Must be 1-512

        data.setInt16(adjusted_channels, 16);
        data.setByteChunk(dmxData, 18, Math.min(numChannels, adjusted_channels)); // Only copy channels we have, extra are zero
        data.setLength(18+adjusted_channels);
    }
}